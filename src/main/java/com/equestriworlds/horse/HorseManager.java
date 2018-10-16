package com.equestriworlds.horse;

import com.equestriworlds.MiniPlugin;
import com.equestriworlds.account.CoreClientManager;
import com.equestriworlds.command.ICommand;
import com.equestriworlds.horse.CowPathfinders;
import com.equestriworlds.horse.HorseGaits;
import com.equestriworlds.horse.branding.Brand;
import com.equestriworlds.horse.branding.BrandConfig;
import com.equestriworlds.horse.branding.BrandToken;
import com.equestriworlds.horse.breeding.HorseBreeding;
import com.equestriworlds.horse.breeding.breedingStages;
import com.equestriworlds.horse.command.HorseCommand;
import com.equestriworlds.horse.command.admin.HorseAdminCommand;
import com.equestriworlds.horse.command.vet.HorseVetCommand;
import com.equestriworlds.horse.config.CustomHorse;
import com.equestriworlds.horse.config.CustomHorseToken;
import com.equestriworlds.horse.config.Gender;
import com.equestriworlds.horse.config.HorseAccess;
import com.equestriworlds.horse.config.HorseConfig;
import com.equestriworlds.horse.event.HorseFriendRemoveEvent;
import com.equestriworlds.horse.lunging.LungingManager;
import com.equestriworlds.misc.EquestriWorldsHooks;
import com.equestriworlds.misc.MiscManager;
import com.equestriworlds.update.UpdateEvent;
import com.equestriworlds.update.UpdateType;
import com.equestriworlds.util.C;
import com.equestriworlds.util.F;
import com.equestriworlds.util.UtilMath;
import com.equestriworlds.util.UtilPlayer;
import com.equestriworlds.util.UtilServer;
import com.equestriworlds.util.UtilTime;
import java.io.PrintStream;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;
import net.md_5.bungee.api.ChatColor;
import net.milkbowl.vault.economy.Economy;
import net.minecraft.server.v1_12_R1.EntityHorseAbstract;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftAbstractHorse;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftEntity;
import org.bukkit.entity.AbstractHorse;
import org.bukkit.entity.AnimalTamer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Horse;
import org.bukkit.entity.Llama;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.event.world.ChunkUnloadEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.ServicesManager;
import org.bukkit.plugin.java.JavaPlugin;

public class HorseManager
extends MiniPlugin {
    public CoreClientManager clientManager;
    public HorseConfig config;
    public BrandConfig brandConfig;
    public HashSet<Player> bypass = new HashSet();
    public HashSet<Player> removeMode = new HashSet();
    public HashSet<Player> info = new HashSet();
    public HashSet<Player> condition = new HashSet();
    public HashMap<Entity, String[]> newGender = new HashMap();
    public Economy eco;
    private long l = 0L;

    public HorseManager(JavaPlugin plugin, CoreClientManager clientManager) {
        super("Horse Manager", plugin);
        this.clientManager = clientManager;
        for (World world : UtilServer.getServer().getWorlds()) {
            for (Entity entity : world.getEntities()) {
                if (!(entity instanceof AbstractHorse) || entity.hasMetadata("NPC")) continue;
                entity.remove();
            }
        }
        this.config = new HorseConfig();
        this.addCommand(new HorseCommand(this));
        this.addCommand(new HorseAdminCommand(this));
        this.addCommand(new HorseVetCommand(this));
        new HorseGaits(this);
        new HorseBreeding(this);
        new LungingManager(this);
        new MiscManager(this);
        this.setupEconomy();
        this.brandConfig = new BrandConfig();
        if (Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) {
            new EquestriWorldsHooks(this).hook();
        }
        new CowPathfinders(this);
        this.runSyncLater(() -> {
            for (CustomHorse horse : this.config.horses.values()) {
                if (horse.token.lastKnown == null || !horse.token.lastKnown.getChunk().isLoaded()) continue;
                horse.spawn(horse.token.lastKnown);
                horse.token.lastKnown = null;
            }
        }, 20L);
    }

    private void setupEconomy() {
        if (this.getPlugin().getServer().getPluginManager().getPlugin("Vault") == null) {
            return;
        }
        RegisteredServiceProvider rsp = this.getPlugin().getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return;
        }
        this.eco = (Economy)rsp.getProvider();
    }

    public void claim(Player player, AbstractHorse horse, String id) {
        horse.setOwner((AnimalTamer)player);
        CustomHorse customHorse = this.config.getHorseByHorse((CraftEntity)horse);
        if (customHorse == null) {
            horse.setCustomName(id);
            horse.setCustomNameVisible(true);
            customHorse = new CustomHorse(id, horse);
            this.config.horses.put(id, customHorse);
            if (this.newGender.containsKey((Object)horse)) {
                String[] list = this.newGender.get((Object)horse);
                customHorse.changeGender(Gender.valueOf(list[0].toUpperCase()));
                customHorse.setSpeed(Double.parseDouble(list[1]));
                customHorse.setJump(Double.parseDouble(list[2]));
                this.newGender.remove((Object)horse);
            }
        } else {
            customHorse.token.free = false;
            customHorse.token.owner = player.getUniqueId();
        }
    }

    public void claimBrand(UUID id, String brand) {
        Brand customBrand = this.brandConfig.getBrandByID(id);
        if (customBrand == null) {
            customBrand = new Brand(id, brand);
            this.brandConfig.brands.put(id, customBrand);
        }
    }

    @EventHandler
    public void save(UpdateEvent e) {
        if (e.getType() != UpdateType.MIN_05) {
            return;
        }
        for (CustomHorse horse : this.config.horses.values()) {
            horse.updateInventory();
        }
        System.out.println("EquestriWorlds Saving process is starting...");
        this.l = System.currentTimeMillis();
        this.runAsync(() -> {
            this.config.save();
            this.brandConfig.save();
            System.out.println("EquestriWorlds Saving process has finished in: " + UtilTime.convertString(System.currentTimeMillis() - this.l, 1, UtilTime.TimeUnit.FIT));
        });
    }

    @EventHandler
    public void cancelDamage(EntityDamageEvent e) {
        CustomHorse horse;
        if (e.getEntity() instanceof AbstractHorse && (horse = this.config.getHorseByHorse((CraftEntity)e.getEntity())) != null) {
            e.setCancelled(true);
        }
        if (e.getEntity() instanceof Player && e.getEntity().isInsideVehicle() && e.getEntity().getVehicle() instanceof AbstractHorse && e.getCause().equals((Object)EntityDamageEvent.DamageCause.FALL)) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void remove(EntityDamageByEntityEvent e) {
        Player caller;
        if (!(e.getEntity() instanceof AbstractHorse)) {
            return;
        }
        if (!(e.getDamager() instanceof Player)) {
            return;
        }
        if (this.removeMode.contains((Object)e.getDamager()) && ((Player)e.getDamager()).isSneaking()) {
            CustomHorse horse = this.config.getHorseByHorse((CraftEntity)e.getEntity());
            if (horse != null && horse.alive()) {
                horse.token.lastKnown = null;
            }
            e.getEntity().remove();
        }
        if (this.info.contains((Object)e.getDamager())) {
            if (!((Player)e.getDamager()).isSneaking()) {
                caller = (Player)e.getDamager();
                CustomHorse horse = this.config.getHorseByHorse((CraftEntity)e.getEntity());
                if (horse == null) {
                    UtilPlayer.message((Entity)caller, F.main(this.getName(), "This horse is not claimed."));
                    return;
                }
                UtilPlayer.message((Entity)caller, ChatColor.translateAlternateColorCodes((char)'&', (String)"&7&m&l\u2055--------------------------&7&l\u2055"));
                UtilPlayer.message((Entity)caller, F.main(this.getName(), "Information about the horse."));
                UtilPlayer.message((Entity)caller, "");
                UtilPlayer.message((Entity)caller, F.desc("ID", horse.id + ""));
                UtilPlayer.message((Entity)caller, F.desc("Name", horse.token.name));
                UtilPlayer.message((Entity)caller, F.desc("Owner", UtilServer.getServer().getOfflinePlayer(horse.token.owner).getName()));
                if (horse.token.brand != null) {
                    UtilPlayer.message((Entity)caller, F.desc("Brand", C.convert(horse.token.brand.token.format)));
                }
                UtilPlayer.message((Entity)caller, F.desc("Gender", horse.token.gender.name));
                if (horse.token.age != null) {
                    UtilPlayer.message((Entity)caller, F.desc("Age", UtilTime.formatDateDiff(horse.token.age, false)));
                }
                UtilPlayer.message((Entity)caller, "");
                UtilPlayer.message((Entity)caller, F.desc("Speed", UtilMath.trim(3, horse.token.speed) + ""));
                UtilPlayer.message((Entity)caller, F.desc("Jump", UtilMath.trim(3, horse.token.jump) + ""));
                UtilPlayer.message((Entity)caller, "");
                if (horse.horse instanceof Horse) {
                    UtilPlayer.message((Entity)caller, F.desc("Color", horse.token.color.toString()));
                    UtilPlayer.message((Entity)caller, F.desc("Style", horse.token.style.toString()));
                }
                if (horse.horse instanceof Llama) {
                    UtilPlayer.message((Entity)caller, F.desc("Color", horse.token.llamaColor.toString()));
                }
                UtilPlayer.message((Entity)caller, F.desc("Adult", F.yn(horse.token.adult)));
                if (horse.token.stage != null && !horse.token.stage.equals((Object)breedingStages.NONE)) {
                    UtilPlayer.message((Entity)caller, F.desc(horse.token.stage.name, UtilTime.formatDateDiff(horse.token.breedingTime, true)));
                }
                UtilPlayer.message((Entity)caller, ChatColor.translateAlternateColorCodes((char)'&', (String)"&7&m&l\u2055--------------------------&7&l\u2055"));
                e.setCancelled(true);
            }
        } else if (this.condition.contains((Object)e.getDamager())) {
            caller = (Player)e.getDamager();
            if (caller.isSneaking()) {
                return;
            }
            CustomHorse horse = this.config.getHorseByHorse((CraftEntity)e.getEntity());
            UtilPlayer.message((Entity)caller, ChatColor.translateAlternateColorCodes((char)'&', (String)"&7&m&l\u2055--------------------------&7&l\u2055"));
            UtilPlayer.message((Entity)caller, F.main(this.getName(), "Information about the horse's condition."));
            UtilPlayer.message((Entity)caller, F.desc("ID", horse.id + ""));
            UtilPlayer.message((Entity)caller, F.desc("Name", horse.token.name));
            UtilPlayer.message((Entity)caller, F.desc("Appearance", this.getProgressBar(horse.token.appearance, 10, 10, "\u00bb", "&3&l", "&f&l")));
            UtilPlayer.message((Entity)caller, ChatColor.translateAlternateColorCodes((char)'&', (String)"&7&m&l\u2055--------------------------&7&l\u2055"));
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void mountHorse(PlayerInteractEntityEvent e) {
        if (!(e.getRightClicked() instanceof AbstractHorse)) {
            return;
        }
        if (e.getRightClicked().hasMetadata("NPC")) {
            e.setCancelled(true);
            return;
        }
        if (e.getPlayer().getInventory().getItemInMainHand() != null && e.getPlayer().getInventory().getItemInMainHand().getType() == Material.LEASH) {
            return;
        }
        if (e.getPlayer().getInventory().getItemInMainHand() != null && e.getPlayer().getInventory().getItemInMainHand().getType() == Material.GOLDEN_APPLE) {
            return;
        }
        CustomHorse horse = this.config.getHorseByHorse((CraftEntity)e.getRightClicked());
        Player player = e.getPlayer();
        if (horse == null) {
            UtilPlayer.message((Entity)player, F.main(this.getName(), "This horse is not claimed."));
            e.setCancelled(true);
            return;
        }
        if (this.bypass.contains((Object)player)) {
            return;
        }
        if (horse.token.owner != null) {
            if (horse.token.stage != null) {
                if (horse.token.stage.equals((Object)breedingStages.FOAL) || horse.token.stage.equals((Object)breedingStages.FOALSTAGE2)) {
                    UtilPlayer.message((Entity)player, F.main(this.getName(), "That horse is too young to ride."));
                    e.setCancelled(true);
                    return;
                }
                if (System.currentTimeMillis() > horse.token.breedingTime - 432000000L && horse.token.stage.equals((Object)breedingStages.PREGNANT)) {
                    UtilPlayer.message((Entity)player, F.main(this.getName(), "This horse is currently pregnant! You're unable to ride."));
                    e.setCancelled(true);
                    return;
                }
                if (System.currentTimeMillis() < horse.token.breedingTime && horse.token.stage.equals((Object)breedingStages.ABORT)) {
                    UtilPlayer.message((Entity)player, F.main(this.getName(), "This horse is currently recovering! You're unable to ride."));
                    e.setCancelled(true);
                    return;
                }
            }
            if (!horse.token.owner.equals(player.getUniqueId())) {
                if (horse.token.sale) {
                    UtilPlayer.message((Entity)player, F.main(this.getName(), "This horse it for sale."));
                    e.setCancelled(true);
                } else if (horse.token.access.equals((Object)HorseAccess.NOBODY)) {
                    UtilPlayer.message((Entity)player, F.main(this.getName(), "This horse can only be accessed by the owner."));
                    e.setCancelled(true);
                } else if (horse.token.access.equals((Object)HorseAccess.FRIENDS) && !horse.token.friends.contains(player.getUniqueId()) && !horse.token.friends.contains(player.getUniqueId()) && !horse.token.trusted.contains(player.getUniqueId())) {
                    UtilPlayer.message((Entity)player, F.main(this.getName(), "This horse can only be accessed by friends or trusted."));
                    e.setCancelled(true);
                }
            }
            if (horse.token.stage != null) {
                if (horse.token.stage.equals((Object)breedingStages.FOAL) || horse.token.stage.equals((Object)breedingStages.FOALSTAGE2)) {
                    UtilPlayer.message((Entity)player, F.main(this.getName(), "That horse is too young to ride."));
                    e.setCancelled(true);
                } else if (System.currentTimeMillis() > horse.token.breedingTime - 432000000L && horse.token.stage.equals((Object)breedingStages.PREGNANT)) {
                    UtilPlayer.message((Entity)player, F.main(this.getName(), "This horse is currently pregnant! You're unable to ride."));
                    e.setCancelled(true);
                } else if (System.currentTimeMillis() < horse.token.breedingTime && horse.token.stage.equals((Object)breedingStages.ABORT)) {
                    UtilPlayer.message((Entity)player, F.main(this.getName(), "This horse is currently recovering! You're unable to ride."));
                    e.setCancelled(true);
                }
            }
        } else if (horse.token.sale) {
            UtilPlayer.message((Entity)player, F.main(this.getName(), "This horse is in Sale mode."));
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void removeEvent(HorseFriendRemoveEvent e) {
        if (e.GetPlayer() == null || !e.GetPlayer().isOnline()) {
            return;
        }
        CustomHorse horse = e.GetCustomHorse();
        Player player = e.GetPlayer().getPlayer();
        if (horse.horse.isLeashed() && horse.horse.getLeashHolder().equals((Object)player)) {
            horse.horse.setLeashHolder(null);
        }
        if (horse.horse.getPassenger().equals((Object)player)) {
            horse.horse.getPassenger().eject();
        }
    }

    @Override
    public void disable() {
        for (CustomHorse horse : this.config.horses.values()) {
            if (!horse.alive()) continue;
            horse.remove();
        }
        for (World world : UtilServer.getServer().getWorlds()) {
            if (world.getName().equals("Survival")) continue;
            for (Entity entity : world.getEntities()) {
                if (!(entity instanceof AbstractHorse) || entity.hasMetadata("NPC")) continue;
                entity.remove();
            }
        }
        this.config.save();
    }

    @EventHandler
    public void quit(PlayerQuitEvent e) {
        if (this.removeMode.contains((Object)e.getPlayer())) {
            this.removeMode.remove((Object)e.getPlayer());
        }
        if (e.getPlayer().isInsideVehicle()) {
            e.getPlayer().leaveVehicle();
        }
    }

    @EventHandler
    public void worldChange(PlayerChangedWorldEvent e) {
        this.runAsync(() -> {
            for (CustomHorse horse : this.config.horses.values()) {
                if (!e.getPlayer().getUniqueId().equals(horse.token.owner) || !horse.follow) continue;
                horse.follow = false;
                UtilPlayer.message((Entity)e.getPlayer(), F.main(this.getName(), F.elem(horse.token.name) + " will " + (horse.follow ? "now" : "no longer") + " follow you."));
            }
        });
    }

    @EventHandler
    public void chunkUnload(ChunkUnloadEvent e) {
        Chunk chunk = e.getChunk();
        if (chunk.getWorld().getName().equals("Survival")) return;
        for (Entity ent : chunk.getEntities()) {
            CustomHorse horse;
            CraftAbstractHorse cHorse;
            if (!(ent instanceof AbstractHorse) || (horse = this.config.getHorseByHorse((CraftEntity)ent)) == null || (cHorse = horse.horse) == null || !cHorse.getHandle().isAlive()) continue;
            horse.remove();
        }
    }

    @EventHandler
    public void chunkLoad(ChunkLoadEvent e) {
        Chunk chunk = e.getChunk();
        if (chunk.getWorld().getName().equals("Survival")) return;
        for (CustomHorse horse : this.config.horses.values()) {
            if (horse.token.lastKnown == null) continue;
            int x = horse.token.lastKnown.getBlockX() >> 4;
            int z = horse.token.lastKnown.getBlockZ() >> 4;
            if (!horse.token.lastKnown.getWorld().getName().equals(chunk.getWorld().getName())
                || x != chunk.getX() || z != chunk.getZ()) continue;
            horse.spawn(horse.token.lastKnown);
        }
    }

    public boolean horseExists(String id) {
        CustomHorse horse = this.config.getHorseById(id);
        return horse != null;
    }

    private String getProgressBar(int current, int max, int totalBars, String symbol, String completedColor, String notCompletedColor) {
        int i;
        float percent = (float)current / (float)max;
        int progressBars = (int)((float)totalBars * percent);
        int leftOver = totalBars - progressBars;
        StringBuilder sb = new StringBuilder();
        sb.append(completedColor);
        for (i = 0; i < progressBars; ++i) {
            sb.append(symbol);
        }
        sb.append(notCompletedColor);
        for (i = 0; i < leftOver; ++i) {
            sb.append(symbol);
        }
        return C.convert(sb.toString());
    }
}
