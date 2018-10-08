/*
 * Decompiled with CFR 0_133.
 * 
 * Could not load the following classes:
 *  org.bukkit.entity.Entity
 *  org.bukkit.entity.Player
 */
package com.equestriworlds.horse.command.brand.admin;

import com.equestriworlds.MiniPlugin;
import com.equestriworlds.command.CommandBase;
import com.equestriworlds.common.Rank;
import com.equestriworlds.horse.HorseManager;
import com.equestriworlds.util.F;
import com.equestriworlds.util.UtilPlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

public class HorseBrandAdminCommand
extends CommandBase<HorseManager> {
    public HorseBrandAdminCommand(HorseManager plugin) {
        super(plugin, Rank.MOD, "brand");
    }

    @Override
    public void Execute(Player caller, String[] args) {
        if (args == null || args.length != 0) {
            UtilPlayer.message((Entity)caller, F.main(((HorseManager)this.Plugin).getName(), "Usage:"));
            UtilPlayer.message((Entity)caller, F.help("/ha brand (ID)", "Display this menu", Rank.MOD));
            UtilPlayer.message((Entity)caller, F.help("/ha unbrand (ID)", "Removes brand from a horse", Rank.MOD));
            UtilPlayer.message((Entity)caller, F.help("/ha deletebrand (Player)", "Deletes a player's brand", Rank.ADMIN));
        } else {
            UtilPlayer.message((Entity)caller, F.main(((HorseManager)this.Plugin).getName(), "Correct usage:"));
            UtilPlayer.message((Entity)caller, F.help("/ha brand (ID)", "Display this menu", Rank.MOD));
            UtilPlayer.message((Entity)caller, F.help("/ha unbrand (ID)", "Removes brand from a horse", Rank.MOD));
            UtilPlayer.message((Entity)caller, F.help("/ha deletebrand (Player)", "Deletes a player's brand", Rank.ADMIN));
        }
    }
}
