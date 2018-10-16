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

public class HorseAdminToggleSaleCommand
extends CommandBase<HorseManager> {
    public HorseAdminToggleSaleCommand(HorseManager plugin) {
        super(plugin, Rank.JRMOD, "togglesale", "ts");
    }

    @Override
    public void Execute(Player caller, String[] args) {
        if (args == null || args.length != 1) {
            UtilPlayer.message((Entity)caller, F.main(((HorseManager)this.Plugin).getName(), "Correct usage:"));
            UtilPlayer.message((Entity)caller, F.help("/ha togglesale (ID)", "Toggle horse's sale mode", Rank.JRMOD));
        } else {
            boolean sale;
            String id = args[0];
            CustomHorse horse = ((HorseManager)this.Plugin).config.getHorseById(id);
            if (horse == null) {
                UtilPlayer.message((Entity)caller, F.main(((HorseManager)this.Plugin).getName(), "Horse under the ID " + F.elem(new StringBuilder().append(id).append("").toString()) + " doesn't exist."));
                return;
            }
            if (!horse.token.free) {
                UtilPlayer.message((Entity)caller, F.main(((HorseManager)this.Plugin).getName(), "The horse has not been freed by the owner."));
                return;
            }
            UtilPlayer.message((Entity)caller, F.main(((HorseManager)this.Plugin).getName(), F.elem(horse.token.name) + " is " + ((sale = horse.toggleSale()) ? "now" : "no longer") + " for sale!"));
        }
    }
}
