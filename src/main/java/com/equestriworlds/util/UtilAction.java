/*
 * Decompiled with CFR 0_133.
 * 
 * Could not load the following classes:
 *  org.bukkit.Location
 *  org.bukkit.Material
 *  org.bukkit.entity.Entity
 *  org.bukkit.entity.Player
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.util.Vector
 */
package com.equestriworlds.util;

import com.equestriworlds.common.Rank;
import com.equestriworlds.util.F;
import com.equestriworlds.util.UtilEnt;
import com.equestriworlds.util.UtilGear;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

public class UtilAction {
    public static void velocity(Entity ent, double str, double yAdd, double yMax, boolean groundBoost) {
        UtilAction.velocity(ent, ent.getLocation().getDirection(), str, false, 0.0, yAdd, yMax, groundBoost);
    }

    public static void velocity(Entity ent, Vector vec, double str, double yAdd, double yMax, boolean groundBoost) {
        UtilAction.velocity(ent, vec, str, false, 0.0, yAdd, yMax, groundBoost);
    }

    public static void velocity(Entity ent, Vector vec, double str, boolean ySet, double yBase, double yAdd, double yMax, boolean groundBoost) {
        if (Double.isNaN(vec.getX()) || Double.isNaN(vec.getY()) || Double.isNaN(vec.getZ()) || vec.length() == 0.0) {
            return;
        }
        if (ySet) {
            vec.setY(yBase);
        }
        vec.normalize();
        vec.multiply(str);
        vec.setY(vec.getY() + yAdd);
        if (vec.getY() > yMax) {
            vec.setY(yMax);
        }
        if (groundBoost && UtilEnt.isGrounded(ent)) {
            vec.setY(vec.getY() + 0.2);
        }
        ent.setFallDistance(0.0f);
        if (ent instanceof Player && UtilGear.isMat(((Player)ent).getItemInHand(), Material.SUGAR) && Rank.Has((Player)ent, Rank.DEV, false)) {
            ent.sendMessage(F.main("Debug", "Apparate Sent: " + vec.length()));
        }
        ent.setVelocity(vec);
    }
}
