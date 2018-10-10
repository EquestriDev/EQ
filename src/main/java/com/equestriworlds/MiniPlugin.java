/*
 * Decompiled with CFR 0_133.
 * 
 * Could not load the following classes:
 *  org.bukkit.Server
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.HandlerList
 *  org.bukkit.event.Listener
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.plugin.PluginManager
 *  org.bukkit.plugin.java.JavaPlugin
 *  org.bukkit.scheduler.BukkitScheduler
 *  org.bukkit.scheduler.BukkitTask
 */
package com.equestriworlds;

import com.equestriworlds.MiniModule;
import com.equestriworlds.command.CommandCenter;
import com.equestriworlds.command.ICommand;
import com.equestriworlds.servermanager.ServerStopEvent;
import com.equestriworlds.util.C;
import com.equestriworlds.util.F;
import com.equestriworlds.util.UtilTime;
import java.io.PrintStream;
import java.util.Collection;
import java.util.HashMap;
import org.bukkit.Server;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scheduler.BukkitTask;

public abstract class MiniPlugin
implements Listener {
    private String _pluginName = "Default";
    protected JavaPlugin _plugin;
    private HashMap<String, ICommand> _commands;
    HashMap<String, MiniModule> _modules;

    public MiniPlugin(String pluginName, JavaPlugin plugin) {
        this._pluginName = pluginName;
        this._plugin = plugin;
        this._commands = new HashMap();
        this._modules = new HashMap();
        this.onEnable();
        this.registerEvents(this);
    }

    public PluginManager getPluginManager() {
        return this._plugin.getServer().getPluginManager();
    }

    public BukkitScheduler getScheduler() {
        return this._plugin.getServer().getScheduler();
    }

    public JavaPlugin getPlugin() {
        return this._plugin;
    }

    public void registerEvents(Listener listener) {
        this._plugin.getServer().getPluginManager().registerEvents(listener, (Plugin)this._plugin);
    }

    public void registerSelf() {
        this.registerEvents(this);
    }

    private void deregisterSelf() {
        HandlerList.unregisterAll((Listener)this);
    }

    private void onEnable() {
        long epoch = System.currentTimeMillis();
        System.out.println("============================");
        this.log("Initializing...");
        this.enable();
        this.addCommands();
        this.log("Enabled in " + UtilTime.convertString(System.currentTimeMillis() - epoch, 1, UtilTime.TimeUnit.FIT) + ".");
    }

    protected final void onDisable() {
        this.log("Disabling...");
        this.disable();
        for (ICommand commandsToRemove : this._commands.values()) {
            this.removeCommand(commandsToRemove);
        }
        for (MiniModule module : this._modules.values()) {
            module.onDisable();
        }
        this.deregisterSelf();
        this.log("Disabled.");
    }

    private void enable() {
    }

    public void disable() {
    }

    @EventHandler
    public void shutdown(ServerStopEvent e) {
        this.onDisable();
    }

    private void addCommands() {
    }

    public final String getName() {
        return this._pluginName;
    }

    public final void addCommand(ICommand command) {
        CommandCenter.Instance.AddCommand(command);
    }

    private void removeCommand(ICommand command) {
        CommandCenter.Instance.RemoveCommand(command);
    }

    private void log(String message) {
        System.out.println(F.main(this._pluginName, message));
    }

    protected void runAsync(Runnable runnable) {
        this._plugin.getServer().getScheduler().runTaskAsynchronously((Plugin)this._plugin, runnable);
    }

    public BukkitTask runAsyncTimer(Runnable runnable, int delay, int tick) {
        return this._plugin.getServer().getScheduler().runTaskTimerAsynchronously((Plugin)this._plugin, runnable, (long)delay, (long)tick);
    }

    public BukkitTask runAsyncLater(Runnable runnable, long delay) {
        return this._plugin.getServer().getScheduler().runTaskLaterAsynchronously((Plugin)this._plugin, runnable, delay);
    }

    public BukkitTask runSync(Runnable runnable) {
        return this._plugin.getServer().getScheduler().runTask((Plugin)this._plugin, runnable);
    }

    public void runSyncLater(Runnable runnable, long delay) {
        this._plugin.getServer().getScheduler().runTaskLater((Plugin)this._plugin, runnable, delay);
    }
}
