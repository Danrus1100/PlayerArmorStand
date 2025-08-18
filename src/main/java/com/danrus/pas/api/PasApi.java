package com.danrus.pas.api;

import com.danrus.pas.core.event.PasEvent;
import com.danrus.pas.core.event.PasEventBus;

public class PasApi {
    public static final String API_VERSION = "1.0.0";
    private static final PasEventBus EVENT_BUS = new PasEventBus();

    /**
     * Returns the current API version.
     *
     * @return The API version as a string.
     */
    public static String getApiVersion() {
        return API_VERSION;
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
