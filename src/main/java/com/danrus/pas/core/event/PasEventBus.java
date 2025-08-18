package com.danrus.pas.core.event;

import java.lang.reflect.Method;
import java.util.*;

public class PasEventBus {
    private final Map<Class<? extends PasEvent>, List<Handler>> handlers = new HashMap<>();

    // Inner class to hold the listener and method information
    private record Handler(Object listener, Method method, int priority) {

        void execute(PasEvent event) {
            try {
                method.invoke(listener, event);
            } catch (Exception e) {
                System.err.println("Error in Handler: " + e.getMessage());
            }
        }
    }

    // Listener registration
    public void register(Object listener) {
        for (Method method : listener.getClass().getDeclaredMethods()) {
            if (method.isAnnotationPresent(PasEventHandler.class)) {
                registerMethod(listener, method);
            }
        }
    }

    // Method to register a single method as an event handler
    private void registerMethod(Object listener, Method method) {
        Class<?>[] params = method.getParameterTypes();
        if (params.length != 1 || !PasEvent.class.isAssignableFrom(params[0])) {
            throw new IllegalArgumentException("Method " + method.getName() + " must have exactly one parameter of type PasEvent");
        }

        @SuppressWarnings("unchecked")
        Class<? extends PasEvent> eventType = (Class<? extends PasEvent>) params[0];
        PasEventHandler annotation = method.getAnnotation(PasEventHandler.class);

        handlers.computeIfAbsent(eventType, k -> new ArrayList<>())
                .add(new Handler(listener, method, annotation.priority()));

        // Sort by priority
        handlers.get(eventType).sort(Comparator.comparingInt(h -> -h.priority));
    }

    // Call event handlers for the given event
    public void post(PasEvent event) {
        List<Handler> eventHandlers = handlers.get(event.getClass());
        if (eventHandlers == null) return;

        for (Handler handler : eventHandlers) {
            if (event.isCancelled()) break;
            handler.execute(event);
        }
    }
}
