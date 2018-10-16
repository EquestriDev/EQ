package com.equestriworlds.horse.command.friend;

import com.equestriworlds.MiniPlugin;
import com.equestriworlds.command.ICommand;
import com.equestriworlds.command.MultiCommandBase;
import com.equestriworlds.common.Rank;
import com.equestriworlds.horse.HorseManager;
import com.equestriworlds.horse.command.friend.HorseFriendAddCommand;
import com.equestriworlds.horse.command.friend.HorseFriendListCommand;
import com.equestriworlds.horse.command.friend.HorseFriendRemoveCommand;
import com.equestriworlds.util.F;
import com.equestriworlds.util.UtilPlayer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

public class HorseFriendCommand
extends MultiCommandBase<HorseManager> {
    public HorseFriendCommand(HorseManager plugin) {
        super(plugin, Rank.PLAYER, "friend", "f");
        this.AddCommand(new HorseFriendAddCommand((HorseManager)this.Plugin));
        this.AddCommand(new HorseFriendRemoveCommand((HorseManager)this.Plugin));
        this.AddCommand(new HorseFriendListCommand((HorseManager)this.Plugin));
    }

    @Override
    protected void Help(Player caller, String[] args) {
        UtilPlayer.message((Entity)caller, " ");
        UtilPlayer.message((Entity)caller, F.main(((HorseManager)this.Plugin).getName(), "Available commands:"));
        UtilPlayer.message((Entity)caller, F.help("/horse friend list (ID)", "Display the friends added to this horse", Rank.PLAYER));
        UtilPlayer.message((Entity)caller, F.help("/horse friend add (ID) (Player)", "Summon your horse", Rank.PLAYER));
        UtilPlayer.message((Entity)caller, F.help("/horse friend remove (ID) (Player)", "Teleport to your horse", Rank.PLAYER));
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
