package com.equestriworlds.servermanager;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class ServerStopEvent
extends Event {
    private static final HandlerList handlers = new HandlerList();

    ServerStopEvent() {
    }

    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
