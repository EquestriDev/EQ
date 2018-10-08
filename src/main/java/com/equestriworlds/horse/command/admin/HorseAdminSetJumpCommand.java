/*
 * Decompiled with CFR 0_133.
 * 
 * Could not load the following classes:
 *  org.bukkit.entity.Entity
 *  org.bukkit.entity.Player
 */
package com.equestriworlds.horse.command.admin;

import com.equestriworlds.MiniPlugin;
import com.equestriworlds.command.CommandBase;
import com.equestriworlds.common.Rank;
import com.equestriworlds.horse.HorseManager;
import com.equestriworlds.horse.config.CustomHorse;
import com.equestriworlds.horse.config.CustomHorseToken;
import com.equestriworlds.horse.config.HorseConfig;
import com.equestriworlds.util.F;
import com.equestriworlds.util.UtilPlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

public class HorseAdminSetJumpCommand
extends CommandBase<HorseManager> {
    public HorseAdminSetJumpCommand(HorseManager plugin) {
        super(plugin, Rank.TJRMOD, "setjump", "sj");
    }

    @Override
    public void Execute(Player caller, String[] args) {
        if (args == null || args.length != 2) {
            UtilPlayer.message((Entity)caller, F.main(((HorseManager)this.Plugin).getName(), "Correct usage:"));
            UtilPlayer.message((Entity)caller, F.help("/ha setjump (ID) (Jump)", "Changes the jump height of the horse.", Rank.TJRMOD));
        } else {
            String id = args[0];
            CustomHorse horse = ((HorseManager)this.Plugin).config.getHorseById(id);
            if (horse == null) {
                UtilPlayer.message((Entity)caller, F.main(((HorseManager)this.Plugin).getName(), "Horse under the ID " + F.elem(new StringBuilder().append(id).append("").toString()) + " doesn't exist."));
                return;
            }
            double jump = 0.0;
            try {
                jump = Double.parseDouble(args[1]);
            }
            catch (NumberFormatException e) {
                UtilPlayer.message((Entity)caller, F.main(((HorseManager)this.Plugin).getName(), "The jump height has to be an decimal number."));
                return;
            }
            horse.setJump(jump);
            UtilPlayer.message((Entity)caller, F.main(((HorseManager)this.Plugin).getName(), "You changed the jump height of " + F.elem(horse.token.name) + " to " + F.time(new StringBuilder().append(jump).append("").toString())));
        }
    }
}
