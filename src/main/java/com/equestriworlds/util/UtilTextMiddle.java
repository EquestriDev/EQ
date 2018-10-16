package com.equestriworlds.util;

import com.equestriworlds.util.C;
import com.equestriworlds.util.UtilServer;
import org.bukkit.entity.Player;

public class UtilTextMiddle {
    public static void display(String title, String subtitle, Player player) {
        UtilTextMiddle.display(title, subtitle, 20, 60, 20, player);
    }

    public static void display(String title, String subtitle, int stay, Player player) {
        UtilTextMiddle.display(title, subtitle, 20, stay, 20, player);
    }

    public static void display(String title, String subtitle, int fadeIn, int stay, int fadeOut, Player player) {
        player.sendTitle(title, subtitle, fadeIn, stay, fadeOut);
    }

    public static void displayAll(String title, String subtitle) {
        UtilTextMiddle.displayAll(title, subtitle, 20, 60, 20);
    }

    public static void displayAll(String title, String subtitle, int stay) {
        UtilTextMiddle.displayAll(title, subtitle, 20, stay, 20);
    }

    public static void displayAll(String title, String subtitle, int fadeIn, int stay, int fadeOut) {
        for (Player player : UtilServer.getPlayers()) {
            player.sendTitle(title, subtitle, fadeIn, stay, fadeOut);
        }
    }

    public static String progress(float exp) {
        String out = "";
        for (int i = 0; i < 40; ++i) {
            float cur = (float)i * 0.025f;
            out = cur < exp ? out + C.cGreen + C.Bold + "|" : out + C.cGray + C.Bold + "|";
        }
        return out;
    }
}
