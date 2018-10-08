/*
 * Decompiled with CFR 0_133.
 * 
 * Could not load the following classes:
 *  org.bukkit.ChatColor
 *  org.bukkit.entity.Player
 */
package com.equestriworlds.util;

import com.equestriworlds.json.JsonMessage;
import com.equestriworlds.util.C;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class UtilTextBottom {
    public static /* varargs */ void display(String text, Player ... players) {
        JsonMessage msg = new JsonMessage(text);
        msg.send(JsonMessage.MessageType.ABOVE_HOTBAR, players);
    }

    public static /* varargs */ void displayProgress(double amount, Player ... players) {
        UtilTextBottom.displayProgress(null, amount, null, players);
    }

    public static /* varargs */ void displayProgress(String prefix, double amount, Player ... players) {
        UtilTextBottom.displayProgress(prefix, amount, null, players);
    }

    public static /* varargs */ void displayProgress(String prefix, double amount, String suffix, Player ... players) {
        UtilTextBottom.displayProgress(prefix, amount, suffix, false, players);
    }

    public static /* varargs */ void displayProgress(String prefix, double amount, String suffix, boolean progressDirectionSwap, Player ... players) {
        if (progressDirectionSwap) {
            amount = 1.0 - amount;
        }
        int bars = 24;
        String progressBar = C.cGreen + "";
        boolean colorChange = false;
        for (int i = 0; i < bars; ++i) {
            if (!colorChange && (double)((float)i / (float)bars) >= amount) {
                progressBar = progressBar + C.cRed;
                colorChange = true;
            }
            progressBar = progressBar + "\u258c";
        }
        for (Player player : players) {
            UtilTextBottom.display((prefix == null ? "" : new StringBuilder().append(prefix).append((Object)ChatColor.RESET).append(" ").toString()) + progressBar + (suffix == null ? "" : new StringBuilder().append((Object)ChatColor.RESET).append(" ").append(suffix).toString()), players);
        }
    }
}
