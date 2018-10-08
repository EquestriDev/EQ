/*
 * Decompiled with CFR 0_133.
 * 
 * Could not load the following classes:
 *  org.bukkit.ChatColor
 *  org.bukkit.GameMode
 *  org.bukkit.Location
 *  org.bukkit.Material
 *  org.bukkit.World
 *  org.bukkit.craftbukkit.v1_12_R1.entity.CraftAbstractHorse
 *  org.bukkit.craftbukkit.v1_12_R1.entity.CraftEntity
 *  org.bukkit.entity.AbstractHorse
 *  org.bukkit.entity.Entity
 *  org.bukkit.entity.HumanEntity
 *  org.bukkit.entity.Item
 *  org.bukkit.entity.Player
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.entity.PlayerDeathEvent
 *  org.bukkit.event.inventory.InventoryClickEvent
 *  org.bukkit.event.inventory.InventoryOpenEvent
 *  org.bukkit.event.player.AsyncPlayerChatEvent
 *  org.bukkit.event.player.AsyncPlayerPreLoginEvent
 *  org.bukkit.event.player.AsyncPlayerPreLoginEvent$Result
 *  org.bukkit.event.player.PlayerChangedWorldEvent
 *  org.bukkit.event.player.PlayerDropItemEvent
 *  org.bukkit.event.player.PlayerGameModeChangeEvent
 *  org.bukkit.event.player.PlayerInteractEntityEvent
 *  org.bukkit.event.player.PlayerInteractEvent
 *  org.bukkit.event.player.PlayerPickupItemEvent
 *  org.bukkit.event.player.PlayerTeleportEvent
 *  org.bukkit.inventory.Inventory
 *  org.bukkit.inventory.ItemStack
 */
package com.equestriworlds.misc;

import com.equestriworlds.MiniModule;
import com.equestriworlds.MiniPlugin;
import com.equestriworlds.command.ICommand;
import com.equestriworlds.common.Rank;
import com.equestriworlds.horse.HorseManager;
import com.equestriworlds.horse.config.CustomHorse;
import com.equestriworlds.horse.config.HorseConfig;
import com.equestriworlds.misc.commands.CheckPlayerTimeCommand;
import com.equestriworlds.misc.commands.PlayerCountdownCommand;
import com.equestriworlds.misc.commands.TestingCommand;
import com.equestriworlds.util.C;
import com.equestriworlds.util.F;
import com.equestriworlds.util.UtilPlayer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftAbstractHorse;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftEntity;
import org.bukkit.entity.AbstractHorse;
import org.bukkit.entity.Entity;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerGameModeChangeEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class MiscManager
extends MiniModule<HorseManager> {
    private ArrayList<Material> disabledItems = new ArrayList();
    private ArrayList<Material> disabledCreative = new ArrayList();

    public MiscManager(HorseManager plugin) {
        super("Misc Manager", plugin);
        plugin.addCommand(new CheckPlayerTimeCommand(plugin));
        plugin.addCommand(new PlayerCountdownCommand(plugin));
        plugin.addCommand(new TestingCommand(plugin));
        this.setDisabled();
    }

    private void setDisabled() {
        this.disabledItems.addAll(Arrays.asList(new Material[]{Material.DIAMOND_BLOCK, Material.GOLD_BLOCK, Material.IRON_BLOCK, Material.LAPIS_BLOCK, Material.DIAMOND_ORE, Material.GOLD_ORE, Material.IRON_ORE, Material.BEDROCK, Material.MOB_SPAWNER, Material.MONSTER_EGG}));
        this.disabledCreative.addAll(Arrays.asList(new Material[]{Material.NETHER_WARTS, Material.MELON_SEEDS, Material.SEEDS, Material.WHEAT, Material.BEETROOT, Material.PUMPKIN_SEEDS, Material.DAYLIGHT_DETECTOR, Material.GLOWSTONE_DUST, Material.APPLE, Material.CARROT, Material.SUGAR, Material.BUCKET, Material.CAULDRON}));
    }

    @EventHandler
    public void chatEvent(AsyncPlayerChatEvent event) {
        event.setFormat(C.convert(C.clear("&3" + event.getPlayer().getName() + " &7\u00bb &f" + event.getMessage())));
    }

    @EventHandler
    public void onInventory(InventoryOpenEvent event) {
        if (!(event.getPlayer() instanceof Player)) {
            return;
        }
        if (Rank.Has((Player)event.getPlayer(), Rank.DEV, false)) {
            return;
        }
        if (event.getInventory().getName().contains("Black Market") && !event.getPlayer().getLocation().getWorld().getName().contains("Survival")) {
            event.getPlayer().sendMessage(C.convert("&3Shop \u00bb &fYou cannot open &3&lBlack Market &fwhile not in &a&lSurvival"));
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player)) {
            return;
        }
        Player player = (Player)event.getWhoClicked();
        if (event.getCurrentItem() == null) {
            return;
        }
        if (this.disabledCreative.contains((Object)event.getCurrentItem().getType()) || this.disabledCreative.contains((Object)event.getCursor().getType())) {
            if (Rank.Has(player, Rank.JRMOD, false)) {
                return;
            }
            if (player.getGameMode() != GameMode.CREATIVE) {
                return;
            }
            UtilPlayer.message((Entity)player, F.main("Creative Manager", (Object)ChatColor.RED + "You can't take these items while in Creative Mode"));
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onGamemodeChange(PlayerGameModeChangeEvent event) {
        Player player = event.getPlayer();
        if (player.getWorld().getName().equalsIgnoreCase("Survival") && !Rank.Has(player, Rank.JRMOD, false) && event.getNewGameMode().equals((Object)GameMode.CREATIVE)) {
            UtilPlayer.message((Entity)player, F.main("Creative Manager", (Object)ChatColor.RED + "You can't change Gamemode in Survival"));
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onGUIOpen(InventoryOpenEvent event) {
        if (!(event.getPlayer() instanceof Player)) {
            return;
        }
        if (!Rank.Has((Player)event.getPlayer(), Rank.TJRMOD, false) && event.getPlayer().getGameMode() == GameMode.CREATIVE) {
            UtilPlayer.message((Entity)event.getPlayer(), F.main("Creative Manager", (Object)ChatColor.RED + "You can't open inventories in Creative"));
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        if (!Rank.Has(event.getPlayer(), Rank.TJRMOD, false) && this.disabledItems.contains((Object)event.getMaterial())) {
            UtilPlayer.message((Entity)event.getPlayer(), F.main("Creative Manager", (Object)ChatColor.RED + "You can't place this block in Creative"));
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onEntityClick(PlayerInteractEntityEvent event) {
        if (!Rank.Has(event.getPlayer(), Rank.TJRMOD, false) && event.getPlayer().getGameMode() == GameMode.CREATIVE) {
            UtilPlayer.message((Entity)event.getPlayer(), F.main("Creative Manager", (Object)ChatColor.RED + "You can't interact with entities while in Creative"));
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onDrop(PlayerDropItemEvent event) {
        if (!Rank.Has(event.getPlayer(), Rank.JRMOD, false) && event.getPlayer().getGameMode() == GameMode.CREATIVE) {
            UtilPlayer.message((Entity)event.getPlayer(), F.main("Creative Manager", (Object)ChatColor.RED + "You can't drop items in Creative"));
            if (this.disabledCreative.contains((Object)event.getItemDrop().getItemStack().getType())) {
                event.getItemDrop().remove();
                return;
            }
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPickup(PlayerPickupItemEvent event) {
        if (!Rank.Has(event.getPlayer(), Rank.JRMOD, false) && event.getPlayer().getGameMode() == GameMode.CREATIVE) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onWorldChange(PlayerChangedWorldEvent event) {
        if (event.getPlayer().getWorld().getName().equalsIgnoreCase("Survival") && event.getPlayer().getGameMode().equals((Object)GameMode.CREATIVE) && !Rank.Has(event.getPlayer(), Rank.JRMOD, false)) {
            event.getPlayer().setGameMode(GameMode.SURVIVAL);
        }
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        if (event.getEntity().getGameMode().equals((Object)GameMode.CREATIVE)) {
            event.getDrops().clear();
            UtilPlayer.message((Entity)event.getEntity().getPlayer(), F.main("Creative Manager", (Object)ChatColor.RED + "Your inventory has been cleared as you died in creative!"));
        }
    }

    @EventHandler
    public void onJoin(AsyncPlayerPreLoginEvent event) {
        if (event.getName().equalsIgnoreCase("Dithuzad") || event.getName().equalsIgnoreCase("kingofthelandfil")) {
            event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_FULL, "Failed to login: The authentication servers are currently down for maintenance");
        }
    }

    @EventHandler
    public void playerTP(PlayerTeleportEvent event) {
        if (event.getPlayer().getVehicle() == null) {
            return;
        }
        Player player = event.getPlayer();
        if (!(player.getVehicle() instanceof AbstractHorse)) {
            return;
        }
        AbstractHorse horse = (AbstractHorse)player.getVehicle();
        CustomHorse customHorse = ((HorseManager)this.Plugin).config.getHorseByHorse((CraftEntity)horse);
        if (customHorse == null) {
            return;
        }
        ((HorseManager)this.Plugin).runSyncLater(() -> {
            Location loc = player.getLocation();
            if (player.isInsideVehicle()) {
                return;
            }
            horse.setFallDistance(-2000000.0f);
            loc.setY(loc.getY() + 0.5);
            customHorse.remove(false);
            customHorse.spawn(loc);
            ((HorseManager)this.Plugin).runSyncLater(() -> customHorse.horse.setPassenger((Entity)player), 5L);
        }, 2L);
    }
}
