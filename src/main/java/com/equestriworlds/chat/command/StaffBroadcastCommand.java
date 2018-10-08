/*
 * Decompiled with CFR 0_133.
 * 
 * Could not load the following classes:
 *  org.bukkit.ChatColor
 *  org.bukkit.entity.Entity
 *  org.bukkit.entity.Player
 */
package com.equestriworlds.chat.command;

import com.equestriworlds.MiniPlugin;
import com.equestriworlds.chat.ChatManager;
import com.equestriworlds.command.CommandBase;
import com.equestriworlds.common.Rank;
import com.equestriworlds.util.F;
import com.equestriworlds.util.UtilPlayer;
import org.bukkit.ChatColor;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

public class StaffBroadcastCommand
extends CommandBase<ChatManager> {
    public StaffBroadcastCommand(ChatManager plugin) {
        super(plugin, Rank.JRMOD, "broadcast", "bc");
    }

    @Override
    public void Execute(Player caller, String[] args) {
        if (args == null) {
            UtilPlayer.message((Entity)caller, F.main("Chat", "Provide message"));
        } else {
            String message = F.combine(args, 0, null);
            ((ChatManager)this.Plugin).broadcast(caller, ChatColor.translateAlternateColorCodes((char)'&', (String)message));
        }
    }
}
