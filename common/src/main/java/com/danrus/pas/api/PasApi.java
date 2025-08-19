package com.danrus.pas.api;

import com.danrus.pas.api.registry.PasFeatureRegistry;
import com.danrus.pas.api.registry.PromisesRegistry;
import com.danrus.pas.api.request.result.Promise;
import com.danrus.pas.core.event.PasEvent;
import com.danrus.pas.core.event.PasEventBus;

public class PasApi {
    public static final String API_VERSION = "1.0.0";
    private static final PasEventBus EVENT_BUS = new PasEventBus();
    private static final PasFeatureRegistry FEATURE_REGISTRY = new PasFeatureRegistry();
    private static final PromisesRegistry<? extends Promise<?>> PROMISES_REGISTRY = new PromisesRegistry<>();

    /**
     * Returns the current API version.
     *
     * @return The API version as a string.
     */
    public static String getApiVersion() {
        return API_VERSION;
    }

    /**
     * Returns the feature registry instance.
     *
     * @return The PasFeatureRegistry instance.
     */
    public static PasFeatureRegistry getFeatureRegistry() {
        return FEATURE_REGISTRY;
    }

    /**
     * Returns the promises registry instance.
     *
     * @return The PromisesRegistry instance.
     */
    public static PromisesRegistry<? extends Promise<?>> getPromisesRegistry() {
        return PROMISES_REGISTRY;
    }

    /**
     * Registers a listener to the event bus.
     *
     * @param listener The listener object that will handle events.
     */

    public static void registerListener(Object listener) {
        EVENT_BUS.register(listener);
    }

    /**
     * Posts an event to the event bus.
     *
     * @param event The event to be posted.
     */
    public static void postEvent(PasEvent event) {
        EVENT_BUS.post(event);
    }
}
