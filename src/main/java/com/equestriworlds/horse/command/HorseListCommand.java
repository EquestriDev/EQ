/*
 * Decompiled with CFR 0_133.
 * 
 * Could not load the following classes:
 *  org.bukkit.OfflinePlayer
 *  org.bukkit.entity.Player
 */
package com.equestriworlds.horse.command;

import com.equestriworlds.MiniPlugin;
import com.equestriworlds.account.CoreClientManager;
import com.equestriworlds.command.CommandBase;
import com.equestriworlds.common.Rank;
import com.equestriworlds.horse.HorseManager;
import com.equestriworlds.horse.gui.list.HorseListMenu;
import com.equestriworlds.util.UtilPlayer;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

public class HorseListCommand
extends CommandBase<HorseManager> {
    HorseListCommand(HorseManager plugin) {
        super(plugin, Rank.PLAYER, "list", "l");
    }

    @Override
    public void Execute(Player caller, String[] args) {
        String playerView = caller.getName();
        if (args != null) {
            OfflinePlayer player = UtilPlayer.searchOffline(caller, args[0], true);
            if (player == null) {
                return;
            }
            playerView = player.getName();
        }
        HorseListMenu listMenu = new HorseListMenu((HorseManager)this.Plugin, ((HorseManager)this.Plugin).clientManager, playerView);
        listMenu.attemptShopOpen(caller);
    }
}
