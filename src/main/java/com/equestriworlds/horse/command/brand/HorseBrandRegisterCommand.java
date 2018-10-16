package com.equestriworlds.horse.command.brand;

import com.equestriworlds.MiniPlugin;
import com.equestriworlds.command.CommandBase;
import com.equestriworlds.common.Rank;
import com.equestriworlds.horse.HorseManager;
import com.equestriworlds.horse.branding.Brand;
import com.equestriworlds.horse.branding.BrandConfig;
import com.equestriworlds.util.C;
import com.equestriworlds.util.F;
import com.equestriworlds.util.UtilPlayer;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

public class HorseBrandRegisterCommand
extends CommandBase<HorseManager> {
    private final Pattern STRIP_COLOR_PATTERN = Pattern.compile("(?i)" + String.valueOf('&') + "[0-9A-FK-OR]");

    public HorseBrandRegisterCommand(HorseManager plugin) {
        super(plugin, Rank.PLAYER, "registerbrand", "brandregister");
    }

    @Override
    public void Execute(Player caller, String[] args) {
        if (args == null || args.length != 1) {
            UtilPlayer.message((Entity)caller, F.main(((HorseManager)this.Plugin).getName(), "Correct usage:"));
            UtilPlayer.message((Entity)caller, F.help("/horse registerbrand (Brand)", "Register the specific brand", Rank.JRMOD));
        } else {
            Brand brand = ((HorseManager)this.Plugin).brandConfig.getBrandByID(caller.getUniqueId());
            if (brand != null) {
                UtilPlayer.message((Entity)caller, F.main(((HorseManager)this.Plugin).getName(), "You already own a brand!"));
                return;
            }
            if (((HorseManager)this.Plugin).brandConfig.getBrandByFormat(this.stripColor(args[0])) != null) {
                UtilPlayer.message((Entity)caller, F.main(((HorseManager)this.Plugin).getName(), F.name(args[0]) + " is already a brand."));
                return;
            }
            int characters = this.stripColor(args[0]).length();
            if (characters > 6) {
                UtilPlayer.message((Entity)caller, F.main(((HorseManager)this.Plugin).getName(), F.name(this.stripColor(args[0])) + " has too many characters."));
                return;
            }
            if (!((HorseManager)this.Plugin).eco.has((OfflinePlayer)caller, 250000.0)) {
                UtilPlayer.message((Entity)caller, F.main(((HorseManager)this.Plugin).getName(), C.convert("&4You have insufficient funds.")));
                return;
            }
            ((HorseManager)this.Plugin).eco.withdrawPlayer((OfflinePlayer)caller, 250000.0);
            ((HorseManager)this.Plugin).claimBrand(caller.getUniqueId(), args[0]);
            UtilPlayer.message((Entity)caller, F.main(((HorseManager)this.Plugin).getName(), "You have registered the brand " + C.convert(args[0])));
        }
    }

    private String stripColor(String input) {
        return input == null ? null : this.STRIP_COLOR_PATTERN.matcher(input).replaceAll("");
    }
}
