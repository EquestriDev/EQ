/*
 * Decompiled with CFR 0_133.
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.Location
 *  org.bukkit.OfflinePlayer
 *  org.bukkit.Server
 *  org.bukkit.Sound
 *  org.bukkit.entity.Entity
 *  org.bukkit.entity.Player
 */
package com.equestriworlds.util;

import com.equestriworlds.util.C;
import com.equestriworlds.util.UtilPlayer;
import java.util.Collection;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.Server;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

public class UtilServer {
    public static Player[] getPlayers() {
        return UtilServer.getServer().getOnlinePlayers().toArray(new Player[0]);
    }

    public static OfflinePlayer[] getOfflinePlayers() {
        return UtilServer.getServer().getOfflinePlayers();
    }

    public static Server getServer() {
        return Bukkit.getServer();
    }

    public static void broadcast(String message) {
        UtilServer.broadcast(message, false);
    }

    public static void broadcast(String message, boolean sound) {
        for (Player cur : UtilServer.getPlayers()) {
            UtilPlayer.message((Entity)cur, message);
            if (!sound) continue;
            cur.playSound(cur.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 2.0f, 0.0f);
        }
    }

    public static void broadcastSpecial(String event, String message) {
        for (Player cur : UtilServer.getPlayers()) {
            UtilPlayer.message((Entity)cur, C.cAqua + C.Bold + event);
            UtilPlayer.message((Entity)cur, message);
            cur.playSound(cur.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 2.0f, 0.0f);
            cur.playSound(cur.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 2.0f, 0.0f);
        }
    }

    public static void broadcast(String sender, String message) {
        UtilServer.broadcast(C.Bold + sender + " " + message);
    }

    public static void broadcastMagic(String sender, String message) {
        UtilServer.broadcast(C.cDGreen + C.Scramble + message);
    }

    public static double getFilledPercent() {
        return (double)UtilServer.getPlayers().length / (double)UtilServer.getServer().getMaxPlayers();
    }
}
