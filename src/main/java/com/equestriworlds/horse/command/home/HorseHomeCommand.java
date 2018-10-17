package com.equestriworlds.horse.command.home;

import com.equestriworlds.MiniPlugin;
import com.equestriworlds.command.CommandBase;
import com.equestriworlds.common.Rank;
import com.equestriworlds.horse.HorseManager;
import com.equestriworlds.horse.config.CustomHorse;
import com.equestriworlds.horse.config.CustomHorseToken;
import com.equestriworlds.horse.config.HorseConfig;
import com.equestriworlds.util.F;
import com.equestriworlds.util.UtilPlayer;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.UUID;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

/**
 * Home system for horses.
 * This is the overview; other commands go along with it.
 * See HorseCommand
 */
public class HorseHomeCommand
extends CommandBase<HorseManager> {
    public HorseHomeCommand(HorseManager plugin) {
        super(plugin, Rank.PLAYER, "home");
    }

    @Override
    public void Execute(Player caller, String[] args) {
        if (args == null) {
            UtilPlayer.message((Entity)caller, F.main(((HorseManager)this.Plugin).getName(), "Available Commands:"));
            UtilPlayer.message((Entity)caller, F.help("/horse home (ID) [Name]", "Teleport your horse to the specified home.", Rank.PLAYER));
            UtilPlayer.message((Entity)caller, F.help("/horse addhome (ID) (Name)", "Add a home.", Rank.PLAYER));
            UtilPlayer.message((Entity)caller, F.help("/horse delhome (ID) [Name]", "Delete a home.", Rank.PLAYER));
            UtilPlayer.message((Entity)caller, F.help("/horse listhome (ID)", "List all homes for a horse.", Rank.PLAYER));
            UtilPlayer.message((Entity)caller, F.help("/horse homeall", "Teleport all horses home.", Rank.PLAYER));
        } else if (args.length == 1) {
            String id = args[0];
            CustomHorse horse = ((HorseManager)this.Plugin).config.getHorseById(id);
            if (horse == null) {
                UtilPlayer.message((Entity)caller, F.main(((HorseManager)this.Plugin).getName(), "Horse under the ID " + F.elem(new StringBuilder().append(id).append("").toString()) + " doesn't exist."));
                return;
            }
            if (horse.token.owner == null || !caller.getUniqueId().equals(horse.token.owner)) {
                UtilPlayer.message((Entity)caller, F.main(((HorseManager)this.Plugin).getName(), "That horse does not belong to you."));
                return;
            }
            if (horse.token.homes.isEmpty()) {
                UtilPlayer.message((Entity)caller, F.main(((HorseManager)this.Plugin).getName(), "This horse doesn't have any homes set."));
                return;
            }
            Collection<Location> locs = horse.token.homes.values();
            Location loc = locs.iterator().next();
            horse.spawn(loc);
            UtilPlayer.message((Entity)caller, F.main(((HorseManager)this.Plugin).getName(), "Successfully teleported " + F.elem(horse.token.name) + " home."));
        } else {
            String id = args[0];
            CustomHorse horse = ((HorseManager)this.Plugin).config.getHorseById(id);
            if (horse == null) {
                UtilPlayer.message((Entity)caller, F.main(((HorseManager)this.Plugin).getName(), "Horse under the ID " + F.elem(new StringBuilder().append(id).append("").toString()) + " doesn't exist."));
                return;
            }
            if (horse.token.owner == null || !caller.getUniqueId().equals(horse.token.owner)) {
                UtilPlayer.message((Entity)caller, F.main(((HorseManager)this.Plugin).getName(), "That horse does not belong to you."));
                return;
            }
            if (horse.token.homes.isEmpty()) {
                UtilPlayer.message((Entity)caller, F.main(((HorseManager)this.Plugin).getName(), "This horse doesn't have any homes set."));
                return;
            }
            if (!horse.token.homes.containsKey(args[1])) {
                UtilPlayer.message((Entity)caller, F.main(((HorseManager)this.Plugin).getName(), "This home doesn't exist for this horse."));
                return;
            }
            Location loc = horse.token.homes.get(args[1]);
            horse.spawn(loc);
            UtilPlayer.message((Entity)caller, F.main(((HorseManager)this.Plugin).getName(), "Successfully teleported " + F.elem(horse.token.name) + " to " + F.elem(args[1]) + "."));
        }
    }
}
