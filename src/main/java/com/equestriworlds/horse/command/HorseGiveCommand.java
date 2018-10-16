package com.equestriworlds.horse.command;

import com.equestriworlds.MiniPlugin;
import com.equestriworlds.command.CommandBase;
import com.equestriworlds.common.Rank;
import com.equestriworlds.horse.HorseManager;
import com.equestriworlds.horse.config.CustomHorse;
import com.equestriworlds.horse.config.CustomHorseToken;
import com.equestriworlds.horse.config.HorseConfig;
import com.equestriworlds.util.F;
import com.equestriworlds.util.UtilPlayer;
import java.util.List;
import java.util.UUID;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

public class HorseGiveCommand
extends CommandBase<HorseManager> {
    HorseGiveCommand(HorseManager plugin) {
        super(plugin, Rank.PLAYER, "give", "setowner", "giveowner");
    }

    @Override
    public void Execute(Player caller, String[] args) {
        if (args == null || args.length != 2) {
            UtilPlayer.message((Entity)caller, F.main(((HorseManager)this.Plugin).getName(), "Correct usage:"));
            UtilPlayer.message((Entity)caller, F.help("/horse give (ID) (Player)", "Give the horse to player", Rank.PLAYER));
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
            OfflinePlayer player = UtilPlayer.searchOffline(caller, args[1], true);
            horse.changeOwner(player.getUniqueId());
            if (horse.token.trusted.contains(player.getUniqueId())) {
                horse.token.trusted.remove(player.getUniqueId());
            }
            UtilPlayer.message((Entity)caller, F.main(((HorseManager)this.Plugin).getName(), "You just gave your horse with the id of " + F.elem(new StringBuilder().append("#").append(horse.id).toString()) + " to " + F.elem(player.getName()) + "."));
            if (player.isOnline()) {
                UtilPlayer.message((Entity)player.getPlayer(), F.main(((HorseManager)this.Plugin).getName(), F.elem(caller.getName()) + " just gave you their horse with the id of " + F.elem(new StringBuilder().append("#").append(horse.id).toString()) + "."));
            }
        }
    }
}
