/*
 * Decompiled with CFR 0_133.
 * 
 * Could not load the following classes:
 *  net.minecraft.server.v1_12_R1.AttributeInstance
 *  net.minecraft.server.v1_12_R1.EntityHorseAbstract
 *  net.minecraft.server.v1_12_R1.GenericAttributes
 *  net.minecraft.server.v1_12_R1.IAttribute
 *  org.bukkit.Location
 *  org.bukkit.Material
 *  org.bukkit.World
 *  org.bukkit.craftbukkit.v1_12_R1.entity.CraftAbstractHorse
 *  org.bukkit.entity.AbstractHorse
 *  org.bukkit.entity.Entity
 *  org.bukkit.entity.LivingEntity
 *  org.bukkit.entity.Player
 *  org.bukkit.entity.Vehicle
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.player.PlayerInteractEvent
 *  org.bukkit.event.vehicle.VehicleEnterEvent
 *  org.bukkit.event.vehicle.VehicleExitEvent
 *  org.bukkit.inventory.EquipmentSlot
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.inventory.PlayerInventory
 *  org.bukkit.util.Vector
 */
package com.equestriworlds.horse;

import com.equestriworlds.MiniModule;
import com.equestriworlds.common.Rank;
import com.equestriworlds.horse.HorseManager;
import com.equestriworlds.update.UpdateEvent;
import com.equestriworlds.update.UpdateType;
import com.equestriworlds.util.C;
import com.equestriworlds.util.UtilAction;
import com.equestriworlds.util.UtilEvent;
import com.equestriworlds.util.UtilGear;
import com.equestriworlds.util.UtilServer;
import com.equestriworlds.util.UtilTextMiddle;
import java.util.HashMap;
import net.minecraft.server.v1_12_R1.AttributeInstance;
import net.minecraft.server.v1_12_R1.EntityHorseAbstract;
import net.minecraft.server.v1_12_R1.GenericAttributes;
import net.minecraft.server.v1_12_R1.IAttribute;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftAbstractHorse;
import org.bukkit.entity.AbstractHorse;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Vehicle;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.vehicle.VehicleEnterEvent;
import org.bukkit.event.vehicle.VehicleExitEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.util.Vector;

public class HorseGaits
extends MiniModule<HorseManager> {
    private HashMap<Player, Integer> gaits = new HashMap();
    private HashMap<AbstractHorse, Double> originalSpeed = new HashMap();

    HorseGaits(HorseManager plugin) {
        super("Horse Gaits", plugin);
    }

    @EventHandler
    public void interact(PlayerInteractEvent e) {
        Player player = e.getPlayer();
        if (!player.isInsideVehicle()) {
            return;
        }
        if (!(player.getVehicle() instanceof AbstractHorse)) {
            return;
        }
        if (!UtilGear.isMat(player.getItemInHand(), Material.STICK)) {
            return;
        }
        if (e.getHand().equals((Object)EquipmentSlot.OFF_HAND)) {
            return;
        }
        if (!this.gaits.containsKey((Object)player) || !this.originalSpeed.containsKey((Object)player.getVehicle())) {
            this.gaits.put(player, 0);
            this.originalSpeed.put((AbstractHorse)player.getVehicle(), ((CraftAbstractHorse)player.getVehicle()).getHandle().getAttributeInstance(GenericAttributes.MOVEMENT_SPEED).getValue());
        }
        e.setCancelled(true);
        if (UtilEvent.isAction(e, UtilEvent.ActionType.L)) {
            if (this.gaits.get((Object)player) >= 4) {
                return;
            }
            int level = this.gaits.get((Object)player);
            this.gaits.put(player, ++level);
            double speed = this.calculateSpeed((AbstractHorse)player.getVehicle(), level);
            ((CraftAbstractHorse)player.getVehicle()).getHandle().getAttributeInstance(GenericAttributes.MOVEMENT_SPEED).setValue(speed);
            UtilTextMiddle.display(" ", C.cYellow + this.gaitName(level), 5, 40, 1, player);
            return;
        }
        if (UtilEvent.isAction(e, UtilEvent.ActionType.R)) {
            if (this.gaits.get((Object)player) <= 0) {
                return;
            }
            int level = this.gaits.get((Object)player);
            this.gaits.put(player, --level);
            double speed = this.calculateSpeed((AbstractHorse)player.getVehicle(), level);
            ((CraftAbstractHorse)player.getVehicle()).getHandle().getAttributeInstance(GenericAttributes.MOVEMENT_SPEED).setValue(speed);
            UtilTextMiddle.display(" ", C.cYellow + this.gaitName(level), 5, 40, 1, player);
        }
    }

    @EventHandler
    public void hopOnHorse(VehicleEnterEvent e) {
        if (!(e.getEntered() instanceof Player)) {
            return;
        }
        if (!(e.getVehicle() instanceof AbstractHorse)) {
            return;
        }
        this.gaits.put((Player)e.getEntered(), 0);
        this.originalSpeed.put((AbstractHorse)e.getVehicle(), ((CraftAbstractHorse)e.getVehicle()).getHandle().getAttributeInstance(GenericAttributes.MOVEMENT_SPEED).getValue());
        int level = this.gaits.get((Object)e.getEntered());
        double speed = this.calculateSpeed((AbstractHorse)e.getVehicle(), level);
        ((CraftAbstractHorse)e.getVehicle()).getHandle().getAttributeInstance(GenericAttributes.MOVEMENT_SPEED).setValue(speed);
    }

    @EventHandler
    public void hopOffHorse(VehicleExitEvent e) {
        if (!(e.getExited() instanceof Player)) {
            return;
        }
        if (!(e.getVehicle() instanceof AbstractHorse)) {
            return;
        }
        this.gaits.remove((Object)e.getExited());
        ((CraftAbstractHorse)e.getVehicle()).getHandle().getAttributeInstance(GenericAttributes.MOVEMENT_SPEED).setValue(this.originalSpeed.get((Object)e.getVehicle()).doubleValue());
        this.originalSpeed.remove((Object)e.getVehicle());
    }

    private String gaitName(int level) {
        if (level == 0) {
            return "Halt";
        }
        if (level == 1) {
            return "Walk";
        }
        if (level == 2) {
            return "Trot";
        }
        if (level == 3) {
            return "Canter";
        }
        if (level == 4) {
            return "Gallop";
        }
        return "";
    }

    private double calculateSpeed(AbstractHorse horse, int level) {
        double speed = this.originalSpeed.get((Object)horse);
        double percent = 0.1;
        if (level == 0) {
            percent = 0.0;
        } else if (level == 1) {
            percent = 0.25;
        } else if (level == 2) {
            percent = 0.5;
        } else if (level == 3) {
            percent = 0.75;
        } else if (level == 4) {
            percent = 1.0;
        }
        return speed * percent;
    }

    public void update(UpdateEvent e) {
        if (e.getType() != UpdateType.TICK) {
            return;
        }
        for (Player player : UtilServer.getPlayers()) {
            if (!Rank.Has(player, Rank.DEV, false) || !player.getInventory().getItemInOffHand().getType().equals((Object)Material.NETHER_STAR)) continue;
            if (player.isInsideVehicle() && player.getVehicle() instanceof AbstractHorse) {
                UtilAction.velocity(player.getVehicle(), player.getLocation().getDirection(), 0.8, 0.1, 1.0, true);
                Location eloc = player.getVehicle().getLocation();
                Location loc = player.getLocation();
                player.getVehicle().teleport(new Location(eloc.getWorld(), eloc.getX(), eloc.getY(), eloc.getZ(), loc.getYaw(), loc.getPitch()));
                continue;
            }
            UtilAction.velocity((Entity)player, 0.8, 0.1, 1.0, true);
        }
    }
}
