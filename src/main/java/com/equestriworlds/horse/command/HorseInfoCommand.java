package com.equestriworlds.horse.command;

import com.equestriworlds.MiniPlugin;
import com.equestriworlds.command.CommandBase;
import com.equestriworlds.common.Rank;
import com.equestriworlds.horse.HorseManager;
import com.equestriworlds.recharge.Recharge;
import com.equestriworlds.util.F;
import com.equestriworlds.util.UtilPlayer;
import java.util.HashSet;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

public class HorseInfoCommand
extends CommandBase<HorseManager> {
    HorseInfoCommand(HorseManager plugin) {
        super(plugin, Rank.PLAYER, "information", "info");
    }

    @Override
    public void Execute(Player caller, String[] args) {
        if (((HorseManager)this.Plugin).info.contains((Object)caller)) {
            ((HorseManager)this.Plugin).info.remove((Object)caller);
        } else {
            ((HorseManager)this.Plugin).info.add(caller);
        }
        UtilPlayer.message((Entity)caller, F.main(((HorseManager)this.Plugin).getName(), "Info Mode: " + F.oo(((HorseManager)this.Plugin).info.contains((Object)caller))));
        if (Recharge.Instance.use(caller, "InfoMessage", 900000L, false, false)) {
            UtilPlayer.message((Entity)caller, F.main(((HorseManager)this.Plugin).getName(), "To get information about the horse, Left-Click on it."));
        }
        if (((HorseManager)this.Plugin).condition.contains((Object)caller)) {
            ((HorseManager)this.Plugin).condition.remove((Object)caller);
            UtilPlayer.message((Entity)caller, F.main(((HorseManager)this.Plugin).getName(), "As you have toggled info on, condition has been " + F.oo(((HorseManager)this.Plugin).condition.contains((Object)caller))));
        }
    }
}
