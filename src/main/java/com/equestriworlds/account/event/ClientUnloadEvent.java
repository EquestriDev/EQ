package com.equestriworlds.account.event;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * Name based online player cache.
 * See CoreClientManager.
 */
public class ClientUnloadEvent
extends Event {
    private static final HandlerList handlers = new HandlerList();
    private String _name;

    public ClientUnloadEvent(String name) {
        this._name = name;
    }

    public String GetName() {
        return this._name;
    }

    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
