package com.equestriworlds.grooming;

import com.equestriworlds.Main;
import com.equestriworlds.MiniPlugin;
import com.equestriworlds.grooming.Objects.CrosstiedHorse;
import com.equestriworlds.grooming.Objects.CrosstiedHorseToken;
import com.equestriworlds.horse.HorseManager;
import com.equestriworlds.horse.config.CustomHorse;
import com.equestriworlds.horse.config.HorseConfig;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftAbstractHorse;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftEntity;
import org.bukkit.entity.AbstractHorse;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Hanging;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.hanging.HangingBreakEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class CrosstiesManager
extends MiniPlugin {
    private HashMap<CustomHorse, CrosstiedHorse> horses = new HashMap();

    public CrosstiesManager(JavaPlugin plugin) {
        super("Crossties", plugin);
    }

    @EventHandler
    public void onLeashHorse(PlayerInteractEntityEvent event) {
        if (!(event.getRightClicked() instanceof AbstractHorse)) {
            return;
        }
        AbstractHorse horse = (AbstractHorse)event.getRightClicked();
        if (!horse.isLeashed()) {
            return;
        }
        CustomHorse customHorse = ((Main)Main.getPlugin(Main.class))._horseManager.config.getHorseByHorse((CraftEntity)horse);
        if (horse == null) {
            return;
        }
        if (!event.getPlayer().getInventory().getItemInMainHand().getType().equals((Object)Material.LEASH)) {
            return;
        }
        if (!this.horses.containsKey(customHorse)) {
            this.horses.put(customHorse, new CrosstiedHorse(customHorse));
        }
        CrosstiedHorse crosstiedHorse = this.horses.get(customHorse);
        if (horse.getLeashHolder().getType() == EntityType.LEASH_HITCH) {
            event.setCancelled(true);
            if (!crosstiedHorse.token.isCrosstied) {
                crosstiedHorse.token.isCrosstied = true;
                horse.setCollidable(false);
                horse.setAI(false);
                AbstractHorse tempHorse = (AbstractHorse)horse.getLocation().getWorld().spawnEntity(horse.getLocation(), horse.getType());
                tempHorse.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 10000000, 2));
                tempHorse.setCollidable(false);
                tempHorse.setAI(false);
                tempHorse.teleport((Entity)horse);
                if (!tempHorse.isAdult()) {
                    tempHorse.setAdult();
                }
                tempHorse.setLeashHolder((Entity)event.getPlayer());
                crosstiedHorse.token.invisibleHorse = tempHorse;
            }
        }
    }

    @EventHandler
    public void onUnleashEvent(HangingBreakEvent event) {
        List<Entity> entities = event.getEntity().getNearbyEntities(10.0, 10.0, 10.0);
        Entity entity = null;
        for (Entity entity1 : entities) {
            if (!(entity1 instanceof AbstractHorse)) continue;
            entity = entity1;
        }
        if (!(entity instanceof AbstractHorse)) {
            return;
        }
        if (entity == null) {
            return;
        }
        AbstractHorse horse = (AbstractHorse)entity;
        CustomHorse customHorse = ((Main)Main.getPlugin(Main.class))._horseManager.config.getHorseByHorse((CraftEntity)horse);
        CrosstiedHorse crosstiedHorse = this.horses.get(customHorse);
        if (crosstiedHorse == null) {
            for (CrosstiedHorse ctH : this.horses.values()) {
                if (!horse.equals((Object)ctH.token.invisibleHorse)) continue;
                ctH.token.isCrosstied = false;
                if (ctH.token.invisibleHorse != null) {
                    ctH.token.invisibleHorse.remove();
                }
                if (ctH.horse.horse == null) {
                    return;
                }
                ctH.horse.horse.setCollidable(true);
                ctH.horse.horse.setAI(true);
                ctH.horse.horse.setLeashHolder(null);
                return;
            }
        }
        if (!crosstiedHorse.token.isCrosstied) {
            return;
        }
        crosstiedHorse.token.isCrosstied = false;
        if (crosstiedHorse.token.invisibleHorse != null) {
            crosstiedHorse.token.invisibleHorse.remove();
        }
        if (crosstiedHorse.horse.horse == null) {
            return;
        }
        horse.setCollidable(true);
        horse.setAI(true);
    }
}
