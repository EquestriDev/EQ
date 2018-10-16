package com.equestriworlds.horse.command.brand.admin;

import com.equestriworlds.MiniPlugin;
import com.equestriworlds.command.CommandBase;
import com.equestriworlds.common.Rank;
import com.equestriworlds.horse.HorseManager;
import com.equestriworlds.horse.branding.Brand;
import com.equestriworlds.horse.branding.BrandConfig;
import com.equestriworlds.horse.branding.BrandToken;
import com.equestriworlds.horse.config.CustomHorse;
import com.equestriworlds.horse.config.CustomHorseToken;
import com.equestriworlds.horse.config.HorseConfig;
import com.equestriworlds.util.C;
import com.equestriworlds.util.F;
import com.equestriworlds.util.UtilPlayer;
import java.util.Collection;
import java.util.HashMap;
import java.util.UUID;
import org.bukkit.OfflinePlayer;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftAbstractHorse;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

public class HorseDeleteBrandCommand
extends CommandBase<HorseManager> {
    public HorseDeleteBrandCommand(HorseManager plugin) {
        super(plugin, Rank.ADMIN, "deletebrand");
    }

    @Override
    public void Execute(Player caller, String[] args) {
        if (args == null || args.length != 1) {
            UtilPlayer.message((Entity)caller, F.main(((HorseManager)this.Plugin).getName(), "Correct usage:"));
            UtilPlayer.message((Entity)caller, F.help("/horse deletebrand (Player)", "Deletes a player's brand", Rank.ADMIN));
        } else {
            OfflinePlayer player = UtilPlayer.searchOffline(caller, args[0], true);
            if (player == null) {
                UtilPlayer.message((Entity)caller, F.main(((HorseManager)this.Plugin).getName(), F.name(args[0]) + " is not online."));
                return;
            }
            Brand brand = ((HorseManager)this.Plugin).brandConfig.getBrandByID(player.getUniqueId());
            if (brand == null) {
                UtilPlayer.message((Entity)caller, F.main(((HorseManager)this.Plugin).getName(), F.name(args[0]) + " doesn't have a brand."));
                return;
            }
            for (CustomHorse horse : ((HorseManager)this.Plugin).config.horses.values()) {
                if (horse.token.brand == null || !horse.token.brand.id.equals(player.getUniqueId())) continue;
                horse.setBrand(null);
                if (!horse.alive()) continue;
                if (horse.token.brand != null) {
                    horse.horse.setCustomName((horse.token.sale ? new StringBuilder().append(C.cGold).append(C.Bold).append("For Sale ").append(C.Reset).toString() : "") + C.convert(new StringBuilder().append(horse.token.brand.token.format).append("&r ").append(horse.token.name).toString()) + " " + horse.g());
                    continue;
                }
                horse.horse.setCustomName((horse.token.sale ? new StringBuilder().append(C.cGold).append(C.Bold).append("For Sale ").append(C.Reset).toString() : "") + C.convert(horse.token.name) + " " + horse.g());
            }
            UtilPlayer.message((Entity)caller, F.main(((HorseManager)this.Plugin).getName(), C.convert(brand.token.format) + " has been deleted."));
            ((HorseManager)this.Plugin).brandConfig.delete(player.getUniqueId());
        }
    }
}
