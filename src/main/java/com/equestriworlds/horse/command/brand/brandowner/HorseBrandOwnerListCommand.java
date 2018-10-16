package com.equestriworlds.horse.command.brand.brandowner;

import com.equestriworlds.MiniPlugin;
import com.equestriworlds.command.CommandBase;
import com.equestriworlds.common.Rank;
import com.equestriworlds.horse.HorseManager;
import com.equestriworlds.horse.branding.Brand;
import com.equestriworlds.horse.branding.BrandConfig;
import com.equestriworlds.horse.branding.BrandToken;
import com.equestriworlds.util.C;
import com.equestriworlds.util.F;
import com.equestriworlds.util.UtilPlayer;
import com.equestriworlds.util.UtilServer;
import java.util.List;
import java.util.UUID;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

public class HorseBrandOwnerListCommand
extends CommandBase<HorseManager> {
    HorseBrandOwnerListCommand(HorseManager plugin) {
        super(plugin, Rank.PLAYER, "list");
    }

    @Override
    public void Execute(Player caller, String[] args) {
        if (args != null) {
            UtilPlayer.message((Entity)caller, F.main(((HorseManager)this.Plugin).getName(), "Correct usage:"));
            UtilPlayer.message((Entity)caller, F.help("/horse brandowner list", "Display those trusted to this horse", Rank.PLAYER));
        } else {
            Brand brand = ((HorseManager)this.Plugin).brandConfig.getBrandByID(caller.getUniqueId());
            if (brand == null) {
                UtilPlayer.message((Entity)caller, F.main(((HorseManager)this.Plugin).getName(), "You do not own a brand!"));
                return;
            }
            if (brand.token.coowners.size() == 0) {
                UtilPlayer.message((Entity)caller, F.main(((HorseManager)this.Plugin).getName(), "Your brand does not have co-owners."));
                return;
            }
            UtilPlayer.message((Entity)caller, F.main(((HorseManager)this.Plugin).getName(), C.convert(brand.token.format) + " Co-Owners:"));
            for (UUID uuid : brand.token.coowners) {
                OfflinePlayer player = UtilServer.getServer().getOfflinePlayer(uuid);
                UtilPlayer.message((Entity)caller, F.count("  - " + player.getName()));
            }
        }
    }
}
