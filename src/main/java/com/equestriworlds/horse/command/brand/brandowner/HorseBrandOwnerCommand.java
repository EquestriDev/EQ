package com.equestriworlds.horse.command.brand.brandowner;

import com.equestriworlds.MiniPlugin;
import com.equestriworlds.command.ICommand;
import com.equestriworlds.command.MultiCommandBase;
import com.equestriworlds.common.Rank;
import com.equestriworlds.horse.HorseManager;
import com.equestriworlds.horse.command.brand.brandowner.HorseBrandOwnerAddCommand;
import com.equestriworlds.horse.command.brand.brandowner.HorseBrandOwnerListCommand;
import com.equestriworlds.horse.command.brand.brandowner.HorseBrandOwnerRemoveCommand;
import com.equestriworlds.util.F;
import com.equestriworlds.util.UtilPlayer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

public class HorseBrandOwnerCommand
extends MultiCommandBase<HorseManager> {
    public HorseBrandOwnerCommand(HorseManager plugin) {
        super(plugin, Rank.PLAYER, "brandowner", "bo");
        this.AddCommand(new HorseBrandOwnerAddCommand((HorseManager)this.Plugin));
        this.AddCommand(new HorseBrandOwnerRemoveCommand((HorseManager)this.Plugin));
        this.AddCommand(new HorseBrandOwnerListCommand((HorseManager)this.Plugin));
    }

    @Override
    protected void Help(Player caller, String[] args) {
        UtilPlayer.message((Entity)caller, " ");
        UtilPlayer.message((Entity)caller, F.main(((HorseManager)this.Plugin).getName(), "Available commands:"));
        UtilPlayer.message((Entity)caller, F.help("/horse brandowner list", "Display those co-owning this brand", Rank.PLAYER));
        UtilPlayer.message((Entity)caller, F.help("/horse brandowner add (Player)", "Trust a player to this brand", Rank.PLAYER));
        UtilPlayer.message((Entity)caller, F.help("/horse brandowner remove (Player)", "Remove a trusted player from this brand", Rank.PLAYER));
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, String commandLabel, String[] args) {
        ICommand command;
        String commandName;
        if (args.length == 2) {
            ArrayList<String> possibleMatches = new ArrayList<String>();
            for (ICommand command2 : this.Commands.values()) {
                if (possibleMatches.contains(command2.Aliases().toArray()[0])) continue;
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
