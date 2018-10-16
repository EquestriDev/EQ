package com.equestriworlds.util;

import net.minecraft.server.v1_12_R1.EntityFireworks;
import net.minecraft.server.v1_12_R1.WorldServer;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_12_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftFirework;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.util.Vector;

public class UtilFirework {
    public static void playFirework(Location loc, FireworkEffect fe) {
        Firework firework = (Firework)loc.getWorld().spawnEntity(loc, EntityType.FIREWORK);
        FireworkMeta data = firework.getFireworkMeta();
        data.clearEffects();
        data.setPower(1);
        data.addEffect(fe);
        firework.setFireworkMeta(data);
        ((CraftFirework)firework).getHandle().expectedLifespan = 1;
    }

    public static Firework launchFirework(Location loc, FireworkEffect fe, Vector dir, int power) {
        try {
            Firework fw = (Firework)loc.getWorld().spawn(loc, Firework.class);
            FireworkMeta data = fw.getFireworkMeta();
            data.clearEffects();
            data.setPower(power);
            data.addEffect(fe);
            fw.setFireworkMeta(data);
            if (dir != null) {
                fw.setVelocity(dir);
            }
            return fw;
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public void detonateFirework(Firework firework) {
        ((CraftWorld)firework.getWorld()).getHandle().broadcastEntityEffect(((CraftEntity)firework).getHandle(), (byte)17);
        firework.remove();
    }

    public static Firework launchFirework(Location loc, FireworkEffect.Type type, Color color, boolean flicker, boolean trail, Vector dir, int power) {
        return UtilFirework.launchFirework(loc, FireworkEffect.builder().flicker(flicker).withColor(color).with(type).trail(trail).build(), dir, power);
    }

    public static void playFirework(Location loc, FireworkEffect.Type type, Color color, boolean flicker, boolean trail) {
        UtilFirework.playFirework(loc, FireworkEffect.builder().flicker(flicker).withColor(color).with(type).trail(trail).build());
    }
}
