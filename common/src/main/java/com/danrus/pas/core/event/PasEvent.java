package com.danrus.pas.core.event;

public abstract class PasEvent {
    private boolean cancelled = false;

    public void setCancelled(boolean cancel) {
        this.cancelled = cancel;
    }

    public boolean isCancelled() {
        return cancelled;
    }
}
