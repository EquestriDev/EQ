/*
 * Decompiled with CFR 0_133.
 * 
 * Could not load the following classes:
 *  org.bukkit.event.HandlerList
 *  org.bukkit.event.Listener
 */
package com.equestriworlds;

import com.equestriworlds.MiniPlugin;
import com.equestriworlds.util.C;
import com.equestriworlds.util.F;
import java.io.PrintStream;
import java.util.HashMap;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;

public abstract class MiniModule<PluginType extends MiniPlugin>
implements Listener {
    protected String Name;
    protected PluginType Plugin;

    public MiniModule(String name, PluginType plugin) {
        this.Name = name;
        this.Plugin = plugin;
        this.onEnable();
    }

    private void onEnable() {
        this.log("Initializing module for " + this.Plugin.getName() + "...");
        this.enable();
        this.Plugin._modules.put(this.Name, this);
        this.registerSelf();
    }

    final void onDisable() {
        this.disable();
        this.deregisterSelf();
        this.log("Disabled");
    }

    private void enable() {
    }

    private void disable() {
    }

    private void registerSelf() {
        this.Plugin.registerEvents(this);
    }

    private void deregisterSelf() {
        HandlerList.unregisterAll((Listener)this);
    }

    public final String getName() {
        return this.Name;
    }

    private void log(String message) {
        System.out.println(C.consoleConvert(F.main("    - " + this.Name, message)));
    }
}
