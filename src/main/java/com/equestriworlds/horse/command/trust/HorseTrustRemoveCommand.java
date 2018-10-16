package com.equestriworlds.horse.command.trust;

import com.equestriworlds.MiniPlugin;
import com.equestriworlds.command.CommandBase;
import com.equestriworlds.common.Rank;
import com.equestriworlds.horse.HorseManager;
import com.equestriworlds.horse.config.CustomHorse;
import com.equestriworlds.horse.config.CustomHorseToken;
import com.equestriworlds.horse.config.HorseConfig;
import com.equestriworlds.horse.event.HorseTrustRemoveEvent;
import com.equestriworlds.util.F;
import com.equestriworlds.util.UtilPlayer;
import java.util.List;
import java.util.UUID;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.plugin.PluginManager;

public class HorseTrustRemoveCommand
extends CommandBase<HorseManager> {
    public HorseTrustRemoveCommand(HorseManager plugin) {
        super(plugin, Rank.PLAYER, "remove");
    }

    @Override
    public void Execute(Player caller, String[] args) {
        if (args == null || args.length != 2) {
            UtilPlayer.message((Entity)caller, F.main(((HorseManager)this.Plugin).getName(), "Correct usage:"));
            UtilPlayer.message((Entity)caller, F.help("/horse trust remove (ID) (Player)", "Remove a trusted player from this horse", Rank.PLAYER));
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
            if (player.getPlayer() == caller) {
                UtilPlayer.message((Entity)caller, F.main(((HorseManager)this.Plugin).getName(), "You cannot remove yourself from the trust list."));
                return;
            }
            if (!horse.token.trusted.contains(player.getUniqueId())) {
                UtilPlayer.message((Entity)caller, F.main(((HorseManager)this.Plugin).getName(), F.elem(player.getName()) + " is not in the horse's trust list."));
                return;
            }
            horse.token.trusted.remove(player.getUniqueId());
            UtilPlayer.message((Entity)caller, F.main(((HorseManager)this.Plugin).getName(), "You just removed " + F.elem(player.getName()) + " from the horse's trust list."));
            HorseTrustRemoveEvent event = new HorseTrustRemoveEvent(horse, player);
            ((HorseManager)this.Plugin).getPluginManager().callEvent((Event)event);
        }
    }
}
