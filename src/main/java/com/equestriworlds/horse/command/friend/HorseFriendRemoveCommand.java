package com.equestriworlds.horse.command.friend;

import com.equestriworlds.MiniPlugin;
import com.equestriworlds.command.CommandBase;
import com.equestriworlds.common.Rank;
import com.equestriworlds.horse.HorseManager;
import com.equestriworlds.horse.config.CustomHorse;
import com.equestriworlds.horse.config.CustomHorseToken;
import com.equestriworlds.horse.config.HorseConfig;
import com.equestriworlds.horse.event.HorseFriendRemoveEvent;
import com.equestriworlds.util.F;
import com.equestriworlds.util.UtilPlayer;
import java.util.List;
import java.util.UUID;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.plugin.PluginManager;

public class HorseFriendRemoveCommand
extends CommandBase<HorseManager> {
    public HorseFriendRemoveCommand(HorseManager plugin) {
        super(plugin, Rank.PLAYER, "remove");
    }

    @Override
    public void Execute(Player caller, String[] args) {
        if (args == null || args.length != 2) {
            UtilPlayer.message((Entity)caller, F.main(((HorseManager)this.Plugin).getName(), "Correct usage:"));
            UtilPlayer.message((Entity)caller, F.help("/horse friend remove (ID) (Player)", "remove a player from the friend list", Rank.PLAYER));
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
                UtilPlayer.message((Entity)caller, F.main(((HorseManager)this.Plugin).getName(), "You cannot remove yourself from the friend list."));
                return;
            }
            if (!horse.token.friends.contains(player.getUniqueId())) {
                UtilPlayer.message((Entity)caller, F.main(((HorseManager)this.Plugin).getName(), F.elem(player.getName()) + " is not in the horse's friend list."));
                return;
            }
            horse.token.friends.remove(player.getUniqueId());
            UtilPlayer.message((Entity)caller, F.main(((HorseManager)this.Plugin).getName(), "You just removed " + F.elem(player.getName()) + " from the horse's friend list."));
            HorseFriendRemoveEvent event = new HorseFriendRemoveEvent(horse, player);
            ((HorseManager)this.Plugin).getPluginManager().callEvent((Event)event);
        }
    }
}
