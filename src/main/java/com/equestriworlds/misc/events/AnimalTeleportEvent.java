package com.equestriworlds.misc.events;

import com.equestriworlds.horse.config.CustomHorse;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftAbstractHorse;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public final class AnimalTeleportEvent
extends Event
implements Cancellable {
    private static final HandlerList handlers = new HandlerList();
    private CustomHorse entity;
    private Player rider;
    private Location loc;
    private Location cLoc;
    private boolean cancelled;

    public AnimalTeleportEvent(CustomHorse entity, Player rider) {
        this(entity, rider, rider.getLocation());
    }

    private AnimalTeleportEvent(CustomHorse entity, Player rider, Location destination) {
        this.entity = entity;
        this.rider = rider;
        this.loc = destination;
        this.cLoc = entity.horse.getLocation();
    }

    public Player getRider() {
        return this.rider;
    }

    public CustomHorse getEntity() {
        return this.entity;
    }

    public Location getDestination() {
        return this.loc;
    }

    public Location getFrom() {
        return this.cLoc;
    }

    public boolean isCancelled() {
        return this.cancelled;
    }

    public void setCancelled(boolean cancel) {
        this.cancelled = cancel;
    }

    public void setCancelled() {
        this.setCancelled(true);
    }

    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
