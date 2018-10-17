package com.equestriworlds.account.event;

import java.util.UUID;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * Name based online player cache.
 * See CoreClientManager.
 */
public class ClientLoadEvent
extends Event {
    private static final HandlerList handlers = new HandlerList();
    private String _name;
    private UUID _uuid;

    public ClientLoadEvent(String name, UUID uuid) {
        this._name = name;
        this._uuid = uuid;
    }

    public String GetName() {
        return this._name;
    }

    public UUID GetUUID() {
        return this._uuid;
    }

    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
