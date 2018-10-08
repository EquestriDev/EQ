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
import java.util.HashMap;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

public class AdminReplyCommand
extends CommandBase<ChatManager> {
    public AdminReplyCommand(ChatManager plugin) {
        super(plugin, Rank.TJRMOD, "adminreply", "ar");
    }

    @Override
    public void Execute(Player caller, String[] args) {
        if (args == null) {
            UtilPlayer.message((Entity)caller, F.main("Chat", "No message?"));
        } else {
            Player target = null;
            if (((ChatManager)this.Plugin).prevStaff.containsKey((Object)caller)) {
                target = ((ChatManager)this.Plugin).prevStaff.get((Object)caller);
                if (!target.isOnline()) {
                    UtilPlayer.message((Entity)caller, F.main("Chat", F.elem(target.getName()) + " is no longer online."));
                    return;
                }
            } else {
                UtilPlayer.message((Entity)caller, F.main("Chat", "You haven't messaged anyone with Admin Chat."));
            }
            String message = F.combine(args, 0, null);
            ((ChatManager)this.Plugin).message(caller, target, message, true);
        }
    }
}
