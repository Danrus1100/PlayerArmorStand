package com.danrus.pas.managers;

import com.danrus.pas.api.NameInfo;
import com.danrus.pas.api.TextureProvider;
import com.danrus.pas.api.TextureProvidersManager;
import com.danrus.pas.impl.providers.skin.MojangSkinProvider;
import com.danrus.pas.impl.providers.skin.NamemcSkinProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class SkinProvidersManager implements TextureProvidersManager {

    private static final String DEFAULT_LITERAL = "M";
    private static final String EXCLUDE_LITERALS = "NF";
    private static final Logger LOGGER = LoggerFactory.getLogger("SkinProvidersManagerImpl");

    private final Map<String, List<PrioritizedProvider>> providers = new HashMap<>();
    private final List<String> pendingList = new ArrayList<>();

    private PasManager pasManager;
    private boolean initialized = false;

    public SkinProvidersManager() {
        // Пустой конструктор
    }

    /**
     * Устанавливает ссылку на PasManager и инициализирует дефолтные провайдеры
     */
    public void initialize(PasManager manager) {
        if (!initialized) {
            this.pasManager = manager;
            initialized = true;

            // Теперь безопасно добавлять провайдеры
            addProvider(new MojangSkinProvider());
            addProvider(new NamemcSkinProvider());
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

        // Используем сохраненную ссылку вместо getInstance()
        if (pasManager != null && pasManager.getExistingProviders() != null) {
            pasManager.getExistingProviders().add(provider.getLiteral());
        }
    }

    @Override
    public void download(NameInfo info) {
        if (pendingList.contains(info.base())) {
            return;
        }

        if (info.base().isEmpty()) {
            LOGGER.warn(getClass().getSimpleName() +
                    ": Invalid input " + info.base());
            return;
        }

        boolean loaded = false;

        for (char c : EXCLUDE_LITERALS.toCharArray()) {
            String literal = String.valueOf(c);
            if (info.getDesiredProvider().equals(literal)) {
                if (tryLoadFromProviders(literal, info)) {
                    loaded = true;
                    break;
                }
            }
        }

        if (!loaded && !EXCLUDE_LITERALS.contains(info.getDesiredProvider())) {
            String literal = info.getDesiredProvider();
            if (tryLoadFromProviders(literal, info)) {
                loaded = true;
            }
        }

        if (!loaded) {
            if (tryLoadFromProviders(DEFAULT_LITERAL, info)) {
                loaded = true;
            }
        }

        if (!loaded) {
            LOGGER.error(getClass().getSimpleName() +
                    ": No provider could load " + info.base() + " with NameInfo: " + info);
            if (pasManager != null) {
                pasManager.getDataManager().invalidateData(info);
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
                pendingList.add(info.base());
                prioritized.provider().load(info, pendingList::remove);
                return true;
            } catch (Exception e) {
                LOGGER.error(
                        "Provider {} failed to load {}: {}",
                        prioritized.provider().getClass().getSimpleName(), info.base(), e.getMessage()
                );
            }
        }
        return false;
    }

    private record PrioritizedProvider(TextureProvider provider, int priority) {}
}
