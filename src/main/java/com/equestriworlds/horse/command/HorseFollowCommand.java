package com.equestriworlds.horse.command;

import com.equestriworlds.MiniPlugin;
import com.equestriworlds.command.CommandBase;
import com.equestriworlds.common.Rank;
import com.equestriworlds.horse.HorseManager;
import com.equestriworlds.horse.config.CustomHorse;
import com.equestriworlds.horse.config.CustomHorseToken;
import com.equestriworlds.horse.config.HorseConfig;
import com.equestriworlds.util.F;
import com.equestriworlds.util.UtilMath;
import com.equestriworlds.util.UtilPlayer;
import java.util.UUID;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftAbstractHorse;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

public class HorseFollowCommand
extends CommandBase<HorseManager> {
    HorseFollowCommand(HorseManager plugin) {
        super(plugin, Rank.PLAYER, "follow");
    }

    @Override
    public void Execute(Player caller, String[] args) {
        if (args == null || args.length != 1) {
            UtilPlayer.message((Entity)caller, F.main(((HorseManager)this.Plugin).getName(), "Correct usage:"));
            UtilPlayer.message((Entity)caller, F.help("/ha follow (ID)", "Make the horse follow you", Rank.PLAYER));
        } else {
            boolean follow;
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
            if (!horse.alive()) {
                UtilPlayer.message((Entity)caller, F.main(((HorseManager)this.Plugin).getName(), "That horse is not spawned"));
                return;
            }
            if (!caller.getWorld().getName().equals(horse.horse.getWorld().getName())) {
                UtilPlayer.message((Entity)caller, F.main(((HorseManager)this.Plugin).getName(), "You are in different worlds."));
                return;
            }
            if (UtilMath.offset((Entity)caller, (Entity)horse.horse) >= 10.0) {
                UtilPlayer.message((Entity)caller, F.main(((HorseManager)this.Plugin).getName(), "You are too far apart. " + F.elem(new StringBuilder().append("(").append(UtilMath.trim(1, UtilMath.offset((Entity)caller, (Entity)horse.horse))).append(" > ").append(10.0).append(")").toString())));
                return;
            }
            UtilPlayer.message((Entity)caller, F.main(((HorseManager)this.Plugin).getName(), F.elem(horse.token.name) + " will " + ((follow = horse.toggleFollow()) ? "now" : "no longer") + " follow you."));
        }
    }
}
