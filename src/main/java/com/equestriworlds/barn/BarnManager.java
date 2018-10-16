package com.equestriworlds.barn;

import com.equestriworlds.MiniPlugin;
import com.equestriworlds.account.CoreClientManager;
import com.equestriworlds.barn.BarnConfig;
import com.equestriworlds.barn.commands.BarnCommand;
import com.equestriworlds.command.ICommand;
import com.equestriworlds.util.C;
import com.equestriworlds.util.UtilPlayer;
import com.intellectualcrafters.plot.api.PlotAPI;
import com.intellectualcrafters.plot.object.Plot;
import com.plotsquared.bukkit.events.PlotDeleteEvent;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.ServicesManager;
import org.bukkit.plugin.java.JavaPlugin;

public class BarnManager
extends MiniPlugin {
    public CoreClientManager clientManager;
    public PlotAPI plot;
    public Economy eco;
    private BarnConfig yml;

    public BarnManager(JavaPlugin plugin, CoreClientManager clientManager) {
        super("Barn Manager", plugin);
        this.clientManager = clientManager;
        PluginManager manager = Bukkit.getServer().getPluginManager();
        Plugin plotsquared = manager.getPlugin("PlotSquared");
        if (plotsquared != null && !plotsquared.isEnabled()) {
            System.out.println(C.cRed + C.Bold + "Could not find PlotSquared! Disabling plugin...");
            this.onDisable();
            return;
        }
        this.plot = new PlotAPI();
        this.yml = new BarnConfig();
        if (!this.setupEconomy()) {
            this.onDisable();
            return;
        }
        this.addCommand(new BarnCommand(this));
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

    public void addToConfig(int tempID, String item) {
        List<String> list = this.yml.contains("barn." + tempID) ? this.yml.getStringList("barn." + tempID) : new ArrayList<String>();
        list.add(item);
        this.yml.set("barn." + tempID, list);
        this.yml.save();
    }

    public void removeFromList(int tempID, String item, Player player) {
        List<String> list = this.yml.contains("barn." + tempID) ? this.yml.getStringList("barn." + tempID) : new ArrayList<>();
        if (list.contains(item)) {
            list.remove(item);
            this.yml.set("barn." + tempID, list);
            this.yml.save();
        } else {
            Plot basePlot = this.plot.getPlot(player);
            for (Plot plots : basePlot.getConnectedPlots()) {
                List<String> connectedPlots = this.yml.contains("barn." + plots.temp) ? this.yml.getStringList("barn." + plots.temp) : new ArrayList<>();
                if (!connectedPlots.contains(item)) continue;
                connectedPlots.remove(item);
                this.yml.set("barn." + plots.temp, connectedPlots);
                this.yml.save();
                return;
            }
        }
    }

    public List<String> getFromConfig(int tempID) {
        if (this.yml.contains("barn." + tempID)) {
            return this.yml.getStringList("barn." + tempID);
        }
        return new ArrayList<String>();
    }

    public void plotRemove(PlotDeleteEvent e) {
        for (Plot plots : e.getPlot().getConnectedPlots()) {
            if (!this.yml.contains("barn." + plots.temp)) continue;
            this.yml.set("barn." + plots.temp, null);
        }
        if (this.yml.contains("barn." + e.getPlot().temp)) {
            this.yml.set("barn." + e.getPlot().temp, null);
            Player player = UtilPlayer.searchExact(e.getPlot().owner);
            if (player == null) {
                return;
            }
            UtilPlayer.message((Entity)player, "asdasd");
        }
        this.yml.save();
    }
}
