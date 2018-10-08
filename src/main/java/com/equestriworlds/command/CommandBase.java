/*
 * Decompiled with CFR 0_133.
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.OfflinePlayer
 *  org.bukkit.command.CommandSender
 *  org.bukkit.entity.Player
 */
package com.equestriworlds.command;

import com.equestriworlds.MiniPlugin;
import com.equestriworlds.command.CommandCenter;
import com.equestriworlds.command.ICommand;
import com.equestriworlds.common.Rank;
import com.equestriworlds.util.UtilServer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public abstract class CommandBase<PluginType extends MiniPlugin>
implements ICommand {
    private Rank _requiredRank;
    private Rank[] _specificRank;
    private List<String> _aliases;
    protected PluginType Plugin;
    protected String AliasUsed;
    CommandCenter CommandCenter;

    public /* varargs */ CommandBase(PluginType plugin, Rank requiredRank, String ... aliases) {
        this.Plugin = plugin;
        this._requiredRank = requiredRank;
        this._aliases = Arrays.asList(aliases);
    }

    public /* varargs */ CommandBase(PluginType plugin, Rank requiredRank, Rank[] specificRank, String ... aliases) {
        this.Plugin = plugin;
        this._requiredRank = requiredRank;
        this._specificRank = specificRank;
        this._aliases = Arrays.asList(aliases);
    }

    @Override
    public Collection<String> Aliases() {
        return this._aliases;
    }

    @Override
    public void SetAliasUsed(String alias) {
        this.AliasUsed = alias;
    }

    @Override
    public Rank GetRequiredRank() {
        return this._requiredRank;
    }

    @Override
    public Rank[] GetSpecificRanks() {
        return this._specificRank;
    }

    @Override
    public void SetCommandCenter(CommandCenter commandCenter) {
        this.CommandCenter = commandCenter;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, String commandLabel, String[] args) {
        return null;
    }

    protected List<String> getMatches(String start, List<String> possibleMatches) {
        ArrayList<String> matches = new ArrayList<String>();
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (!player.getName().toLowerCase().startsWith(start.toLowerCase())) continue;
            matches.add(player.getName());
        }
        for (String possibleMatch : possibleMatches) {
            if (!possibleMatch.toLowerCase().startsWith(start.toLowerCase())) continue;
            matches.add(possibleMatch);
        }
        return matches;
    }

    protected List<String> getMatches(String start, Enum[] numerators) {
        ArrayList<String> matches = new ArrayList<String>();
        for (Enum e : numerators) {
            String s = e.toString();
            if (!s.toLowerCase().startsWith(start.toLowerCase())) continue;
            matches.add(s);
        }
        return matches;
    }

    protected List<String> getPlayerMatches(Player sender, String start) {
        ArrayList<String> matches = new ArrayList<String>();
        for (Player player : UtilServer.getPlayers()) {
            if (!sender.canSee(player) || !player.getName().toLowerCase().startsWith(start.toLowerCase())) continue;
            matches.add(player.getName());
        }
        return matches;
    }

    protected List<String> getOfflinePlayerMatches(Player sender, String start) {
        ArrayList<String> matches = new ArrayList<String>();
        for (OfflinePlayer player : UtilServer.getOfflinePlayers()) {
            if (!player.getName().toLowerCase().startsWith(start.toLowerCase())) continue;
            matches.add(player.getName());
        }
        return matches;
    }
}
