package com.danrus.pas.impl.providers.common;

import com.danrus.pas.api.data.*;
import com.danrus.pas.api.info.NameInfo;
import com.danrus.pas.managers.PasManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public abstract class AbstractTextureProviderManager<T extends DataHolder> implements TextureProvidersManager {

    private final Logger LOGGER = LoggerFactory.getLogger(this.getName());

    private boolean initialized = false;
    private PasManager pasManager;

    private final Map<String, List<PrioritizedProvider>> providers = new HashMap<>();
    private final Set<String> pendingSet = Collections.newSetFromMap(new ConcurrentHashMap<>());

    public void initialize(PasManager manager) {
        if (!initialized) {
            this.pasManager = manager;
            initialized = true;
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
        String id = getOutputString(info);

        if (!pendingSet.add(id)) {
            return;
        }

        boolean taskScheduled = false;

        try {
            if (info.base().isEmpty()) {
                LOGGER.warn("{}: Invalid input {}", getClass().getSimpleName(), info.base());
                return;
            }

            for (char c : getExcludeLiterals().toCharArray()) {
                String literal = String.valueOf(c);
                if (getProvider(info).equals(literal)) {
                    if (tryLoadFromProviders(literal, info)) {
                        taskScheduled = true;
                        break;
                    }
                }
            }

            if (!taskScheduled && !getExcludeLiterals().contains(getProvider(info))) {
                String literal = getProvider(info);
                if (tryLoadFromProviders(literal, info)) {
                    taskScheduled = true;
                }
            }

            if (!taskScheduled) {
                if (tryLoadFromProviders(getDefaultLiteral(), info)) {
                    taskScheduled = true;
                }
            }

            if (!taskScheduled) {
                LOGGER.error("{}: No provider could load {} with NameInfo: {}",
                        getClass().getSimpleName(), info.base(), info);

                if (pasManager != null) {
                    this.getDataManager().invalidateData(info);
                }
            }

        } finally {
            if (!taskScheduled) {
                pendingSet.remove(id);
            }
        }
    }

    private boolean tryLoadFromProviders(String literal, NameInfo info) {
        return tryLoad(providers.get(literal), info);
    }

    private boolean tryLoad(List<PrioritizedProvider> providerList, NameInfo info) {
        if (providerList == null || providerList.isEmpty()) return false;

        for (PrioritizedProvider prioritized : providerList) {
            try {
                LOGGER.info("Trying to download from {}", prioritized.provider.getClass().getSimpleName());
                prioritized.provider().load(info, (result) -> {
                    pendingSet.remove(getOutputString(info));
                });

                return true;
            } catch (Exception e) {
                LOGGER.error(
                        "Provider {} failed to start load {}: {}",
                        prioritized.provider().getClass().getSimpleName(), getOutputString(info), e.getMessage()
                );
            }
        }
        return false;
    }

    protected abstract String getOutputString(NameInfo info);

    protected abstract void prepareProviders();
    protected abstract String getProvider(NameInfo info);
    protected abstract String getName();
    protected abstract String getDefaultLiteral();
    protected abstract String getExcludeLiterals();
    protected abstract DataRepository<T> getDataManager();
    protected abstract boolean registerAsExistingProvider();

    private record PrioritizedProvider(TextureProvider provider, int priority) {}
}