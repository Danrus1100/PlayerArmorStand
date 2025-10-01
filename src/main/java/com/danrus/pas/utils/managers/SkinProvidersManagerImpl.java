package com.danrus.pas.utils.managers;

import com.danrus.pas.PlayerArmorStandsClient;
import com.danrus.pas.api.SkinProvider;
import com.danrus.pas.api.SkinProvidersManager;
import com.danrus.pas.utils.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * Manages registration and selection of {@link SkinProvider}s by literal markers.
 */
public class SkinProvidersManagerImpl implements SkinProvidersManager {

    private static final String DEFAULT_LITERAL = "M";  // Default literal for providers
    private static final String EXCLUDE_LITERALS = "NF"; // Prevents fallback to default providers
    private static final Logger LOGGER = LoggerFactory.getLogger("SkinProvidersManagerImpl");

    // Key: literal, Value: sorted list of providers
    private final Map<String, List<PrioritizedProvider>> providers = new HashMap<>();

    private final List<String> pendingList = new ArrayList<>();

    @Override
    public void addProvider(SkinProvider provider) {
        addProvider(provider, 0);
    }

    @Override
    public void addProvider(SkinProvider provider, int priority) {
        providers
                .computeIfAbsent(provider.getLiteral(), k -> new ArrayList<>())
                .add(new PrioritizedProvider(provider, priority));

        // Keep providers sorted at all times
        providers.get(provider.getLiteral())
                .sort(Comparator.comparingInt(PrioritizedProvider::priority).reversed());
    }

    @Override
    public void download(String input) {
//        LOGGER.info("Download initialized: " + input);
        List<String> parts = StringUtils.matchASName(input);
        String name = parts.get(0);
        String params = parts.get(1);

        if (pendingList.contains(input)) {
            return;
        }

        if (name.isEmpty()) {
            LOGGER.warn(getClass().getSimpleName() +
                    ": Invalid input " + input);
            return;
        }

        boolean loaded = false;

        for (char c : EXCLUDE_LITERALS.toCharArray()) {
            if (params.indexOf(c) >= 0) {
                if (tryLoadFromProviders(String.valueOf(c), input, name)) {
                    loaded = true;
                    break;
                }
            }
        }

        if (!loaded) {
            for (var entry : providers.entrySet()) {
                String literal = entry.getKey();
                if (EXCLUDE_LITERALS.contains(literal)) continue; // Пропускаем те, что уже проверили
                if (params.contains(literal) && tryLoad(entry.getValue(), input, name)) {
                    loaded = true;
                    break;
                }
            }
        }

        if (!loaded) {
            if (tryLoadFromProviders(DEFAULT_LITERAL, input, name)) {
                loaded = true;
            }
        }

        if (!loaded) {
            LOGGER.error(getClass().getSimpleName() +
                    ": No provider could load " + name + " with params: " + params);
            SkinManger.getInstance().getDataManager().invalidateData(name);
        }
    }

    private boolean tryLoadFromProviders(String literal, String input, String name) {
        return tryLoad(providers.get(literal), input, name);
    }

    private boolean tryLoad(List<PrioritizedProvider> providerList, String input, String name) {
        if (providerList == null || providerList.isEmpty()) return false;


        for (PrioritizedProvider prioritized : providerList) {
            try {
                LOGGER.info("Trying to download from {}", prioritized.provider.getClass().getSimpleName());
                pendingList.add(input);
                prioritized.provider().load(input, pendingList::remove);
                return true;
            } catch (Exception e) {
                LOGGER.error(
                        "Provider {} failed to load {}: {}",
                        prioritized.provider().getClass().getSimpleName(), name, e.getMessage()
                );
            }
        }
        return false;
    }

    private record PrioritizedProvider(SkinProvider provider, int priority) {}
}
