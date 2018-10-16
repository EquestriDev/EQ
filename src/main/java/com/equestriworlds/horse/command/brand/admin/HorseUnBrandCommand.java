package com.equestriworlds.horse.command.brand.admin;

import com.equestriworlds.MiniPlugin;
import com.equestriworlds.command.CommandBase;
import com.equestriworlds.common.Rank;
import com.equestriworlds.horse.HorseManager;
import com.equestriworlds.horse.branding.Brand;
import com.equestriworlds.horse.branding.BrandToken;
import com.equestriworlds.horse.config.CustomHorse;
import com.equestriworlds.horse.config.CustomHorseToken;
import com.equestriworlds.horse.config.HorseConfig;
import com.equestriworlds.util.C;
import com.equestriworlds.util.F;
import com.equestriworlds.util.UtilPlayer;
import java.util.UUID;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftAbstractHorse;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

public class HorseUnBrandCommand
extends CommandBase<HorseManager> {
    public HorseUnBrandCommand(HorseManager plugin) {
        super(plugin, Rank.MOD, "unbrand", "removebrand");
    }

    @Override
    public void Execute(Player caller, String[] args) {
        if (args == null || args.length != 1) {
            UtilPlayer.message((Entity)caller, F.main(((HorseManager)this.Plugin).getName(), "Correct usage:"));
            UtilPlayer.message((Entity)caller, F.help("/horse unbrand (ID)", "Removes the branding from the horse", Rank.MOD));
        } else {
            CustomHorse horse = ((HorseManager)this.Plugin).config.getHorseById(args[0]);
            if (horse == null) {
                UtilPlayer.message((Entity)caller, F.main(((HorseManager)this.Plugin).getName(), "Horse under the ID " + F.elem(new StringBuilder().append(args[0]).append("").toString()) + " doesn't exist."));
                return;
            }
            if (horse.token.brand == null) {
                UtilPlayer.message((Entity)caller, F.main(((HorseManager)this.Plugin).getName(), "Horse under the ID " + F.elem(new StringBuilder().append(args[0]).append("").toString()) + " doesn't have a brand."));
                return;
            }
            Player player = Bukkit.getPlayer((UUID)horse.token.owner);
            if (player == null) {
                UtilPlayer.message((Entity)caller, F.main(((HorseManager)this.Plugin).getName(), F.name(player.getName() + " is not online")));
                return;
            }
            if (!((HorseManager)this.Plugin).eco.has((OfflinePlayer)player, 20000.0)) {
                UtilPlayer.message((Entity)caller, F.main(((HorseManager)this.Plugin).getName(), C.convert(player.getName() + " &4has insufficient funds.")));
                return;
            }
            ((HorseManager)this.Plugin).eco.withdrawPlayer((OfflinePlayer)player, 20000.0);
            horse.setBrand(null);
            UtilPlayer.message((Entity)caller, F.main(((HorseManager)this.Plugin).getName(), horse.token.name + C.convert("&7's brand has been removed.")));
            if (horse.alive()) {
                horse.horse.setCustomName((horse.token.sale ? new StringBuilder().append(C.cGold).append(C.Bold).append("For Sale ").append(C.Reset).toString() : "") + (horse.token.brand != null ? C.convert(new StringBuilder().append(horse.token.brand.token.format).append("&r ").toString()) : "") + horse.token.name + " " + horse.g());
            }
        }
    }
}
