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
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftEntity;
import org.bukkit.entity.AbstractHorse;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

public class HorseAdminFreeCommand
extends CommandBase<HorseManager> {
    public HorseAdminFreeCommand(HorseManager plugin) {
        super(plugin, Rank.JRMOD, "free");
    }

    @Override
    public void Execute(Player caller, String[] args) {
        Entity ehorse = caller.getVehicle();
        if (ehorse == null || !(ehorse instanceof AbstractHorse)) {
            UtilPlayer.message((Entity)caller, F.main(((HorseManager)this.Plugin).getName(), "You are not sitting on a horse."));
            return;
        }
        CustomHorse horse = ((HorseManager)this.Plugin).config.getHorseByHorse((CraftEntity)ehorse);
        if (horse == null) {
            UtilPlayer.message((Entity)caller, F.main(((HorseManager)this.Plugin).getName(), "This horse has not been claimed."));
            return;
        }
        if (horse.token.free) {
            UtilPlayer.message((Entity)caller, F.main(((HorseManager)this.Plugin).getName(), "This horse is already free."));
            return;
        }
        horse.free();
        UtilPlayer.message((Entity)caller, F.main(((HorseManager)this.Plugin).getName(), "You just freed " + F.elem(horse.token.name) + ". Bye bye, horsie!"));
    }
}
