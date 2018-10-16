package com.equestriworlds.grooming;

import com.equestriworlds.Main;
import com.equestriworlds.MiniPlugin;
import com.equestriworlds.grooming.Objects.GroomedHorse;
import com.equestriworlds.grooming.Objects.GroomedHorseToken;
import com.equestriworlds.horse.HorseManager;
import com.equestriworlds.horse.config.CustomHorse;
import com.equestriworlds.horse.config.CustomHorseToken;
import com.equestriworlds.horse.config.HorseConfig;
import com.equestriworlds.update.UpdateEvent;
import com.equestriworlds.update.UpdateType;
import com.equestriworlds.util.C;
import com.equestriworlds.util.F;
import com.equestriworlds.util.UtilPlayer;
import com.equestriworlds.util.UtilTime;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.minecraft.server.v1_12_R1.NBTTagCompound;
import org.apache.commons.io.FileUtils;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftItemStack;
import org.bukkit.entity.AbstractHorse;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

/**
 * Grooming event handlers.
 * Texture Pack, Cooldowns!
 */
public class GroomingManager
extends MiniPlugin {
    private int count = 0;
    private HashMap<CustomHorse, GroomedHorse> horses = new HashMap();

    public GroomingManager(JavaPlugin plugin) {
        super("Grooming", plugin);
        YamlConfiguration config;
        File langFile = new File(((Main)Main.getPlugin(Main.class)).getDataFolder(), "config.yml");
        if (!langFile.exists()) {
            try {
                langFile.createNewFile();
                FileUtils.copyInputStreamToFile((InputStream)((Main)Main.getPlugin(Main.class)).getResource("config.yml"), (File)langFile);
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (!(config = YamlConfiguration.loadConfiguration((File)langFile)).isSet("count")) {
            config.set("count", (Object)0);
        }
        try {
            config.save(langFile);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        this.count = config.getInt("count");
    }

    @EventHandler
    public void join(PlayerJoinEvent e) {
        this.runAsyncLater(() -> {
            Player player = e.getPlayer();
            UtilPlayer.message((Entity)player, ChatColor.translateAlternateColorCodes((char)'&', (String)"&7&m&l\u2055--------------------------&7&l\u2055"));
            UtilPlayer.message((Entity)player, C.cDAqua + "Would you like to install the grooming texturepack? ");
            UtilPlayer.message((Entity)player, C.Reset + C.cGray + "(HINT: Open the chat to click on the options)");
            UtilPlayer.message((Entity)player, " ");
            TextComponent line = new TextComponent("              ");
            TextComponent yes = new TextComponent("Download - Yes");
            yes.setColor(ChatColor.AQUA);
            yes.setBold(Boolean.valueOf(true));
            yes.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/rp GROOMING"));
            yes.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("Click here to download the texturepack!").create()));
            line.addExtra((BaseComponent)yes);
            line.addExtra("                    ");
            e.getPlayer().spigot().sendMessage((BaseComponent)line);
            UtilPlayer.message((Entity)player, " ");
            UtilPlayer.message((Entity)player, ChatColor.translateAlternateColorCodes((char)'&', (String)"&7&m&l\u2055--------------------------&7&l\u2055"));
            player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 9999.0f, 0.0f);
        }, 100L);
    }

    @EventHandler
    public void save(UpdateEvent e) {
        if (e.getType() != UpdateType.MIN_05) {
            return;
        }
        File langFile = new File(((Main)Main.getPlugin(Main.class)).getDataFolder(), "config.yml");
        if (!langFile.exists()) {
            try {
                langFile.createNewFile();
                FileUtils.copyInputStreamToFile((InputStream)((Main)Main.getPlugin(Main.class)).getResource("config.yml"), (File)langFile);
            }
            catch (IOException error) {
                error.printStackTrace();
            }
        }
        YamlConfiguration config = YamlConfiguration.loadConfiguration((File)langFile);
        ++this.count;
        if (this.count == 24) {
            this.runAsync(() -> {
                for (CustomHorse horse : ((Main)Main.getPlugin(Main.class))._horseManager.config.horses.values()) {
                    --horse.token.appearance;
                    if (horse.token.appearance >= 0) continue;
                    horse.token.appearance = 0;
                }
            });
            this.count = 0;
        }
        config.set("count", (Object)this.count);
        try {
            config.save(langFile);
        }
        catch (IOException e1) {
            e1.printStackTrace();
        }
    }

    @EventHandler
    public void onClick(PlayerInteractEntityEvent event) {
        if (!(event.getRightClicked() instanceof AbstractHorse)) {
            return;
        }
        CustomHorse horse = ((Main)Main.getPlugin(Main.class))._horseManager.config.getHorseByHorse((CraftEntity)event.getRightClicked());
        if (horse == null) {
            return;
        }
        Player player = event.getPlayer();
        if (player.getInventory().getItemInMainHand().getType() == Material.AIR) {
            return;
        }
        if (player.getInventory().getItemInMainHand().getType() == Material.LEASH) {
            return;
        }
        if (!this.isGroomingItem(CraftItemStack.asNMSCopy((ItemStack)player.getInventory().getItemInMainHand())) && player.getInventory().getItemInMainHand().getType() != Material.WATER_BUCKET) {
            return;
        }
        if (!(horse.token.owner.equals(player.getUniqueId()) || horse.token.trusted.contains(player.getUniqueId()) || horse.token.friends.contains(player.getUniqueId()))) {
            UtilPlayer.message((Entity)player, F.main("Grooming", "This horse does not belong to you"));
            return;
        }
        Location location = player.getLocation();
        event.setCancelled(true);
        player.teleport(location);
        ItemStack item = player.getInventory().getItemInMainHand();
        if (item.getType() != Material.WATER_BUCKET) {
            if (item.getItemMeta() == null || item.getItemMeta().getLore() == null) {
                return;
            }
            int uses = Integer.parseInt(org.bukkit.ChatColor.stripColor((String)((String)item.getItemMeta().getLore().get(0)).replace("Uses: ", "")));
            if (uses == 1) {
                player.getInventory().remove(player.getInventory().getItemInMainHand());
                return;
            }
            ItemMeta itemMeta = item.getItemMeta();
            itemMeta.setLore(Collections.singletonList((Object)org.bukkit.ChatColor.DARK_AQUA + "Uses: " + (Object)org.bukkit.ChatColor.AQUA + (uses - 1)));
            item.setItemMeta(itemMeta);
            player.updateInventory();
        }
        if (!this.horses.containsKey(horse)) {
            this.horses.put(horse, new GroomedHorse(horse));
        }
        GroomedHorse groomedHorse = this.horses.get(horse);
        switch (item.getType()) {
            case SHEARS: {
                if (System.currentTimeMillis() <= groomedHorse.token.shearTime) {
                    UtilPlayer.message((Entity)player, F.main("Grooming", horse.token.name + (Object)org.bukkit.ChatColor.GRAY + "'s clipping cooldown is active " + (Object)org.bukkit.ChatColor.AQUA + (Object)org.bukkit.ChatColor.BOLD + UtilTime.formatDateDiff(groomedHorse.token.shearTime, true)));
                    return;
                }
                horse.addAppearance(2);
                UtilPlayer.message((Entity)player, F.main("Grooming", "You have clipped " + horse.token.name));
                this.addCooldown(groomedHorse, item);
                break;
            }
            case BLAZE_ROD: {
                if (System.currentTimeMillis() <= groomedHorse.token.brushTime) {
                    UtilPlayer.message((Entity)player, F.main("Grooming", horse.token.name + (Object)org.bukkit.ChatColor.GRAY + "'s brushing cooldown is active " + (Object)org.bukkit.ChatColor.AQUA + (Object)org.bukkit.ChatColor.BOLD + UtilTime.formatDateDiff(groomedHorse.token.brushTime, true)));
                    return;
                }
                if (groomedHorse.token.brush != 5) {
                    ++groomedHorse.token.brush;
                    UtilPlayer.message((Entity)player, F.main("Grooming", "You have brushed " + horse.token.name));
                    return;
                }
                UtilPlayer.message((Entity)player, F.main("Grooming", "You have finished brushing " + horse.token.name));
                horse.addAppearance(1);
                groomedHorse.token.brush = 0;
                this.addCooldown(groomedHorse, item);
                break;
            }
            case BLAZE_POWDER: {
                if (System.currentTimeMillis() <= groomedHorse.token.sweatScraperTime) {
                    UtilPlayer.message((Entity)player, F.main("Grooming", horse.token.name + (Object)org.bukkit.ChatColor.GRAY + "'s bathing cooldown is active " + (Object)org.bukkit.ChatColor.AQUA + (Object)org.bukkit.ChatColor.BOLD + UtilTime.formatDateDiff(groomedHorse.token.sweatScraperTime, true)));
                    return;
                }
                if (groomedHorse.token.water && groomedHorse.token.shampoo) {
                    UtilPlayer.message((Entity)player, F.main("Grooming", "You have finished washing " + horse.token.name));
                    horse.addAppearance(3);
                    groomedHorse.token.water = false;
                    groomedHorse.token.shampoo = false;
                    this.addCooldown(groomedHorse, item);
                    return;
                }
                UtilPlayer.message((Entity)player, F.main("Grooming", "You must soak and use shampoo on " + horse.token.name + (Object)org.bukkit.ChatColor.GRAY + " before you can use this"));
                break;
            }
            case QUARTZ: {
                if (System.currentTimeMillis() <= groomedHorse.token.hoofTime) {
                    UtilPlayer.message((Entity)player, F.main("Grooming", horse.token.name + (Object)org.bukkit.ChatColor.GRAY + "'s hoof picking cooldown is active " + (Object)org.bukkit.ChatColor.AQUA + (Object)org.bukkit.ChatColor.BOLD + UtilTime.formatDateDiff(groomedHorse.token.hoofTime, true)));
                    return;
                }
                if (groomedHorse.token.hoof != 3) {
                    ++groomedHorse.token.hoof;
                    UtilPlayer.message((Entity)player, F.main("Grooming", "You have picked " + horse.token.name + (Object)org.bukkit.ChatColor.GRAY + "'s hooves"));
                    return;
                }
                UtilPlayer.message((Entity)player, F.main("Grooming", "You have finished picking " + horse.token.name + (Object)org.bukkit.ChatColor.GRAY + "'s hooves"));
                horse.addAppearance(1);
                groomedHorse.token.hoof = 0;
                this.addCooldown(groomedHorse, item);
                break;
            }
            case FLINT: {
                if (System.currentTimeMillis() <= groomedHorse.token.combTime) {
                    UtilPlayer.message((Entity)player, F.main("Grooming", horse.token.name + (Object)org.bukkit.ChatColor.GRAY + "'s combing cooldown is active " + (Object)org.bukkit.ChatColor.AQUA + (Object)org.bukkit.ChatColor.BOLD + UtilTime.formatDateDiff(groomedHorse.token.combTime, true)));
                    return;
                }
                if (groomedHorse.token.comb != 5) {
                    ++groomedHorse.token.comb;
                    UtilPlayer.message((Entity)player, F.main("Grooming", "You have combed " + horse.token.name + (Object)org.bukkit.ChatColor.GRAY));
                    return;
                }
                horse.addAppearance(1);
                groomedHorse.token.comb = 0;
                UtilPlayer.message((Entity)player, F.main("Grooming", "You have finished combing " + horse.token.name));
                this.addCooldown(groomedHorse, item);
                break;
            }
            case NETHER_BRICK_ITEM: {
                if (System.currentTimeMillis() <= groomedHorse.token.sheddingTime) {
                    UtilPlayer.message((Entity)player, F.main("Grooming", horse.token.name + (Object)org.bukkit.ChatColor.GRAY + "'s shedding blade cooldown is active " + (Object)org.bukkit.ChatColor.AQUA + (Object)org.bukkit.ChatColor.BOLD + UtilTime.formatDateDiff(groomedHorse.token.sheddingTime, true)));
                    return;
                }
                if (groomedHorse.token.shedding != 2) {
                    ++groomedHorse.token.shedding;
                    UtilPlayer.message((Entity)player, F.main("Grooming", "You have removed loose hair"));
                    return;
                }
                horse.addAppearance(1);
                groomedHorse.token.shedding = 0;
                UtilPlayer.message((Entity)player, F.main("Grooming", "You have removed " + horse.token.name + (Object)org.bukkit.ChatColor.GRAY + "'s shedded hair"));
                this.addCooldown(groomedHorse, item);
                break;
            }
            case INK_SACK: {
                short data = item.getDurability();
                int temp = groomedHorse.token.maneAndTail++;
                if (data == 13) {
                    if (System.currentTimeMillis() <= groomedHorse.token.maneAndTailTime) {
                        UtilPlayer.message((Entity)player, F.main("Grooming", horse.token.name + (Object)org.bukkit.ChatColor.GRAY + "'s Mane and Tail comb cooldown is active " + (Object)org.bukkit.ChatColor.AQUA + (Object)org.bukkit.ChatColor.BOLD + UtilTime.formatDateDiff(groomedHorse.token.maneAndTailTime, true)));
                        return;
                    }
                    if (temp == 0) {
                        UtilPlayer.message((Entity)player, F.main("Grooming", "You have combed " + horse.token.name + (Object)org.bukkit.ChatColor.GRAY + "'s mane"));
                        break;
                    }
                    if (temp != 1) break;
                    UtilPlayer.message((Entity)player, F.main("Grooming", "You have combed " + horse.token.name + (Object)org.bukkit.ChatColor.GRAY + "'s tail"));
                    --groomedHorse.token.maneAndTail;
                    horse.addAppearance(1);
                    this.addCooldown(groomedHorse, item);
                    break;
                }
                if (data == 10) {
                    if (!groomedHorse.token.water) {
                        UtilPlayer.message((Entity)player, F.main("Grooming", "You must apply water before you add shampoo"));
                        return;
                    }
                    if (groomedHorse.token.shampoo) {
                        UtilPlayer.message((Entity)player, F.main("Grooming", "You have already applied shampoo to " + horse.token.name));
                        return;
                    }
                    groomedHorse.token.shampoo = true;
                    UtilPlayer.message((Entity)player, F.main("Grooming", "You have have shampooed " + horse.token.name));
                    break;
                }
                if (data == 0) {
                    if (System.currentTimeMillis() <= groomedHorse.token.hoofOilTime) {
                        UtilPlayer.message((Entity)player, F.main("Grooming", horse.token.name + (Object)org.bukkit.ChatColor.GRAY + "'s hoof oil cooldown is active " + (Object)org.bukkit.ChatColor.AQUA + (Object)org.bukkit.ChatColor.BOLD + UtilTime.formatDateDiff(groomedHorse.token.hoofOilTime, true)));
                        return;
                    }
                    if (groomedHorse.token.hoofOil != 3) {
                        ++groomedHorse.token.hoofOil;
                        UtilPlayer.message((Entity)player, F.main("Grooming", "You have oiled one of " + horse.token.name + (Object)org.bukkit.ChatColor.GRAY + "'s hooves"));
                        return;
                    }
                    horse.addAppearance(1);
                    groomedHorse.token.hoofOil = 0;
                    UtilPlayer.message((Entity)player, F.main("Grooming", "You have oiled " + horse.token.name + (Object)org.bukkit.ChatColor.GRAY + "'s hooves"));
                    this.addCooldown(groomedHorse, item);
                    return;
                }
                if (data != 7) break;
                if (System.currentTimeMillis() <= groomedHorse.token.faceBrushTime) {
                    UtilPlayer.message((Entity)player, F.main("Grooming", horse.token.name + (Object)org.bukkit.ChatColor.GRAY + "'s face brush cooldown is active " + (Object)org.bukkit.ChatColor.AQUA + (Object)org.bukkit.ChatColor.BOLD + UtilTime.formatDateDiff(groomedHorse.token.faceBrushTime, true)));
                    return;
                }
                if (groomedHorse.token.faceBrush != 1) {
                    ++groomedHorse.token.faceBrush;
                    UtilPlayer.message((Entity)player, F.main("Grooming", "You have brushed " + horse.token.name + (Object)org.bukkit.ChatColor.GRAY + "'s face"));
                    return;
                }
                horse.addAppearance(1);
                groomedHorse.token.faceBrush = 0;
                UtilPlayer.message((Entity)player, F.main("Grooming", "You have finished brushing " + horse.token.name + (Object)org.bukkit.ChatColor.GRAY + "'s face"));
                this.addCooldown(groomedHorse, item);
                return;
            }
            case WATER_BUCKET: {
                if (!groomedHorse.token.water) {
                    UtilPlayer.message((Entity)player, F.main("Grooming", "You have soaked " + horse.token.name));
                    groomedHorse.token.water = true;
                    return;
                }
                UtilPlayer.message((Entity)player, F.main("Grooming", "You have already wet " + horse.token.name));
                break;
            }
            case GOLD_NUGGET: {
                if (System.currentTimeMillis() <= groomedHorse.token.showSheenTime) {
                    UtilPlayer.message((Entity)player, F.main("Grooming", horse.token.name + (Object)org.bukkit.ChatColor.GRAY + "'s show sheen cooldown is active " + (Object)org.bukkit.ChatColor.AQUA + (Object)org.bukkit.ChatColor.BOLD + UtilTime.formatDateDiff(groomedHorse.token.showSheenTime, true)));
                    return;
                }
                if (groomedHorse.token.showSheen != 2) {
                    ++groomedHorse.token.showSheen;
                    UtilPlayer.message((Entity)player, F.main("Grooming", "You have applied show sheen"));
                    return;
                }
                horse.addAppearance(1);
                groomedHorse.token.showSheen = 0;
                UtilPlayer.message((Entity)player, F.main("Grooming", "You have finished applying show sheen to " + horse.token.name));
                this.addCooldown(groomedHorse, item);
                break;
            }
        }
    }

    private void addCooldown(GroomedHorse horse, ItemStack item) {
        switch (item.getType()) {
            case SHEARS: {
                horse.token.shearTime = System.currentTimeMillis() + 7200000L;
                break;
            }
            case BLAZE_ROD: {
                horse.token.brushTime = System.currentTimeMillis() + 7200000L;
                break;
            }
            case BLAZE_POWDER: {
                horse.token.sweatScraperTime = System.currentTimeMillis() + 7200000L;
                break;
            }
            case QUARTZ: {
                horse.token.hoofTime = System.currentTimeMillis() + 7200000L;
                break;
            }
            case FLINT: {
                horse.token.combTime = System.currentTimeMillis() + 7200000L;
                break;
            }
            case NETHER_BRICK_ITEM: {
                horse.token.sheddingTime = System.currentTimeMillis() + 7200000L;
                break;
            }
            case INK_SACK: {
                short data = item.getDurability();
                if (data == 13) {
                    horse.token.maneAndTailTime = System.currentTimeMillis() + 7200000L;
                    break;
                }
                if (data == 0) {
                    horse.token.hoofOilTime = System.currentTimeMillis() + 7200000L;
                    break;
                }
                if (data != 7) break;
                horse.token.faceBrushTime = System.currentTimeMillis() + 7200000L;
                break;
            }
            case GOLD_NUGGET: {
                horse.token.showSheenTime = System.currentTimeMillis() + 7200000L;
            }
        }
    }

    private boolean isGroomingItem(net.minecraft.server.v1_12_R1.ItemStack item) {
        NBTTagCompound tags = item.getTag();
        return tags != null && tags.getString("token") != null;
    }

}
