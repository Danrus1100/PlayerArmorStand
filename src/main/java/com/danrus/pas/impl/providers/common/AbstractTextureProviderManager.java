package com.danrus.pas.impl.providers.common;

import com.danrus.pas.api.DownloadStatus;
import com.danrus.pas.api.data.*;
import com.danrus.pas.api.info.NameInfo;
import com.danrus.pas.api.reg.InfoTranslators;
import com.danrus.pas.managers.PasManager;
import net.minecraft.resources.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;

public abstract class AbstractTextureProviderManager<T extends DataHolder> implements TextureProvidersManager {

    private final Logger LOGGER = LoggerFactory.getLogger(this.getName());

    private final AtomicBoolean initialized = new AtomicBoolean(false);
    private PasManager pasManager;

    private final Map<String, List<PrioritizedProvider>> providers = new ConcurrentHashMap<>();
    private final Map<Identifier, CompletableFuture<T>> pendingDownloads = new ConcurrentHashMap<>();

    public void clearPending() { pendingDownloads.clear(); }

    public void initialize(PasManager manager) {
        if (initialized.compareAndSet(false, true)) {
            this.pasManager = manager;
            this.prepareProviders();
        }
    }
    @Override
    public void addProvider(TextureProvider provider) {
        addProvider(provider, 0);
    }

    @Override
    public void addProvider(TextureProvider provider, int priority) {
        providers
                .computeIfAbsent(provider.getLiteral(), k -> new ArrayList<>())
                .add(new PrioritizedProvider(provider, priority));

        providers.get(provider.getLiteral())
                .sort(Comparator.comparingInt(PrioritizedProvider::priority).reversed());

        if (pasManager != null && pasManager.getExistingProviders() != null && registerAsExistingProvider()) {
            pasManager.getExistingProviders().add(provider.getLiteral());
        }
    }

    @Override
    public void download(NameInfo info) {
        if (info.base().isEmpty()) {
            LOGGER.warn("{}: Invalid input {}", getClass().getSimpleName(), info.base());
            return;
        }

        final Identifier pendingKey = InfoTranslators.getInstance().toIdentifier(getType(), info);
        CompletableFuture<T> created = new CompletableFuture<>();
        CompletableFuture<T> shared = pendingDownloads.putIfAbsent(pendingKey, created);

        if (shared == null) {
            shared = created;
            created.whenComplete((data, throwable) ->
                    pendingDownloads.remove(pendingKey, created));
            try {
                startDownload(info, pendingKey, created);
            } catch (Exception exception) {
                created.completeExceptionally(exception);
            }
        }

        subscribe(info, shared);
    }

    private void startDownload(
            NameInfo info,
            Identifier pendingKey,
            CompletableFuture<T> result
    ) {
        CompletableFuture<Void> providerFuture = null;

        for (char c : getExcludeLiterals().toCharArray()) {
            String literal = String.valueOf(c);
            if (getProvider(info).equals(literal)) {
                providerFuture = tryLoadFromProviders(literal, info, pendingKey);
                if (providerFuture != null) {
                    break;
                }
            }
        }

        if (providerFuture == null && !getExcludeLiterals().contains(getProvider(info))) {
            String literal = getProvider(info);
            providerFuture = tryLoadFromProviders(literal, info, pendingKey);
        }

        if (providerFuture == null) {
            providerFuture = tryLoadFromProviders(getDefaultLiteral(), info, pendingKey);
        }

        if (providerFuture == null) {
            IllegalStateException exception = new IllegalStateException(
                    "No provider could load " + info.base() + " with NameInfo: " + info);
            LOGGER.error("{}: {}", getClass().getSimpleName(), exception.getMessage());
            if (pasManager != null) {
                this.getDataManager().invalidateData(info);
            }
            result.completeExceptionally(exception);
            return;
        }

        CompletableFuture<Void> selectedFuture = providerFuture;
        selectedFuture.whenComplete((ignored, throwable) -> {
            if (throwable != null) {
                result.completeExceptionally(throwable);
                return;
            }

            Optional<T> data = getDataManager().peek(info);
            if (data.isEmpty() || data.get().getStatus() != DownloadStatus.COMPLETED) {
                result.completeExceptionally(new IllegalStateException(
                        "Provider completed without valid data for " + info));
                return;
            }

            result.complete(data.get());
        });
    }

    private void subscribe(NameInfo info, CompletableFuture<T> download) {
        download.whenComplete((data, throwable) -> {
            if (throwable == null) {
                getDataManager().store(info, data);
            } else {
                LOGGER.error("Failed to download texture for {}", info, throwable);
                getDataManager().invalidateData(info);
            }
        });
    }

    private CompletableFuture<Void> tryLoadFromProviders(
            String literal,
            NameInfo info,
            Identifier pendingKey
    ) {
        return tryLoad(providers.get(literal), info, pendingKey);
    }

    private CompletableFuture<Void> tryLoad(
            List<PrioritizedProvider> providerList,
            NameInfo info,
            Identifier pendingKey
    ) {
        if (providerList == null || providerList.isEmpty()) return null;

        for (PrioritizedProvider prioritized : providerList) {
            try {
                LOGGER.info("Trying to download {} from {}", pendingKey, prioritized.provider.getClass().getSimpleName());
                return prioritized.provider().load(info);
            } catch (Exception e) {
                LOGGER.error(
                        "Provider {} failed to load {}: {}",
                        prioritized.provider().getClass().getSimpleName(), pendingKey, e.getMessage()
                );
            }
        }
        return null;
    }

    protected abstract Class<? extends DataHolder> getType();
    protected abstract void prepareProviders();
    protected abstract String getProvider(NameInfo info);
    protected abstract String getName();
    protected abstract String getDefaultLiteral();
    protected abstract String getExcludeLiterals();
    protected abstract DataRepository<T> getDataManager();
    protected abstract boolean registerAsExistingProvider();

    private record PrioritizedProvider(TextureProvider provider, int priority) {}
}
