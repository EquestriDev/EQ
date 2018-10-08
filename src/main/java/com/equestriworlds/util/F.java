/*
 * Decompiled with CFR 0_133.
 * 
 * Could not load the following classes:
 *  org.bukkit.ChatColor
 */
package com.equestriworlds.util;

import com.equestriworlds.common.Rank;
import com.equestriworlds.util.C;
import org.bukkit.ChatColor;

public class F {
    public static String main(String module, String body) {
        return C.mHead + module + (Object)ChatColor.DARK_AQUA + " \u00bb " + C.mBody + body;
    }

    public static String error(String module, String body) {
        return C.mHead + module + " \u00bb " + C.mError + body;
    }

    public static String tute(String sender, String body) {
        return C.cGold + sender + " \u00bb " + C.cWhite + body;
    }

    public static String te(String message) {
        return C.cYellow + message + C.cWhite;
    }

    public static String game(String elem) {
        return C.mGame + elem + C.mBody;
    }

    public static String ta(String message) {
        return C.cGreen + message + C.cWhite;
    }

    public static String ts(String message) {
        return C.cGold + message + C.cWhite;
    }

    public static String elem(String elem) {
        return C.mElem + elem + (Object)ChatColor.RESET + C.mBody;
    }

    public static String name(String elem) {
        return C.mElem + elem + C.mBody;
    }

    public static String count(String elem) {
        return C.mCount + elem + C.mBody;
    }

    public static String item(String elem) {
        return C.mItem + elem + C.mBody;
    }

    public static String link(String elem) {
        return C.mLink + elem + C.mBody;
    }

    public static String skill(String elem) {
        return C.mSkill + elem + C.mBody;
    }

    public static String skill(String a, String b) {
        return C.cYellow + " " + C.cGreen + b + C.mBody;
    }

    public static String time(String elem) {
        return C.mTime + elem + C.mBody;
    }

    public static String desc(String head, String body) {
        return C.descHead + head + (Object)ChatColor.GRAY + ": " + C.descBody + body;
    }

    public static String desc2(String head, String body) {
        return C.descHead + head + C.descBody2 + body;
    }

    public static String wField(String field, String data) {
        return C.wFrame + "[" + C.wField + field + C.wFrame + "] " + C.mBody + data + " ";
    }

    public static String help(String cmd, String body, Rank rank) {
        return (Object)rank.GetColor() + cmd + " " + C.mBody + body + " " + (rank.equals((Object)Rank.PLAYER) ? "" : F.rank(rank));
    }

    public static String rank(Rank rank) {
        if (rank == Rank.PLAYER) {
            return (Object)rank.GetColor() + "Player";
        }
        return rank.GetTag(false, false);
    }

    public static String rankBold(Rank rank) {
        if (rank == Rank.PLAYER) {
            return (Object)rank.GetColor() + C.Bold + "Player";
        }
        return rank.GetTag(true, false);
    }

    public static String value(String variable, String value) {
        return F.value(0, variable, value);
    }

    public static String value(int a, String variable, String value) {
        String indent = "";
        while (indent.length() < a) {
            indent = indent + (Object)ChatColor.GRAY + " \u00bb";
        }
        return indent + C.listTitle + variable + ": " + C.listValue + value;
    }

    public static String value(String variable, String value, boolean on) {
        return F.value(0, variable, value, on);
    }

    public static String value(int a, String variable, String value, boolean on) {
        String indent = "";
        while (indent.length() < a) {
            indent = indent + (Object)ChatColor.GRAY + " \u00bb";
        }
        if (on) {
            return indent + C.listTitle + variable + ": " + C.listValueOn + value;
        }
        return indent + C.listTitle + variable + ": " + C.listValueOff + value;
    }

    public static String attention(String module, String body) {
        return C.cRed + C.Scramble + C.Bold + "@" + C.cRed + C.Bold + " " + module + C.cWhite + C.Bold + " " + body;
    }

    public static String ed(boolean var) {
        if (var) {
            return C.listValueOn + "Enabled" + C.mBody;
        }
        return C.listValueOff + "Disabled" + C.mBody;
    }

    public static String oo(boolean var) {
        if (var) {
            return C.listValueOn + "On" + C.mBody;
        }
        return C.listValueOff + "Off" + C.mBody;
    }

    public static String yn(boolean var) {
        if (var) {
            return C.listValueOn + "Yes" + C.mBody;
        }
        return C.listValueOff + "No" + C.mBody;
    }

    public static String tf(boolean var) {
        if (var) {
            return C.listValueOn + "True" + C.mBody;
        }
        return C.listValueOff + "False" + C.mBody;
    }

    public static String oo(String variable, boolean value) {
        if (value) {
            return C.listValueOn + variable + C.mBody;
        }
        return C.listValueOff + variable + C.mBody;
    }

    public static String combine(String[] args, int start, String color) {
        if (args.length == 0) {
            return "";
        }
        StringBuilder out = new StringBuilder();
        for (int i = start; i < args.length; ++i) {
            if (color != null) {
                String preColor = ChatColor.getLastColors((String)args[i]);
                out.append(color).append(args[i]).append(preColor);
            } else {
                out.append(args[i]);
            }
            out.append(" ");
        }
        if (out.length() > 0) {
            out = new StringBuilder(out.substring(0, out.length() - 1));
        }
        return out.toString();
    }
}
