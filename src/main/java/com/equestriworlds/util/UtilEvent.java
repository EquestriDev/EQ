/*
 * Decompiled with CFR 0_133.
 * 
 * Could not load the following classes:
 *  org.bukkit.entity.Entity
 *  org.bukkit.entity.LivingEntity
 *  org.bukkit.entity.Projectile
 *  org.bukkit.event.block.Action
 *  org.bukkit.event.entity.EntityDamageByEntityEvent
 *  org.bukkit.event.entity.EntityDamageEvent
 *  org.bukkit.event.player.PlayerInteractEvent
 *  org.bukkit.projectiles.ProjectileSource
 */
package com.equestriworlds.util;

import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Projectile;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.projectiles.ProjectileSource;

public class UtilEvent {
    public static boolean isAction(PlayerInteractEvent event, ActionType action) {
        if (action == ActionType.L) {
            return event.getAction() == Action.LEFT_CLICK_AIR || event.getAction() == Action.LEFT_CLICK_BLOCK;
        }
        if (action == ActionType.L_AIR) {
            return event.getAction() == Action.LEFT_CLICK_AIR;
        }
        if (action == ActionType.L_BLOCK) {
            return event.getAction() == Action.LEFT_CLICK_BLOCK;
        }
        if (action == ActionType.R) {
            return event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK;
        }
        if (action == ActionType.R_AIR) {
            return event.getAction() == Action.RIGHT_CLICK_AIR;
        }
        if (action == ActionType.R_BLOCK) {
            return event.getAction() == Action.RIGHT_CLICK_BLOCK;
        }
        return false;
    }

    public static Entity GetDamagerEntity(EntityDamageEvent event, boolean ranged) {
        if (!(event instanceof EntityDamageByEntityEvent)) {
            return null;
        }
        EntityDamageByEntityEvent eventEE = (EntityDamageByEntityEvent)event;
        if (!(eventEE.getDamager() instanceof Projectile)) {
            return eventEE.getDamager();
        }
        if (!ranged) {
            return null;
        }
        if (!(eventEE.getDamager() instanceof Projectile)) {
            return null;
        }
        Projectile projectile = (Projectile)eventEE.getDamager();
        if (projectile.getShooter() == null) {
            return null;
        }
        if (!(projectile.getShooter() instanceof LivingEntity)) {
            return null;
        }
        return (LivingEntity)projectile.getShooter();
    }

    public static enum ActionType {
        L,
        L_AIR,
        L_BLOCK,
        R,
        R_AIR,
        R_BLOCK;
        

        private ActionType() {
        }
    }

}
