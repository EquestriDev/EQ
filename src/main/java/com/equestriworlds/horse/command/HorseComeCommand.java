/*
 * Decompiled with CFR 0_133.
 * 
 * Could not load the following classes:
 *  org.bukkit.Location
 *  org.bukkit.World
 *  org.bukkit.craftbukkit.v1_12_R1.entity.CraftAbstractHorse
 *  org.bukkit.entity.Entity
 *  org.bukkit.entity.Player
 */
package com.equestriworlds.horse.command;

import com.equestriworlds.MiniPlugin;
import com.equestriworlds.command.CommandBase;
import com.equestriworlds.common.Rank;
import com.equestriworlds.horse.HorseManager;
import com.equestriworlds.horse.config.CustomHorse;
import com.equestriworlds.horse.config.CustomHorseToken;
import com.equestriworlds.horse.config.HorseConfig;
import com.equestriworlds.util.F;
import com.equestriworlds.util.UtilEnt;
import com.equestriworlds.util.UtilMath;
import com.equestriworlds.util.UtilPlayer;
import java.util.UUID;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftAbstractHorse;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

public class HorseComeCommand
extends CommandBase<HorseManager> {
    public HorseComeCommand(HorseManager plugin) {
        super(plugin, Rank.PLAYER, "come");
    }

    @Override
    public void Execute(Player caller, String[] args) {
        if (args == null || args.length != 1) {
            UtilPlayer.message((Entity)caller, F.main(((HorseManager)this.Plugin).getName(), "Correct usage:"));
            UtilPlayer.message((Entity)caller, F.help("/horse come (ID)", "Call your horse", Rank.PLAYER));
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
            if (!horse.alive()) {
                UtilPlayer.message((Entity)caller, F.main(((HorseManager)this.Plugin).getName(), "That horse is not spawned"));
                return;
            }
            if (!caller.getWorld().getName().equals(horse.horse.getWorld().getName())) {
                UtilPlayer.message((Entity)caller, F.main(((HorseManager)this.Plugin).getName(), "You are in different worlds."));
                return;
            }
            if (UtilMath.offset((Entity)caller, (Entity)horse.horse) >= 50.0) {
                UtilPlayer.message((Entity)caller, F.main(((HorseManager)this.Plugin).getName(), "You are too far apart. " + F.elem(new StringBuilder().append("(").append(UtilMath.trim(1, UtilMath.offset((Entity)caller, (Entity)horse.horse))).append(" > ").append(50.0).append(")").toString())));
                return;
            }
            UtilEnt.CreatureMoveFast((Entity)horse.horse, caller.getLocation(), 1.0f);
            UtilPlayer.message((Entity)caller, F.main(((HorseManager)this.Plugin).getName(), "You called " + F.elem(horse.token.name) + " to yourself."));
        }
    }
}
