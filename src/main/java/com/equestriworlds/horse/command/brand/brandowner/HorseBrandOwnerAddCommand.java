/*
 * Decompiled with CFR 0_133.
 * 
 * Could not load the following classes:
 *  org.bukkit.OfflinePlayer
 *  org.bukkit.entity.Entity
 *  org.bukkit.entity.Player
 */
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

public class HorseBrandOwnerAddCommand
extends CommandBase<HorseManager> {
    HorseBrandOwnerAddCommand(HorseManager plugin) {
        super(plugin, Rank.PLAYER, "add");
    }

    @Override
    public void Execute(Player caller, String[] args) {
        if (args == null || args.length != 1) {
            UtilPlayer.message((Entity)caller, F.main(((HorseManager)this.Plugin).getName(), "Correct usage:"));
            UtilPlayer.message((Entity)caller, F.help("/horse brandowner add (Player)", "Add a player to your Brand", Rank.PLAYER));
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
            if (brand.token.coowners.contains(player.getUniqueId())) {
                UtilPlayer.message((Entity)caller, F.main(((HorseManager)this.Plugin).getName(), player.getName() + " is already added to the brandowner list."));
                return;
            }
            Brand playersBrand = ((HorseManager)this.Plugin).brandConfig.getBrandByID(player.getUniqueId());
            if (playersBrand != null) {
                UtilPlayer.message((Entity)caller, F.main(((HorseManager)this.Plugin).getName(), F.elem(player.getName()) + " already has a brand."));
                return;
            }
            if (player.getPlayer() == caller) {
                UtilPlayer.message((Entity)caller, F.main(((HorseManager)this.Plugin).getName(), "You cannot add yourself to the brandowner list."));
                return;
            }
            brand.token.coowners.add(player.getUniqueId());
            UtilPlayer.message((Entity)caller, F.main(((HorseManager)this.Plugin).getName(), "You just added " + F.elem(player.getName()) + " to your brand!"));
        }
    }
}
