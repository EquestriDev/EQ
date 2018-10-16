package com.equestriworlds.horse.command.trust;

import com.equestriworlds.MiniPlugin;
import com.equestriworlds.command.ICommand;
import com.equestriworlds.command.MultiCommandBase;
import com.equestriworlds.common.Rank;
import com.equestriworlds.horse.HorseManager;
import com.equestriworlds.horse.command.trust.HorseTrustAddCommand;
import com.equestriworlds.horse.command.trust.HorseTrustListCommand;
import com.equestriworlds.horse.command.trust.HorseTrustRemoveCommand;
import com.equestriworlds.util.F;
import com.equestriworlds.util.UtilPlayer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

public class HorseTrustCommand
extends MultiCommandBase<HorseManager> {
    public HorseTrustCommand(HorseManager plugin) {
        super(plugin, Rank.PLAYER, "trust", "t");
        this.AddCommand(new HorseTrustAddCommand((HorseManager)this.Plugin));
        this.AddCommand(new HorseTrustRemoveCommand((HorseManager)this.Plugin));
        this.AddCommand(new HorseTrustListCommand((HorseManager)this.Plugin));
    }

    @Override
    protected void Help(Player caller, String[] args) {
        UtilPlayer.message((Entity)caller, " ");
        UtilPlayer.message((Entity)caller, F.main(((HorseManager)this.Plugin).getName(), "Available commands:"));
        UtilPlayer.message((Entity)caller, F.help("/horse trust list (ID)", "Display those trusted to this horse", Rank.PLAYER));
        UtilPlayer.message((Entity)caller, F.help("/horse trust add (ID) (Player)", "Trust a player to this horse", Rank.PLAYER));
        UtilPlayer.message((Entity)caller, F.help("/horse trust remove (ID) (Player)", "Remove a trusted player from this horse", Rank.PLAYER));
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, String commandLabel, String[] args) {
        ICommand command;
        String commandName;
        if (args.length == 2) {
            ArrayList<String> possibleMatches = new ArrayList<String>();
            for (ICommand command2 : this.Commands.values()) {
                if (possibleMatches.contains((String)command2.Aliases().toArray()[0])) continue;
                possibleMatches.add((String)command2.Aliases().toArray()[0]);
            }
            return this.getMatches(args[1], possibleMatches);
        }
        if (args.length > 2 && (command = (ICommand)this.Commands.get(commandName = args[1])) != null) {
            return command.onTabComplete(sender, commandLabel, args);
        }
        return null;
    }
}
