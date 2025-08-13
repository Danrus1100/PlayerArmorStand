package com.danrus.pas.utils.managers;

import com.danrus.pas.PlayerArmorStandsClient;
import com.danrus.pas.api.SkinProvider;
import com.danrus.pas.api.SkinProvidersManager;
import com.danrus.pas.utils.StringUtils;

import java.util.*;

public class SkinProvidersManagerImpl implements SkinProvidersManager {

    private final Map<String, List<PrioritizedProvider>> providers = new HashMap<>();
    private final String excludeLiterals = "NF"; // Exclude literals. for NOT default providers

    public void addProvider(SkinProvider provider) {
        addProvider(provider, 0);
    }

    public void addProvider(SkinProvider provider, int priority) {
        String literal = provider.getLiteral();
        List<PrioritizedProvider> literalProviders = providers.computeIfAbsent(literal, k -> new ArrayList<>());
        literalProviders.add(new PrioritizedProvider(provider, priority));
        // Sort by priority (higher priority first)
        literalProviders.sort(Comparator.comparingInt(PrioritizedProvider::priority).reversed());
    }

    public void download(String string) {
        String name = StringUtils.matchASName(string).get(0);
        String params = StringUtils.matchASName(string).get(1);

        boolean loaded = false;

        // Check for providers based on literals in params
        for (var entry : providers.entrySet()) {
            String literal = entry.getKey();
            if (params.contains(literal)) {
                List<PrioritizedProvider> literalProviders = entry.getValue();
                // Try each provider for this literal in order (sorted by priority)
                for (PrioritizedProvider prioritizedProvider : literalProviders) {
                    try {
                        prioritizedProvider.provider().load(name);
                        loaded = true;
                        break; // Break from provider loop if successful
                    } catch (Exception e) {
                        // Continue to next provider if this one fails
                        PlayerArmorStandsClient.LOGGER.debug("Provider " +
                            prioritizedProvider.provider().getClass().getSimpleName() +
                            " failed to load " + name + ": " + e.getMessage());
                    }
                }
                if (loaded) break; // Break from literal loop if loaded successfully
            }
        }

        // If no specific provider loaded, try default "M" providers if allowed
        if (!loaded && !params.matches(".*[" + excludeLiterals + "].*")) {
            List<PrioritizedProvider> defaultProviders = providers.get("M");
            if (defaultProviders != null) {
                for (PrioritizedProvider prioritizedProvider : defaultProviders) {
                    try {
                        prioritizedProvider.provider().load(name);
                        loaded = true;
                        break;
                    } catch (Exception e) {
                        PlayerArmorStandsClient.LOGGER.debug("Default provider " +
                            prioritizedProvider.provider().getClass().getSimpleName() +
                            " failed to load " + name + ": " + e.getMessage());
                    }
                }
            }
        }

        // Log warning if no provider could load
        if (!loaded) {
            PlayerArmorStandsClient.LOGGER.warn(this.getClass().getName() +
                ": No provider could load " + name + " with params: " + params);
            SkinManger.getInstance().getDataManager().invalidateData(name);
        }
    }

    // Helper class to store a provider with its priority
    private record PrioritizedProvider(SkinProvider provider, int priority) {}
}