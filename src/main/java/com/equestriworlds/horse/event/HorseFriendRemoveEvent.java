package com.equestriworlds.horse.event;

import com.equestriworlds.horse.config.CustomHorse;
import org.bukkit.OfflinePlayer;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class HorseFriendRemoveEvent
extends Event {
    private static final HandlerList handlers = new HandlerList();
    private CustomHorse _horse;
    private OfflinePlayer _player;

    public HorseFriendRemoveEvent(CustomHorse horse, OfflinePlayer player) {
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
