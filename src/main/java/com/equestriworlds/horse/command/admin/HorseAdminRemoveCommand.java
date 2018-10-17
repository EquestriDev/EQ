package com.equestriworlds.horse.command.admin;

import com.equestriworlds.MiniPlugin;
import com.equestriworlds.command.CommandBase;
import com.equestriworlds.common.Rank;
import com.equestriworlds.horse.HorseManager;
import com.equestriworlds.horse.config.CustomHorse;
import com.equestriworlds.horse.config.CustomHorseToken;
import com.equestriworlds.horse.config.HorseConfig;
import com.equestriworlds.recharge.Recharge;
import com.equestriworlds.util.F;
import com.equestriworlds.util.UtilPlayer;
import com.equestriworlds.util.UtilServer;
import java.util.HashSet;
import java.util.List;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftEntity;
import org.bukkit.entity.AbstractHorse;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

/**
 * /ha remove
 */
public class HorseAdminRemoveCommand
extends CommandBase<HorseManager> {
    HorseAdminRemoveCommand(HorseManager plugin) {
        super(plugin, Rank.JRMOD, "remove", "rem");
    }

    @Override
    public void Execute(Player caller, String[] args) {
        if (args == null) {
            if (((HorseManager)this.Plugin).removeMode.contains((Object)caller)) {
                ((HorseManager)this.Plugin).removeMode.remove((Object)caller);
            } else {
                ((HorseManager)this.Plugin).removeMode.add(caller);
            }
            UtilPlayer.message((Entity)caller, F.main(((HorseManager)this.Plugin).getName(), "Remove Mode: " + F.oo(((HorseManager)this.Plugin).removeMode.contains((Object)caller))));
            if (Recharge.Instance.use(caller, "DespawnMessage", 900000L, false, false)) {
                UtilPlayer.message((Entity)caller, F.main(((HorseManager)this.Plugin).getName(), "To despawn a horse, Crouch and Left Click on a horse."));
            }
        } else if (args[0].equalsIgnoreCase("all")) {
            int i = 0;
            for (World world : UtilServer.getServer().getWorlds()) {
                for (Entity entity : world.getEntities()) {
                    if (!(entity instanceof AbstractHorse)) continue;
                    CustomHorse horse = ((HorseManager)this.Plugin).config.getHorseByHorse((CraftEntity)entity);
                    if (horse != null) {
                        // empty if block
                    }
                    entity.remove();
                    ++i;
                }
            }
            UtilPlayer.message((Entity)caller, F.main(((HorseManager)this.Plugin).getName(), "Removed " + F.elem(new StringBuilder().append(i).append(" Horses").toString()) + "."));
        } else {
            String id = args[0];
            CustomHorse horse = ((HorseManager)this.Plugin).config.getHorseById(id);
            if (horse == null) {
                UtilPlayer.message((Entity)caller, F.main(((HorseManager)this.Plugin).getName(), "Horse under the ID " + F.elem(new StringBuilder().append(id).append("").toString()) + " doesn't exist."));
                return;
            }
            if (!horse.alive()) {
                UtilPlayer.message((Entity)caller, F.main(((HorseManager)this.Plugin).getName(), "Horse " + F.elem(new StringBuilder().append(id).append("").toString()) + " is not spawned."));
                return;
            }
            horse.remove();
            UtilPlayer.message((Entity)caller, F.main(((HorseManager)this.Plugin).getName(), "Removed " + F.elem(horse.token.name) + "."));
        }
    }
}
