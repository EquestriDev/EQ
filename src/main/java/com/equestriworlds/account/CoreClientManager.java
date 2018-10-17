package com.equestriworlds.account;

import com.equestriworlds.MiniPlugin;
import com.equestriworlds.account.CoreClient;
import com.equestriworlds.account.event.ClientLoadEvent;
import com.equestriworlds.account.event.ClientUnloadEvent;
import com.equestriworlds.util.UtilServer;
import java.util.HashMap;
import java.util.UUID;
import org.bukkit.OfflinePlayer;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Name based online player cache.
 */
public class CoreClientManager
extends MiniPlugin {
    private JavaPlugin _plugin;
    private HashMap<String, CoreClient> _clientList;
    private final Object _clientLock = new Object();

    public CoreClientManager(JavaPlugin plugin) {
        super("Client Manager", plugin);
        this._plugin = plugin;
        this._clientList = new HashMap();
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private void Add(String name) {
        CoreClient oldClient;
        CoreClient newClient = null;
        if (newClient == null) {
            newClient = new CoreClient(name);
        }
        Object object = this._clientLock;
        synchronized (object) {
            oldClient = this._clientList.put(name, newClient);
        }
        if (oldClient != null) {
            oldClient.Delete();
        }
        this._plugin.getServer().getPluginManager().callEvent((Event)new ClientLoadEvent(name, UtilServer.getServer().getOfflinePlayer(name).getUniqueId()));
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public void Del(String name) {
        Object object = this._clientLock;
        synchronized (object) {
            this._clientList.remove(name);
        }
        this._plugin.getServer().getPluginManager().callEvent((Event)new ClientUnloadEvent(name));
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public CoreClient Get(String name) {
        Object object = this._clientLock;
        synchronized (object) {
            return this._clientList.get(name);
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public CoreClient Get(Player player) {
        Object object = this._clientLock;
        synchronized (object) {
            return this.Get(player.getName());
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @EventHandler(priority=EventPriority.LOWEST)
    public void Login(PlayerLoginEvent event) {
        Object object = this._clientLock;
        synchronized (object) {
            if (!this._clientList.containsKey(event.getPlayer().getName())) {
                this.Add(event.getPlayer().getName());
            }
        }
        CoreClient client = this.Get(event.getPlayer().getName());
        if (client == null) {
            event.disallow(PlayerLoginEvent.Result.KICK_OTHER, "There was an error logging you in.  Please reconnect.");
        }
    }
}
