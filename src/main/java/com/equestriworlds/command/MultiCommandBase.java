/*
 * Decompiled with CFR 0_133.
 * 
 * Could not load the following classes:
 *  com.google.common.base.Joiner
 *  org.bukkit.command.CommandSender
 *  org.bukkit.entity.Player
 */
package com.equestriworlds.command;

import com.equestriworlds.MiniPlugin;
import com.equestriworlds.command.CommandBase;
import com.equestriworlds.command.CommandCenter;
import com.equestriworlds.command.ICommand;
import com.equestriworlds.common.Rank;
import com.google.common.base.Joiner;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public abstract class MultiCommandBase<PluginType extends MiniPlugin>
extends CommandBase<PluginType> {
    protected HashMap<String, ICommand> Commands = new HashMap();

    public /* varargs */ MultiCommandBase(PluginType plugin, Rank rank, String ... aliases) {
        super(plugin, rank, aliases);
    }

    public /* varargs */ MultiCommandBase(PluginType plugin, Rank rank, Rank[] specificRanks, String ... aliases) {
        super(plugin, rank, specificRanks, aliases);
    }

    public void AddCommand(ICommand command) {
        for (String commandRoot : command.Aliases()) {
            this.Commands.put(commandRoot, command);
            command.SetCommandCenter(this.CommandCenter);
        }
    }

    @Override
    public void Execute(Player caller, String[] args) {
        ICommand command;
        String commandName = null;
        String[] newArgs = null;
        if (args != null && args.length > 0) {
            commandName = args[0].toLowerCase();
            if (args.length > 1) {
                newArgs = new String[args.length - 1];
                System.arraycopy(args, 1, newArgs, 0, newArgs.length);
            }
        }
        if ((command = this.Commands.get(commandName)) != null) {
            if (Rank.Has(caller, command.GetRequiredRank(), command.GetSpecificRanks(), true)) {
                command.SetAliasUsed(commandName);
                command.Execute(caller, newArgs);
            }
        } else {
            this.Help(caller, args);
        }
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, String commandLabel, String[] args) {
        if (args.length == 1) {
            ArrayList<String> possibleMatches = new ArrayList<String>();
            for (ICommand command : this.Commands.values()) {
                if (!Rank.Has((Player)sender, command.GetRequiredRank(), command.GetSpecificRanks(), false) || possibleMatches.contains(command.Aliases().toArray()[0])) continue;
                possibleMatches.add((String)command.Aliases().toArray()[0]);
            }
            return this.getMatches(args[0], possibleMatches);
        }
        if (args.length > 1) {
            return this.getMatches(Joiner.on((String)" ").join((Object[])args).replaceAll("^.*?(\\w+)\\W*$", "$1"), new ArrayList<String>());
        }
        return null;
    }

    protected abstract void Help(Player var1, String[] var2);
}
