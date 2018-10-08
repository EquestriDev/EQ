/*
 * Decompiled with CFR 0_133.
 * 
 * Could not load the following classes:
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
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

public class AdminMessageCommand
extends CommandBase<ChatManager> {
    public AdminMessageCommand(ChatManager plugin) {
        super(plugin, Rank.TJRMOD, "adminmessage", "am");
    }

    @Override
    public void Execute(Player caller, String[] args) {
        if (args == null || args.length < 2) {
            UtilPlayer.message((Entity)caller, F.main("Chat", "Provide player and message"));
        } else {
            String message = F.combine(args, 1, null);
            Player target = UtilPlayer.searchOnline(caller, args[0], true);
            if (target == null) {
                return;
            }
            ((ChatManager)this.Plugin).message(caller, target, message, true);
        }
    }
}
