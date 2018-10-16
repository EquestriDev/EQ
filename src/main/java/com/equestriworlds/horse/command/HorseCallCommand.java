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
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

public class HorseCallCommand
extends CommandBase<HorseManager> {
    public HorseCallCommand(HorseManager plugin) {
        super(plugin, Rank.PLAYER, "here", "h", "call");
    }

    @Override
    public void Execute(Player caller, String[] args) {
        if (caller.getLocation().getWorld().getName().equals("Survival")) {
            UtilPlayer.message((Entity)caller, F.main(((HorseManager)this.Plugin).getName(), "You cannot use this in Survival world."));
            return;
        }
        if (args == null || args.length != 1) {
            UtilPlayer.message((Entity)caller, F.main(((HorseManager)this.Plugin).getName(), "Correct usage:"));
            UtilPlayer.message((Entity)caller, F.help("/horse call (ID)", "Summon your horse", Rank.PLAYER));
        } else {
            String id = args[0];
            CustomHorse horse = ((HorseManager)this.Plugin).config.getHorseById(id);
            if (horse == null) {
                UtilPlayer.message((Entity)caller, F.main(((HorseManager)this.Plugin).getName(), "Horse under the ID " + F.elem(new StringBuilder().append(id).append("").toString()) + " doesn't exist."));
                return;
            }
            for (UUID uuid : horse.token.trusted) {
                if (!caller.getUniqueId().equals(uuid)) continue;
                if (horse.token.age == null) {
                    horse.setAge(System.currentTimeMillis());
                }
                horse.remove();
                horse.spawn(caller.getLocation());
                UtilPlayer.message((Entity)caller, F.main(((HorseManager)this.Plugin).getName(), "You teleported " + F.elem(horse.token.name) + " to yourself."));
                return;
            }
            if (horse.token.owner == null || !caller.getUniqueId().equals(horse.token.owner)) {
                UtilPlayer.message((Entity)caller, F.main(((HorseManager)this.Plugin).getName(), "That horse does not belong to you."));
                return;
            }
            if (horse.token.age == null) {
                horse.setAge(System.currentTimeMillis());
            }
            horse.summonToOwner();
            UtilPlayer.message((Entity)caller, F.main(((HorseManager)this.Plugin).getName(), "You teleported " + F.elem(horse.token.name) + " to yourself."));
        }
    }
}
