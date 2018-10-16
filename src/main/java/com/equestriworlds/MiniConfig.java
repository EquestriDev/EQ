package com.equestriworlds;

import com.equestriworlds.MiniConfigPlugin;
import com.equestriworlds.update.UpdateEvent;
import com.equestriworlds.update.UpdateType;
import com.equestriworlds.util.UtilServer;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Set;
import java.util.UUID;
import java.util.logging.Level;
import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.MemoryConfigurationOptions;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.configuration.file.YamlConfigurationOptions;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class MiniConfig<T>
extends YamlConfiguration
implements Listener {
    private MiniConfigPlugin<T> Plugin;
    public Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
    private File file;

    MiniConfig(MiniConfigPlugin<T> plugin) {
        this.Plugin = plugin;
        Validate.notNull(this.Plugin, (String)"Plugin is null");
        this.file = new File("./plugins/EQ/" + this.Plugin.getName() + "/" + this.Plugin.getName() + ".yml");
        this.Plugin.registerEvents((Listener)this);
        if (!this.file.exists()) {
            try {
                this.save(this.file);
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            this.load(this.file);
        }
        catch (FileNotFoundException ex) {
            try {
                this.file.createNewFile();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
        catch (IOException | InvalidConfigurationException ex) {
            Bukkit.getLogger().log(Level.SEVERE, "Cannot load " + this.file, ex);
        }
    }

    @EventHandler
    public void updateSave(UpdateEvent e) {
        if (e.getType() != UpdateType.MIN_01) {
            return;
        }
        for (String name : this.Plugin._clientData.keySet()) {
            this.set(this.Plugin.getName() + "." + UtilServer.getServer().getOfflinePlayer(name).getUniqueId().toString(), (Object)this.gson.toJson(this.Plugin.Get(name)));
        }
        try {
            this.save(this.file);
        }
        catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
