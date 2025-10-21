package com.danrus.pas.managers;

import com.danrus.pas.api.NameInfo;
import com.danrus.pas.api.TextureProvider;
import com.danrus.pas.api.TextureProvidersManager;
import com.danrus.pas.impl.providers.MojangSkinProvider;
import com.danrus.pas.impl.providers.NamemcSkinProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * Manages registration and selection of {@link TextureProvider}s by literal markers.
 */
public class SkinProvidersManager implements TextureProvidersManager {

    private static final String DEFAULT_LITERAL = "M";  // Default literal for providers
    private static final String EXCLUDE_LITERALS = "NF"; // Prevents fallback to default providers
    private static final Logger LOGGER = LoggerFactory.getLogger("SkinProvidersManagerImpl");

    // Key: literal, Value: sorted list of providers
    private final Map<String, List<PrioritizedProvider>> providers = new HashMap<>();

    private final List<String> pendingList = new ArrayList<>();

    public SkinProvidersManager() {
        addProvider(new MojangSkinProvider());
        addProvider(new NamemcSkinProvider());
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

        // Keep providers sorted at all times
        providers.get(provider.getLiteral())
                .sort(Comparator.comparingInt(PrioritizedProvider::priority).reversed());
    }

    @Override
    public void download(NameInfo info) {
//        LOGGER.info("Download initialized: " + input);

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
            if (info.params().indexOf(c) >= 0) {
                if (tryLoadFromProviders(String.valueOf(c), info)) {
                    loaded = true;
                    break;
                }
            }
        }

        if (!loaded) {
            for (var entry : providers.entrySet()) {
                String literal = entry.getKey();
                if (EXCLUDE_LITERALS.contains(literal)) continue; // Пропускаем те, что уже проверили
                if (info.params().contains(literal) && tryLoad(entry.getValue(), info)) {
                    loaded = true;
                    break;
                }
            }
        }

        if (!loaded) {
            if (tryLoadFromProviders(DEFAULT_LITERAL, info)) {
                loaded = true;
            }
        }

        if (!loaded) {
            LOGGER.error(getClass().getSimpleName() +
                    ": No provider could load " + info.base() + " with params: " + info.params());
            PasManager.getInstance().getDataManager().invalidateData(info);
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
