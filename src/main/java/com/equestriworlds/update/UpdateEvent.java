/*
 * Decompiled with CFR 0_133.
 * 
 * Could not load the following classes:
 *  org.bukkit.event.Event
 *  org.bukkit.event.HandlerList
 */
package com.equestriworlds.update;

import com.equestriworlds.update.UpdateType;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class UpdateEvent
extends Event {
    private static final HandlerList handlers = new HandlerList();
    private UpdateType _type;

    UpdateEvent(UpdateType example) {
        this._type = example;
    }

    public UpdateType getType() {
        return this._type;
    }

    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
