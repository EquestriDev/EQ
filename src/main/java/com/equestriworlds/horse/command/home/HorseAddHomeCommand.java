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
import java.util.HashMap;
import java.util.UUID;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

public class HorseAddHomeCommand
extends CommandBase<HorseManager> {
    public HorseAddHomeCommand(HorseManager plugin) {
        super(plugin, Rank.PLAYER, "addhome", "homeadd", "sethome", "homeset");
    }

    @Override
    public void Execute(Player caller, String[] args) {
        if (args == null || args.length != 2) {
            UtilPlayer.message((Entity)caller, F.main(((HorseManager)this.Plugin).getName(), "Correct usage:"));
            UtilPlayer.message((Entity)caller, F.help("/horse addhome (ID) (Name)", "Add a home for your horse", Rank.PLAYER));
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
            if (horse.token.homes.containsKey(args[1])) {
                UtilPlayer.message((Entity)caller, F.main(((HorseManager)this.Plugin).getName(), "Overridden a home for " + F.elem(horse.token.name) + " under the name " + F.elem(args[1])));
            } else {
                UtilPlayer.message((Entity)caller, F.main(((HorseManager)this.Plugin).getName(), "Added a home for " + F.elem(horse.token.name) + " under the name " + F.elem(args[1])));
            }
            horse.token.homes.put(args[1], caller.getLocation());
        }
    }
}
