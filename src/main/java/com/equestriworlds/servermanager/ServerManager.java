/*
 * Decompiled with CFR 0_133.
 * 
 * Could not load the following classes:
 *  org.bukkit.entity.Player
 *  org.bukkit.event.Event
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.player.PlayerCommandPreprocessEvent
 *  org.bukkit.event.server.ServerCommandEvent
 *  org.bukkit.plugin.PluginManager
 *  org.bukkit.plugin.java.JavaPlugin
 */
package com.equestriworlds.servermanager;

import com.equestriworlds.MiniPlugin;
import com.equestriworlds.common.Rank;
import com.equestriworlds.servermanager.ServerStopEvent;
import com.equestriworlds.util.F;
import com.equestriworlds.util.UtilPlayer;
import com.equestriworlds.util.UtilServer;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.server.ServerCommandEvent;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class ServerManager
extends MiniPlugin {
    public ServerManager(JavaPlugin plugin) {
        super("Server Manager", plugin);
    }

    @EventHandler
    public void consoleShutdown(ServerCommandEvent e) {
        if (e.getCommand().equalsIgnoreCase("stop") || e.getCommand().equalsIgnoreCase("restart") || e.getCommand().equalsIgnoreCase("reload") || e.getCommand().equalsIgnoreCase("rl")) {
            e.setCancelled(true);
            UtilServer.broadcast(F.main(this.getName(), "The server is shutting down in " + F.count("5.0 seconds") + "."));
            this.runSyncLater(() -> {
                for (Player players : UtilServer.getPlayers()) {
                    UtilPlayer.kick(players, "Thank you for playing", "The server is restarting!");
                }
                UtilServer.getServer().shutdown();
            }, 200L);
            this.getPluginManager().callEvent((Event)new ServerStopEvent());
        } else if (e.getCommand().equalsIgnoreCase("forceshutdown")) {
            UtilServer.getServer().shutdown();
        }
    }

    @EventHandler
    public void playerShutdown(PlayerCommandPreprocessEvent e) {
        if (e.getMessage().equalsIgnoreCase("/stop") || e.getMessage().equalsIgnoreCase("/restart") || e.getMessage().equalsIgnoreCase("/reload") || e.getMessage().equalsIgnoreCase("/rl")) {
            e.setCancelled(true);
            if (!Rank.Has(e.getPlayer(), Rank.OWNER, new Rank[]{Rank.DEV}, true)) {
                return;
            }
            UtilServer.broadcast(F.main(this.getName(), "The server is shutting down in " + F.count("5.0 seconds") + "!"));
            this.runSyncLater(new Runnable(){

                @Override
                public void run() {
                    for (Player players : UtilServer.getPlayers()) {
                        UtilPlayer.kick(players, "Thank you for playing", "The server is restarting!");
                    }
                    UtilServer.getServer().shutdown();
                }
            }, 200L);
            this.getPluginManager().callEvent((Event)new ServerStopEvent());
        }
    }

}
