package com.danrus.pas.impl.providers.common;

import com.danrus.pas.api.data.*;
import com.danrus.pas.api.info.NameInfo;
import com.danrus.pas.managers.PasManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;

public abstract class AbstractTextureProviderManager<T extends DataHolder> implements TextureProvidersManager {

    private final Logger LOGGER = LoggerFactory.getLogger(this.getName());

    private final AtomicBoolean initialized = new AtomicBoolean(false);
    private PasManager pasManager;

    private final Map<String, List<PrioritizedProvider>> providers = new ConcurrentHashMap<>();
    private final Set<String> pendingList = ConcurrentHashMap.newKeySet();

    public void clearPending() { pendingList.clear(); }

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
            LOGGER.warn(getClass().getSimpleName() +
                    ": Invalid input " + info.base());
            return;
        }

        final String pendingKey = getPendingKey(info);
        if (!pendingList.add(pendingKey)) {
            return;
        }

        boolean loaded = false;

        for (char c : getExcludeLiterals().toCharArray()) {
            String literal = String.valueOf(c);
            if (getProvider(info).equals(literal)) {
                if (tryLoadFromProviders(literal, info, pendingKey)) {
                    loaded = true;
                    break;
                }
            }
        }

        if (!loaded && !getExcludeLiterals().contains(getProvider(info))) {
            String literal = getProvider(info);
            if (tryLoadFromProviders(literal, info, pendingKey)) {
                loaded = true;
            }
        }

        if (!loaded) {
            if (tryLoadFromProviders(getDefaultLiteral(), info, pendingKey)) {
                loaded = true;
            }
        }

        if (!loaded) {
            pendingList.remove(pendingKey);
            LOGGER.error(getClass().getSimpleName() +
                    ": No provider could load " + info.base() + " with NameInfo: " + info);
            if (pasManager != null) {
                this.getDataManager().invalidateData(info);
            }
        }
    }

    private boolean tryLoadFromProviders(String literal, NameInfo info, String pendingKey) {
        return tryLoad(providers.get(literal), info, pendingKey);
    }

    private boolean tryLoad(List<PrioritizedProvider> providerList, NameInfo info, String pendingKey) {
        if (providerList == null || providerList.isEmpty()) return false;

        for (PrioritizedProvider prioritized : providerList) {
            try {
                LOGGER.info("Trying to download {} from {}", pendingKey, prioritized.provider.getClass().getSimpleName());
                prioritized.provider().load(info, ignored -> pendingList.remove(pendingKey));
                return true;
            } catch (Exception e) {
                LOGGER.error(
                        "Provider {} failed to load {}: {}",
                        prioritized.provider().getClass().getSimpleName(), pendingKey, e.getMessage()
                );
            }
        }
        return false;
    }

    protected abstract String getPendingKey(NameInfo info);

    protected abstract void prepareProviders();
    protected abstract String getProvider(NameInfo info);
    protected abstract String getName();
    protected abstract String getDefaultLiteral();
    protected abstract String getExcludeLiterals();
    protected abstract DataRepository<T> getDataManager();
    protected abstract boolean registerAsExistingProvider();

    private record PrioritizedProvider(TextureProvider provider, int priority) {}
}
