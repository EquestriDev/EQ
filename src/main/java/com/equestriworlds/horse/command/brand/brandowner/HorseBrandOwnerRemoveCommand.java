package com.equestriworlds.horse.command.brand.brandowner;

import com.equestriworlds.MiniPlugin;
import com.equestriworlds.command.CommandBase;
import com.equestriworlds.common.Rank;
import com.equestriworlds.horse.HorseManager;
import com.equestriworlds.horse.branding.Brand;
import com.equestriworlds.horse.branding.BrandConfig;
import com.equestriworlds.horse.branding.BrandToken;
import com.equestriworlds.util.F;
import com.equestriworlds.util.UtilPlayer;
import java.util.List;
import java.util.UUID;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

public class HorseBrandOwnerRemoveCommand
extends CommandBase<HorseManager> {
    HorseBrandOwnerRemoveCommand(HorseManager plugin) {
        super(plugin, Rank.PLAYER, "remove");
    }

    @Override
    public void Execute(Player caller, String[] args) {
        if (args == null || args.length != 1) {
            UtilPlayer.message((Entity)caller, F.main(((HorseManager)this.Plugin).getName(), "Correct usage:"));
            UtilPlayer.message((Entity)caller, F.help("/horse brandowner remove (Player)", "Remove a player from your Brand", Rank.PLAYER));
        } else {
            OfflinePlayer player = UtilPlayer.searchOffline(caller, args[0], true);
            Brand brand = ((HorseManager)this.Plugin).brandConfig.getBrandByID(caller.getUniqueId());
            if (brand == null) {
                UtilPlayer.message((Entity)caller, F.main(((HorseManager)this.Plugin).getName(), "You do not own a brand!"));
                return;
            }
            if (player == null) {
                return;
            }
            if (player.getPlayer() == caller) {
                UtilPlayer.message((Entity)caller, F.main(((HorseManager)this.Plugin).getName(), "You cannot remove yourself from the brandowner list."));
                return;
            }
            if (!brand.token.coowners.contains(player.getUniqueId())) {
                UtilPlayer.message((Entity)caller, F.main(((HorseManager)this.Plugin).getName(), player.getName() + " is not added to the brandowner list."));
                return;
            }
            brand.token.coowners.remove(player.getUniqueId());
            UtilPlayer.message((Entity)caller, F.main(((HorseManager)this.Plugin).getName(), "You just removed " + F.elem(player.getName()) + " from your brand!"));
        }
    }
}
