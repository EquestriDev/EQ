/*
 * Decompiled with CFR 0_133.
 * 
 * Could not load the following classes:
 *  org.bukkit.Location
 *  org.bukkit.Sound
 *  org.bukkit.entity.Entity
 *  org.bukkit.entity.Player
 */
package com.equestriworlds.chat.command;

import com.equestriworlds.MiniPlugin;
import com.equestriworlds.chat.ChatManager;
import com.equestriworlds.command.CommandBase;
import com.equestriworlds.common.Rank;
import com.equestriworlds.util.C;
import com.equestriworlds.util.F;
import com.equestriworlds.util.FileUtil;
import com.equestriworlds.util.UtilPlayer;
import com.equestriworlds.util.UtilServer;
import java.util.ArrayList;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

public class AdminChatCommand
extends CommandBase<ChatManager> {
    public AdminChatCommand(ChatManager plugin) {
        super(plugin, Rank.PLAYER, "adminchat", "ac", "admin", "a", "sc", "staffchat");
    }

    @Override
    public void Execute(Player caller, String[] args) {
        if (args == null) {
            if (Rank.Has(caller, Rank.TJRMOD, false)) {
                if (((ChatManager)this.Plugin).staffChat.contains((Object)caller)) {
                    ((ChatManager)this.Plugin).staffChat.remove((Object)caller);
                } else {
                    ((ChatManager)this.Plugin).staffChat.add(caller);
                }
                UtilPlayer.message((Entity)caller, F.main("Staff Chat", F.ed(((ChatManager)this.Plugin).staffChat.contains((Object)caller))));
                return;
            }
            UtilPlayer.message((Entity)caller, F.main("Chat", "No message?"));
        } else {
            String message = F.combine(args, 0, null);
            boolean staffOnline = false;
            FileUtil.logToFile(message, caller.getName());
            for (Player staff : UtilServer.getPlayers()) {
                if (!Rank.Has(staff, Rank.TJRMOD, false)) continue;
                UtilPlayer.message((Entity)staff, C.convert("&8[&dSC&8] &5" + Rank.getRank((Player)caller).Name + " &d" + caller.getName() + " &7\u00bb &5" + message));
                staffOnline = true;
                staff.playSound(staff.getLocation(), Sound.BLOCK_NOTE_PLING, 0.5f, 2.0f);
            }
            if (staffOnline) {
                if (!Rank.Has(caller, Rank.TJRMOD, false)) {
                    UtilPlayer.message((Entity)caller, C.convert("&8[&dSC&8] &5" + Rank.getRank((Player)caller).Name + " &d" + caller.getName() + " &7\u00bb &5" + message));
                }
            } else {
                UtilPlayer.message((Entity)caller, F.main("Staff Chat", "There are no staff members online"));
            }
        }
    }
}
