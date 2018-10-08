/*
 * Decompiled with CFR 0_133.
 * 
 * Could not load the following classes:
 *  org.bukkit.ChatColor
 *  org.bukkit.entity.Entity
 *  org.bukkit.entity.Player
 *  ru.tehkode.permissions.bukkit.PermissionsEx
 */
package com.equestriworlds.common;

import com.equestriworlds.util.C;
import com.equestriworlds.util.UtilPlayer;
import com.equestriworlds.util.UtilServer;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.ChatColor;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import ru.tehkode.permissions.bukkit.PermissionsEx;

public enum Rank {
    OWNER("Owner", "ew.owner", ChatColor.AQUA, "a"),
    COOWNER("Co-Owner", "ew.coowner", ChatColor.DARK_AQUA, "b"),
    DEV("Developer", "ew.dev", ChatColor.GOLD, "c"),
    HEADADMIN("Head-Admin", "ew.headadmin", ChatColor.YELLOW, "d"),
    ADMIN("Admin", "ew.admin", ChatColor.RED, "e"),
    HEADMOD("Head-Mod", "ew.headmod", ChatColor.DARK_PURPLE, "f"),
    MOD("Mod", "ew.mod", ChatColor.LIGHT_PURPLE, "g"),
    JRMOD("Jr. Mod", "ew.jrmod", ChatColor.BLUE, "h"),
    TJRMOD("Trial", "ew.tjrmod", ChatColor.BLUE, "i"),
    VET("Vet", "ew.vet", ChatColor.AQUA, "j"),
    MENTOR("Mentor", "ew.mentor", ChatColor.DARK_AQUA, "k"),
    VIPPP("VIP++", "ew.vippp", ChatColor.AQUA, "l"),
    VIPP("VIP+", "ew.vipp", ChatColor.AQUA, "m"),
    VIP("VIP", "ew.vip", ChatColor.AQUA, "n"),
    Level9("Player", "ew.level9", ChatColor.AQUA, "o"),
    Level8("Player", "ew.level8", ChatColor.AQUA, "p"),
    Level7("Player", "ew.level7", ChatColor.AQUA, "q"),
    Level6("Player", "ew.level6", ChatColor.AQUA, "r"),
    Level5("Player", "ew.level5", ChatColor.AQUA, "s"),
    Level4("Player", "ew.level4", ChatColor.AQUA, "t"),
    Level3("Player", "ew.level3", ChatColor.AQUA, "u"),
    Level2("Player", "ew.level2", ChatColor.AQUA, "v"),
    Level1("Player", "ew.level1", ChatColor.AQUA, "w"),
    PLAYER("Player", "ew.player", ChatColor.AQUA, "x");
    
    public String Name;
    public String Perm;
    private ChatColor Color;
    public String Index;

    private Rank(String name, String perm, ChatColor color, String index) {
        this.Name = name;
        this.Perm = perm;
        this.Color = color;
        this.Index = index;
    }

    public boolean Has(Rank rank) {
        return Rank.Has(null, rank, false);
    }

    public static boolean Has(Player player, Rank rank, boolean inform) {
        return Rank.Has(player, rank, null, inform);
    }

    public static boolean Has(Player player, Rank rank, Rank[] specific, boolean inform) {
        if (specific != null) {
            for (Rank curRank : specific) {
                if (Rank.getRank(player).compareTo(curRank) != 0) continue;
                return true;
            }
        }
        if (Rank.getRank(player).compareTo(rank) <= 0) {
            return true;
        }
        if (inform) {
            UtilPlayer.message((Entity)player, C.mHead + "Permissions> " + C.mBody + "For this you need Rank [" + rank.GetTag(false, true) + C.mBody + "].");
        }
        return false;
    }

    public String GetTag(boolean bold, boolean uppercase) {
        if (this.Name.equalsIgnoreCase("PLAYER")) {
            return "";
        }
        String name = this.Name;
        if (uppercase) {
            name = this.Name.toUpperCase();
        }
        if (bold) {
            return (Object)this.Color + C.Bold + name;
        }
        return (Object)this.Color + name;
    }

    public ChatColor GetColor() {
        return this.Color;
    }

    public Rank parse(String rank) throws Exception {
        for (Rank ranks : Rank.values()) {
            if (!rank.equalsIgnoreCase(ranks.Name)) continue;
            return ranks;
        }
        return null;
    }

    public static Rank getRank(Player player) {
        for (Rank ranks : Rank.values()) {
            if (!PermissionsEx.getUser((Player)player).has(ranks.Perm)) continue;
            return ranks;
        }
        return PLAYER;
    }

    public static List<String> getPlayerNamesOfRank(Rank rank) {
        ArrayList<String> names = new ArrayList<String>();
        for (Player player : UtilServer.getPlayers()) {
            if (!Rank.getRank(player).equals((Object)rank)) continue;
            names.add(player.getName());
        }
        return names;
    }
}
