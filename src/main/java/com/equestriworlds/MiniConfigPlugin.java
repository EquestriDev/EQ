/*
 * Decompiled with CFR 0_133.
 * 
 * Could not load the following classes:
 *  org.bukkit.OfflinePlayer
 *  org.bukkit.entity.Player
 *  org.bukkit.event.EventHandler
 *  org.bukkit.plugin.java.JavaPlugin
 */
package com.equestriworlds;

import com.equestriworlds.MiniConfig;
import com.equestriworlds.MiniPlugin;
import com.equestriworlds.account.event.ClientUnloadEvent;
import java.util.Collection;
import java.util.HashMap;
import java.util.UUID;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.plugin.java.JavaPlugin;

public abstract class MiniConfigPlugin<DataType>
extends MiniPlugin {
    protected MiniConfig<DataType> config = new MiniConfig(this);
    private static Object _clientDataLock = new Object();
    HashMap<String, DataType> _clientData = new HashMap();

    public MiniConfigPlugin(String moduleName, JavaPlugin plugin) {
        super(moduleName, plugin);
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @EventHandler
    public void UnloadPlayer(ClientUnloadEvent event) {
        Object object = _clientDataLock;
        synchronized (object) {
            this._clientData.remove(event.GetName());
        }
    }

    public DataType Get(String name) {
        if (!this._clientData.containsKey(name)) {
            this._clientData.put(name, this.AddPlayer(name));
        }
        return this._clientData.get(name);
    }

    public DataType Get(Player player) {
        return this.Get(player.getName());
    }

    protected Collection<DataType> GetValues() {
        return this._clientData.values();
    }

    protected void Set(Player player, DataType data) {
        this.Set(player.getName(), data);
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    protected void Set(String name, DataType data) {
        Object object = _clientDataLock;
        synchronized (object) {
            this._clientData.put(name, data);
        }
    }

    protected String GetConfigJson(OfflinePlayer player) {
        return this.config.getString(this.getName() + "." + player.getUniqueId().toString());
    }

    protected abstract DataType AddPlayer(String var1);
}
