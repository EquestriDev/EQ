package com.equestriworlds.horse.command.trust;

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

public class HorseTrustAddCommand
extends CommandBase<HorseManager> {
    public HorseTrustAddCommand(HorseManager plugin) {
        super(plugin, Rank.PLAYER, "add");
    }

    @Override
    public void Execute(Player caller, String[] args) {
        if (args == null || args.length != 2) {
            UtilPlayer.message((Entity)caller, F.main(((HorseManager)this.Plugin).getName(), "Correct usage:"));
            UtilPlayer.message((Entity)caller, F.help("/horse trust add (ID) (Player)", "Add a player to the friend list", Rank.PLAYER));
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
            if (player == null) {
                return;
            }
            if (player.getPlayer() == caller && horse.token.owner == caller.getUniqueId()) {
                UtilPlayer.message((Entity)caller, F.main(((HorseManager)this.Plugin).getName(), "You cannot add yourself to the trust list."));
                return;
            }
            horse.token.trusted.add(player.getUniqueId());
            UtilPlayer.message((Entity)caller, F.main(((HorseManager)this.Plugin).getName(), "You just added " + F.elem(player.getName()) + " to the horse's trust list."));
        }
    }
}
