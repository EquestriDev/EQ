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
import com.equestriworlds.util.UtilServer;
import java.util.List;
import java.util.UUID;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

public class HorseTrustListCommand
extends CommandBase<HorseManager> {
    HorseTrustListCommand(HorseManager plugin) {
        super(plugin, Rank.PLAYER, "list");
    }

    @Override
    public void Execute(Player caller, String[] args) {
        if (args == null) {
            UtilPlayer.message((Entity)caller, F.main(((HorseManager)this.Plugin).getName(), "Correct usage:"));
            UtilPlayer.message((Entity)caller, F.help("/horse trust list (ID)", "Display those trusted to this horse", Rank.PLAYER));
        } else {
            String id = args[0];
            CustomHorse horse = ((HorseManager)this.Plugin).config.getHorseById(id);
            if (horse == null) {
                UtilPlayer.message((Entity)caller, F.main(((HorseManager)this.Plugin).getName(), "Horse under the ID " + F.elem(new StringBuilder().append(id).append("").toString()) + " doesn't exist."));
                return;
            }
            if (horse.token.owner == null || !caller.getUniqueId().equals(horse.token.owner)) {
                UtilPlayer.message((Entity)caller, F.main(((HorseManager)this.Plugin).getName(), "That horse does not belong to you."));
            }
            UtilPlayer.message((Entity)caller, F.main(((HorseManager)this.Plugin).getName(), F.elem(new StringBuilder().append(horse.token.name).append("'s").toString()) + " trusted:"));
            for (UUID uuid : horse.token.trusted) {
                OfflinePlayer player = UtilServer.getServer().getOfflinePlayer(uuid);
                UtilPlayer.message((Entity)caller, F.count("  - " + player.getName()));
            }
        }
    }
}
