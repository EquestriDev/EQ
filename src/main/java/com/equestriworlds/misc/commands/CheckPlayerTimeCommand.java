/*
 * Decompiled with CFR 0_133.
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.OfflinePlayer
 *  org.bukkit.entity.Entity
 *  org.bukkit.entity.Player
 */
package com.equestriworlds.misc.commands;

import com.equestriworlds.command.MultiCommandBase;
import com.equestriworlds.common.Rank;
import com.equestriworlds.horse.HorseManager;
import com.equestriworlds.util.F;
import com.equestriworlds.util.UtilPlayer;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

public class CheckPlayerTimeCommand
extends MultiCommandBase<HorseManager> {
    public CheckPlayerTimeCommand(HorseManager plugin) {
        super(plugin, Rank.DEV, "checktime", "jointime", "joined");
    }

    @Override
    protected void Help(Player caller, String[] args) {
        if (args == null) {
            UtilPlayer.message((Entity)caller, F.main("Time Checker", "Invalid arguments! Usage: /checktime <player>"));
            return;
        }
        if (args.length != 1) {
            UtilPlayer.message((Entity)caller, F.main("Time Checker", "Invalid arguments! Usage: /checktime <player>"));
            return;
        }
        Player player = Bukkit.getServer().getPlayer(args[0]);
        if (player == null) {
            UtilPlayer.message((Entity)caller, F.main("Time Checker", player.getName() + " isn't online!"));
            return;
        }
        OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer((UUID)player.getUniqueId());
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date date = new Date(offlinePlayer.getFirstPlayed());
        UtilPlayer.message((Entity)caller, F.main("Time Checker", formatter.format(date)));
    }
}
