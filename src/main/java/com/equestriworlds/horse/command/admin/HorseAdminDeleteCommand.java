package com.equestriworlds.horse.command.admin;

import com.equestriworlds.MiniPlugin;
import com.equestriworlds.command.CommandBase;
import com.equestriworlds.common.Rank;
import com.equestriworlds.horse.HorseManager;
import com.equestriworlds.horse.config.CustomHorse;
import com.equestriworlds.horse.config.HorseConfig;
import com.equestriworlds.util.F;
import com.equestriworlds.util.UtilPlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

public class HorseAdminDeleteCommand
extends CommandBase<HorseManager> {
    public HorseAdminDeleteCommand(HorseManager plugin) {
        super(plugin, Rank.MOD, "delete");
    }

    @Override
    public void Execute(Player caller, String[] args) {
        if (args == null || args.length != 1) {
            UtilPlayer.message((Entity)caller, F.main(((HorseManager)this.Plugin).getName(), "Correct usage:"));
            UtilPlayer.message((Entity)caller, F.help("/ha delete (ID)", "Deletes a horse completely from the server.", Rank.MOD));
            UtilPlayer.message((Entity)caller, F.attention("WARNING", "This will delete the horse PERMANENTLY!!!"));
        } else {
            String id = args[0];
            CustomHorse horse = ((HorseManager)this.Plugin).config.getHorseById(id);
            if (horse == null) {
                UtilPlayer.message((Entity)caller, F.main(((HorseManager)this.Plugin).getName(), "Horse under the ID " + F.elem(new StringBuilder().append(id).append("").toString()) + " doesn't exist."));
                return;
            }
            ((HorseManager)this.Plugin).config.delete(id);
            UtilPlayer.message((Entity)caller, F.main(((HorseManager)this.Plugin).getName(), F.elem(id) + " has been permanently deleted."));
        }
    }
}
