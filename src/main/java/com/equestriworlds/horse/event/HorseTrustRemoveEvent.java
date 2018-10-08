/*
 * Decompiled with CFR 0_133.
 * 
 * Could not load the following classes:
 *  org.bukkit.OfflinePlayer
 *  org.bukkit.event.Event
 *  org.bukkit.event.HandlerList
 */
package com.equestriworlds.horse.event;

import com.equestriworlds.horse.config.CustomHorse;
import org.bukkit.OfflinePlayer;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class HorseTrustRemoveEvent
extends Event {
    private static final HandlerList handlers = new HandlerList();
    private CustomHorse _horse;
    private OfflinePlayer _player;

    public HorseTrustRemoveEvent(CustomHorse horse, OfflinePlayer player) {
    }

    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    public CustomHorse GetCustomHorse() {
        return this._horse;
    }

    public OfflinePlayer GetPlayer() {
        return this._player;
    }
}
