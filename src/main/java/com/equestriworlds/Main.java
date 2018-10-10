/*
 * Decompiled with CFR 0_133.
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.Server
 *  org.bukkit.event.Listener
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.plugin.java.JavaPlugin
 *  org.bukkit.scheduler.BukkitScheduler
 */
package com.equestriworlds;

import com.equestriworlds.account.CoreClientManager;
import com.equestriworlds.barn.BarnManager;
import com.equestriworlds.certifications.CertificationsManager;
import com.equestriworlds.chat.ChatManager;
import com.equestriworlds.command.CommandCenter;
import com.equestriworlds.grooming.CrosstiesManager;
import com.equestriworlds.grooming.GroomingManager;
import com.equestriworlds.horse.HorseManager;
import com.equestriworlds.horse.config.HorseConfig;
import com.equestriworlds.itemstack.ItemStackFactory;
import com.equestriworlds.prefs.PreferenceManager;
import com.equestriworlds.recharge.Recharge;
import com.equestriworlds.rpmanager.ResourcePackManager;
import com.equestriworlds.update.Updater;
import com.equestriworlds.util.C;
import com.equestriworlds.vet.VetManager;
import java.io.PrintStream;
import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;

public class Main
extends JavaPlugin
implements Listener {
    public HorseManager _horseManager;

    public void onEnable() {
        saveDefaultConfig();
        for (String f: new String[] {
                "certifications/certifications.yml",
                "milo3.png",
                "milo2.png",
                "xray.png",
                "got.png",
                "leocaitycrystal.png",
                "megan.png",
                "leo.png",
                "milo.png",
                "plugin.yml",
                "caity.png"
            }) {
            saveResource(f, false);
        }
        CoreClientManager clientManager = new CoreClientManager(this);
        CommandCenter.Initialize(this);
        this.getServer().getScheduler().scheduleSyncRepeatingTask((Plugin)this, (Runnable)new Updater(this), 1L, 1L);
        Recharge.Initialize(this);
        ItemStackFactory.Initialize(this, false);
        new PreferenceManager(this);
        new ChatManager(this);
        new HorseConfig();
        new CertificationsManager(this);
        new VetManager(this);
        this._horseManager = new HorseManager(this, clientManager);
        new ResourcePackManager(this);
        new BarnManager(this, clientManager);
        new GroomingManager(this);
        new CrosstiesManager(this);
        Bukkit.getPluginManager().registerEvents((Listener)this, (Plugin)this);
        System.out.println("============================");
    }

    public void onDisable() {
        if (this._horseManager != null) {
            this._horseManager.onDisable();
        }
    }

    public HorseManager getHorseManager() {
        return this._horseManager;
    }
}
