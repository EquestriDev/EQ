/*
 * Decompiled with CFR 0_133.
 * 
 * Could not load the following classes:
 *  org.bukkit.Server
 *  org.bukkit.event.Event
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.plugin.PluginManager
 *  org.bukkit.plugin.java.JavaPlugin
 *  org.bukkit.scheduler.BukkitScheduler
 */
package com.equestriworlds.update;

import com.equestriworlds.update.UpdateEvent;
import com.equestriworlds.update.UpdateType;
import org.bukkit.Server;
import org.bukkit.event.Event;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;

public class Updater
implements Runnable {
    private JavaPlugin _plugin;

    public Updater(JavaPlugin plugin) {
        this._plugin = plugin;
        this._plugin.getServer().getScheduler().scheduleSyncRepeatingTask((Plugin)this._plugin, (Runnable)this, 0L, 1L);
    }

    @Override
    public void run() {
        for (UpdateType updateType : UpdateType.values()) {
            if (!updateType.Elapsed()) continue;
            this._plugin.getServer().getPluginManager().callEvent((Event)new UpdateEvent(updateType));
        }
    }
}
