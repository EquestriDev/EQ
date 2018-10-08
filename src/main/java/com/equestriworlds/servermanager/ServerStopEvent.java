/*
 * Decompiled with CFR 0_133.
 * 
 * Could not load the following classes:
 *  org.bukkit.event.Event
 *  org.bukkit.event.HandlerList
 */
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
