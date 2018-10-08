/*
 * Decompiled with CFR 0_133.
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.entity.Entity
 *  org.bukkit.entity.Player
 */
package com.equestriworlds.horse.command.admin;

import com.equestriworlds.command.CommandBase;
import com.equestriworlds.common.Rank;
import com.equestriworlds.horse.HorseManager;
import com.equestriworlds.util.F;
import com.equestriworlds.util.UtilPlayer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

public class HorseAdminDismountAllCommand
extends CommandBase<HorseManager> {
    public HorseAdminDismountAllCommand(HorseManager plugin) {
        super(plugin, Rank.DEV, "forcedismount", "dismountall", "dismount");
    }

    @Override
    public void Execute(Player caller, String[] args) {
        if (args == null || args.length != 1) {
            UtilPlayer.message((Entity)caller, F.main("Force Dismount", "Correct usage:"));
            UtilPlayer.message((Entity)caller, F.help("/ha dismount (Player)", "Force a player to dismount.", Rank.DEV));
            return;
        }
        if (args.length == 1) {
            if (args[0].equalsIgnoreCase("*")) {
                int amount = 0;
                for (Player player : Bukkit.getOnlinePlayers()) {
                    if (!player.isInsideVehicle()) continue;
                    player.getVehicle().eject();
                    ++amount;
                    UtilPlayer.message((Entity)player, F.main("Dismount", "You have been dismounted by staff."));
                }
                UtilPlayer.message((Entity)caller, F.main("Force Dismount", String.valueOf(amount) + " have been dismounted."));
            } else {
                Player target = Bukkit.getServer().getPlayer(args[0]);
                if (target == null) {
                    UtilPlayer.message((Entity)caller, F.main("Force Dismount", args[0] + " is not online."));
                    return;
                }
                if (target.getVehicle() == null) {
                    UtilPlayer.message((Entity)caller, F.main("Force Dismount", target.getName() + " is not mounted."));
                    return;
                }
                target.getVehicle().eject();
                UtilPlayer.message((Entity)caller, F.main("Force Dismount", target.getName() + " has been dismounted."));
            }
        }
    }
}
