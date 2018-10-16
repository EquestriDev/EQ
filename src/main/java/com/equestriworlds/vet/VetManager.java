package com.equestriworlds.vet;

import com.equestriworlds.Main;
import com.equestriworlds.MiniPlugin;
import com.equestriworlds.util.F;
import com.equestriworlds.util.Renderer;
import com.equestriworlds.util.UtilPlayer;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import net.milkbowl.vault.economy.Economy;
import net.minecraft.server.v1_12_R1.NBTBase;
import net.minecraft.server.v1_12_R1.NBTTagCompound;
import org.apache.commons.io.FileUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftItemStack;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.server.MapInitializeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.map.MapRenderer;
import org.bukkit.map.MapView;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.ServicesManager;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Economy, gunpowder item, load some maps.
 */
public class VetManager
extends MiniPlugin {
    public Economy eco;

    public VetManager(JavaPlugin plugin) {
        super("Vet", plugin);
        this.loadMaps();
        if (!this.setupEconomy()) {
            this.onDisable();
        }
    }

    private boolean setupEconomy() {
        if (this.getPlugin().getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider rsp = this.getPlugin().getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        this.eco = (Economy)rsp.getProvider();
        return this.eco != null;
    }

    /**
     * Mark sulphur/gunpowder named "Mange Spray" with nbt tags.
     */
    @EventHandler
    public void onUse(PlayerInteractEvent event) {
        int uses;
        if (!event.getPlayer().isOp()) return;
        Player player = event.getPlayer();
        Action action = event.getAction();
        if (action != Action.RIGHT_CLICK_AIR && action != Action.RIGHT_CLICK_BLOCK) {
            return;
        }
        if (event.getItem() == null || event.getItem().getType() != Material.SULPHUR) {
            return;
        }
        if (event.getItem().getItemMeta() == null || event.getItem().getItemMeta().getDisplayName() == null) {
            return;
        }
        if (!event.getItem().getItemMeta().getDisplayName().contains("Mange Spray")) {
            return;
        }
        net.minecraft.server.v1_12_R1.ItemStack item = CraftItemStack.asNMSCopy((ItemStack)event.getItem());
        NBTTagCompound tags = item.getTag();
        if (tags == null) {
            tags = new NBTTagCompound();
        }
        if (tags.get("token") == null) {
            tags.setString("token", String.valueOf(UUID.randomUUID()));
        }
        if (tags.get("mangeSpray") == null) {
            tags.setInt("mangeSpray", 6);
        }
        if ((uses = tags.getInt("mangeSpray")) == 0) {
            UtilPlayer.message((Entity)player, F.main("Vet", "Mange Spray has been broken!"));
            ItemStack spigotItem = CraftItemStack.asBukkitCopy((net.minecraft.server.v1_12_R1.ItemStack)item);
            if (spigotItem.getAmount() == 1) {
                player.getInventory().remove(spigotItem);
                return;
            }
            spigotItem.setAmount(spigotItem.getAmount() - 1);
            player.getInventory().setItemInMainHand(spigotItem);
            return;
        }
        tags.setInt("mangeSpray", tags.getInt("mangeSpray") - 1);
        UtilPlayer.message((Entity)player, F.main("Vet", "Mange Spray current uses: " + uses));
        item.setTag(tags);
        ItemStack spigotItem = CraftItemStack.asBukkitCopy((net.minecraft.server.v1_12_R1.ItemStack)item);
        player.getInventory().setItemInMainHand(spigotItem);
    }

    /**
     * Turn map with id #11 into xray.png
     */
    @EventHandler
    public void onMapLoad(MapInitializeEvent event) {
        MapView mapView = event.getMap();
        if (mapView.getId() != 11) {
            return;
        }
        mapView.setScale(MapView.Scale.FARTHEST);
        mapView.getRenderers().clear();
        try {
            mapView.addRenderer((MapRenderer)new Renderer("xray"));
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadMaps() {
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
        YamlConfiguration config = YamlConfiguration.loadConfiguration((File)langFile);
        for (String string : config.getConfigurationSection("maps").getKeys(false)) {
            MapView view = Bukkit.getMap((short)Short.parseShort(string));
            if (view == null) continue;
            view.getRenderers().clear();
            try {
                view.addRenderer((MapRenderer)new Renderer(config.getString("maps." + string)));
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
