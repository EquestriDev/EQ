/*
 * Decompiled with CFR 0_133.
 * 
 * Could not load the following classes:
 *  org.bukkit.command.CommandSender
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
import com.equestriworlds.horse.config.Gender;
import com.equestriworlds.horse.config.HorseConfig;
import com.equestriworlds.util.C;
import com.equestriworlds.util.F;
import com.equestriworlds.util.UtilPlayer;
import java.util.List;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

public class HorseAdminGenderCommand
extends CommandBase<HorseManager> {
    public HorseAdminGenderCommand(HorseManager plugin) {
        super(plugin, Rank.TJRMOD, "setgender", "gender");
    }

    @Override
    public void Execute(Player caller, String[] args) {
        if (args == null || args.length != 2) {
            UtilPlayer.message((Entity)caller, F.main(((HorseManager)this.Plugin).getName(), "Correct usage:"));
            UtilPlayer.message((Entity)caller, F.help("/ha setgender (ID) (Gender)", "Set the gender", Rank.TJRMOD));
        } else {
            String id = args[0];
            CustomHorse horse = ((HorseManager)this.Plugin).config.getHorseById(id);
            if (horse == null) {
                UtilPlayer.message((Entity)caller, F.main(((HorseManager)this.Plugin).getName(), "Horse under the ID " + F.elem(new StringBuilder().append(id).append("").toString()) + " doesn't exist."));
                return;
            }
            Gender gender = null;
            try {
                gender = Gender.valueOf(args[1].toUpperCase());
            }
            catch (NumberFormatException e) {
                UtilPlayer.message((Entity)caller, F.main(((HorseManager)this.Plugin).getName(), C.mError + "Invalid gender."));
                return;
            }
            horse.changeGender(gender);
            UtilPlayer.message((Entity)caller, F.main(((HorseManager)this.Plugin).getName(), "You changed the gender of " + F.elem(horse.token.name) + " to " + F.time(gender.name)));
        }
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, String commandLabel, String[] args) {
        if (args.length != 3) {
            return null;
        }
        String lastArg = args[2];
        return this.getMatches(lastArg, Gender.values());
    }
}
