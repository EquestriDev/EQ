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
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftAbstractHorse;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

public class HorseAdminAgeCommand
extends CommandBase<HorseManager> {
    public HorseAdminAgeCommand(HorseManager plugin) {
        super(plugin, Rank.JRMOD, "age");
    }

    @Override
    public void Execute(Player caller, String[] args) {
        if (args == null || args.length != 1) {
            UtilPlayer.message((Entity)caller, F.main(((HorseManager)this.Plugin).getName(), "Correct usage:"));
            UtilPlayer.message((Entity)caller, F.help("/ha age (ID)", "Toggle age", Rank.JRMOD));
        } else {
            String id = args[0];
            CustomHorse horse = ((HorseManager)this.Plugin).config.getHorseById(id);
            if (horse == null) {
                UtilPlayer.message((Entity)caller, F.main(((HorseManager)this.Plugin).getName(), "Horse under the ID " + F.elem(new StringBuilder().append(id).append("").toString()) + " doesn't exist."));
                return;
            }
            boolean age = horse.token.adult = !horse.token.adult;
            if (horse.alive()) {
                if (age) {
                    horse.horse.setAdult();
                } else {
                    horse.horse.setBaby();
                }
            }
            UtilPlayer.message((Entity)caller, F.main(((HorseManager)this.Plugin).getName(), "Change the age of " + F.elem(horse.token.name) + " to " + F.elem(age ? "Adult" : "Baby") + "."));
        }
    }
}
