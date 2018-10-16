package com.equestriworlds.prefs;

import com.equestriworlds.MiniConfig;
import com.equestriworlds.MiniConfigPlugin;
import com.equestriworlds.account.event.ClientLoadEvent;
import com.equestriworlds.command.ICommand;
import com.equestriworlds.prefs.Preference;
import com.equestriworlds.prefs.command.PreferenceCommand;
import com.equestriworlds.util.UtilServer;
import com.google.gson.Gson;
import java.util.UUID;
import org.bukkit.OfflinePlayer;
import org.bukkit.event.EventHandler;
import org.bukkit.plugin.java.JavaPlugin;

public class PreferenceManager
extends MiniConfigPlugin<Preference> {
    public PreferenceManager(JavaPlugin plugin) {
        super("Preference Manager", plugin);
        this.addCommand(new PreferenceCommand(this));
    }

    @EventHandler
    public void LoadPlayer(ClientLoadEvent e) {
        Preference pref = this.GetConfigJson(UtilServer.getServer().getOfflinePlayer(e.GetUUID())) == null ? new Preference() : (Preference)this.config.gson.fromJson(this.GetConfigJson(UtilServer.getServer().getOfflinePlayer(e.GetUUID())), Preference.class);
        this.Set(e.GetName(), pref);
    }

    @Override
    protected Preference AddPlayer(String player) {
        return new Preference();
    }
}
