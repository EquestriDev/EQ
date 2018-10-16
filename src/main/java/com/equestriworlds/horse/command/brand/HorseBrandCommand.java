package com.equestriworlds.horse.command.brand;

import com.equestriworlds.MiniPlugin;
import com.equestriworlds.command.CommandBase;
import com.equestriworlds.common.Rank;
import com.equestriworlds.horse.HorseManager;
import com.equestriworlds.horse.branding.Brand;
import com.equestriworlds.horse.branding.BrandConfig;
import com.equestriworlds.horse.branding.BrandToken;
import com.equestriworlds.horse.breeding.breedingStages;
import com.equestriworlds.horse.config.CustomHorse;
import com.equestriworlds.horse.config.CustomHorseToken;
import com.equestriworlds.horse.config.HorseConfig;
import com.equestriworlds.util.C;
import com.equestriworlds.util.F;
import com.equestriworlds.util.UtilPlayer;
import java.util.UUID;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.OfflinePlayer;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftAbstractHorse;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

public class HorseBrandCommand
extends CommandBase<HorseManager> {
    public HorseBrandCommand(HorseManager plugin) {
        super(plugin, Rank.PLAYER, "brand");
    }

    @Override
    public void Execute(Player caller, String[] args) {
        if (args == null || args.length != 1) {
            UtilPlayer.message((Entity)caller, F.main(((HorseManager)this.Plugin).getName(), "Correct usage:"));
            UtilPlayer.message((Entity)caller, F.help("/horse brand (ID)", "Brands the horse with your branding", Rank.PLAYER));
            UtilPlayer.message((Entity)caller, F.help("/horse registerbrand (ID)", "Create your own brand", Rank.PLAYER));
            UtilPlayer.message((Entity)caller, F.help("/horse brandowner", "Allow a player to use your brand", Rank.PLAYER));
            UtilPlayer.message((Entity)caller, F.help("/horse brandlist", "List all brands", Rank.PLAYER));
        } else {
            CustomHorse horse = ((HorseManager)this.Plugin).config.getHorseById(args[0]);
            if (horse == null) {
                UtilPlayer.message((Entity)caller, F.main(((HorseManager)this.Plugin).getName(), "Horse under the ID " + F.elem(new StringBuilder().append(args[0]).append("").toString()) + " doesn't exist."));
                return;
            }
            if (horse.token.owner == null || !caller.getUniqueId().equals(horse.token.owner)) {
                UtilPlayer.message((Entity)caller, F.main(((HorseManager)this.Plugin).getName(), "That horse does not belong to you."));
                return;
            }
            Brand brand = ((HorseManager)this.Plugin).brandConfig.getBrandByID(caller.getUniqueId());
            if (brand == null) {
                UtilPlayer.message((Entity)caller, F.main(((HorseManager)this.Plugin).getName(), "You do not own a brand!"));
                return;
            }
            if (horse.token.brand != null) {
                UtilPlayer.message((Entity)caller, F.main(((HorseManager)this.Plugin).getName(), "Horse under the ID " + F.elem(new StringBuilder().append(args[0]).append("").toString()) + " is already branded!"));
                return;
            }
            if (!((HorseManager)this.Plugin).eco.has((OfflinePlayer)caller, 10000.0)) {
                UtilPlayer.message((Entity)caller, F.main(((HorseManager)this.Plugin).getName(), C.convert("&4You have insufficient funds.")));
                return;
            }
            breedingStages breedingStage = horse.token.stage;
            if (breedingStage != breedingStages.FOALSTAGE2 && breedingStage != breedingStages.FOAL) {
                UtilPlayer.message((Entity)caller, F.main(((HorseManager)this.Plugin).getName(), "You may only brand a horse in the foal stage!"));
                return;
            }
            ((HorseManager)this.Plugin).eco.withdrawPlayer((OfflinePlayer)caller, 10000.0);
            horse.setBrand(brand);
            UtilPlayer.message((Entity)caller, F.main(((HorseManager)this.Plugin).getName(), horse.token.name + C.convert(new StringBuilder().append("&7 has been branded with ").append(brand.token.format).toString())));
            if (horse.alive()) {
                horse.horse.setCustomName((horse.token.sale ? new StringBuilder().append(C.cGold).append(C.Bold).append("For Sale ").append(C.Reset).toString() : "") + (horse.token.brand != null ? C.convert(new StringBuilder().append(horse.token.brand.token.format).append("&r ").toString()) : "") + horse.token.name + " " + horse.g());
            }
        }
    }
}
