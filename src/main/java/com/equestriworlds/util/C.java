/*
 * Decompiled with CFR 0_133.
 * 
 * Could not load the following classes:
 *  org.bukkit.ChatColor
 *  org.fusesource.jansi.Ansi
 *  org.fusesource.jansi.Ansi$Attribute
 *  org.fusesource.jansi.Ansi$Color
 */
package com.equestriworlds.util;

import java.util.EnumMap;
import java.util.Map;
import org.bukkit.ChatColor;
import org.fusesource.jansi.Ansi;

public class C {
    public static String Scramble = "" + (Object)ChatColor.MAGIC;
    public static String Bold = "" + (Object)ChatColor.BOLD;
    public static String Strike = "" + (Object)ChatColor.STRIKETHROUGH;
    public static String BoldStrike = "" + (Object)ChatColor.BOLD + "" + (Object)ChatColor.STRIKETHROUGH;
    public static String Underline = "" + (Object)ChatColor.UNDERLINE;
    public static String Italics = "" + (Object)ChatColor.ITALIC;
    public static String Reset = "" + (Object)ChatColor.RESET;
    public static String cAqua = "" + (Object)ChatColor.AQUA;
    public static String cBlack = "" + (Object)ChatColor.BLACK;
    public static String cBlue = "" + (Object)ChatColor.BLUE;
    public static String cDAqua = "" + (Object)ChatColor.DARK_AQUA;
    public static String cDBlue = "" + (Object)ChatColor.DARK_BLUE;
    public static String cDGray = "" + (Object)ChatColor.DARK_GRAY;
    public static String cDGreen = "" + (Object)ChatColor.DARK_GREEN;
    public static String cDPurple = "" + (Object)ChatColor.DARK_PURPLE;
    public static String cDRed = "" + (Object)ChatColor.DARK_RED;
    public static String cGold = "" + (Object)ChatColor.GOLD;
    public static String cGray = "" + (Object)ChatColor.GRAY;
    public static String cGreen = "" + (Object)ChatColor.GREEN;
    public static String cPurple = "" + (Object)ChatColor.LIGHT_PURPLE;
    public static String cRed = "" + (Object)ChatColor.RED;
    public static String cWhite = "" + (Object)ChatColor.WHITE;
    public static String cYellow = "" + (Object)ChatColor.YELLOW;
    public static String mHead = "" + (Object)ChatColor.AQUA + (Object)ChatColor.BOLD;
    public static String mBody = "" + (Object)ChatColor.GRAY;
    public static String mChat = "" + (Object)ChatColor.WHITE;
    public static String mElem = "" + (Object)ChatColor.DARK_AQUA;
    public static String mCount = "" + (Object)ChatColor.DARK_AQUA;
    public static String mTime = "" + (Object)ChatColor.AQUA;
    public static String mItem = "" + (Object)ChatColor.YELLOW;
    public static String mSkill = "" + (Object)ChatColor.GREEN;
    public static String mLink = "" + (Object)ChatColor.GREEN;
    public static String mLoot = "" + (Object)ChatColor.RED;
    public static String mGame = "" + (Object)ChatColor.LIGHT_PURPLE;
    public static String mError = "" + (Object)ChatColor.RED;
    public static String wField = "" + (Object)ChatColor.WHITE;
    public static String wFrame = "" + (Object)ChatColor.DARK_GRAY;
    public static String descHead = "" + (Object)ChatColor.DARK_AQUA;
    public static String descBody = "" + (Object)ChatColor.WHITE;
    public static String descBody2 = "" + (Object)ChatColor.GRAY;
    public static String chatPMHead = "" + (Object)ChatColor.DARK_GREEN;
    public static String chatPMBody = "" + (Object)ChatColor.GREEN;
    public static String chatClanHead = "" + (Object)ChatColor.DARK_AQUA;
    public static String chatClanBody = "" + (Object)ChatColor.AQUA;
    public static String chatAdminHead = "" + (Object)ChatColor.DARK_PURPLE;
    public static String chatAdminBody = "" + (Object)ChatColor.LIGHT_PURPLE;
    public static String listTitle = "" + (Object)ChatColor.WHITE;
    public static String listValue = "" + (Object)ChatColor.YELLOW;
    public static String listValueOn = "" + (Object)ChatColor.GREEN;
    public static String listValueOff = "" + (Object)ChatColor.RED;
    public static String consoleHead = "" + (Object)ChatColor.RED;
    public static String consoleFill = "" + (Object)ChatColor.WHITE;
    public static String consoleBody = "" + (Object)ChatColor.YELLOW;
    public static String sysHead = "" + (Object)ChatColor.DARK_GRAY;
    public static String sysBody = "" + (Object)ChatColor.GRAY;
    public static String chat = "" + (Object)ChatColor.WHITE;
    public static String xBorderlands = "" + (Object)ChatColor.GOLD;
    public static String xWilderness = "" + (Object)ChatColor.YELLOW;
    public static String xMid = "" + (Object)ChatColor.WHITE;
    public static String xNone = "" + (Object)ChatColor.GRAY;
    public static ChatColor xAdmin = ChatColor.WHITE;
    public static ChatColor xSafe = ChatColor.AQUA;
    public static ChatColor xSelf = ChatColor.AQUA;
    public static ChatColor xAlly = ChatColor.GREEN;
    public static ChatColor xEnemy = ChatColor.YELLOW;
    public static ChatColor xWar = ChatColor.RED;
    public static ChatColor xPillage = ChatColor.LIGHT_PURPLE;
    public static ChatColor xdSelf = ChatColor.DARK_AQUA;
    public static ChatColor xdAlly = ChatColor.DARK_GREEN;
    public static ChatColor xdEnemy = ChatColor.GOLD;
    public static ChatColor xdWar = ChatColor.DARK_RED;
    public static ChatColor xdPillage = ChatColor.DARK_PURPLE;
    private static Map<ChatColor, String> replacements = new EnumMap<ChatColor, String>(ChatColor.class);
    private static ChatColor[] colors = ChatColor.values();

    public static String convert(String string) {
        return ChatColor.translateAlternateColorCodes((char)'&', (String)string);
    }

    public static String clear(String string) {
        return ChatColor.stripColor((String)string);
    }

    public static String convertFromChatColor(ChatColor color) {
        if (color.equals((Object)ChatColor.AQUA)) {
            return cAqua;
        }
        if (color.equals((Object)ChatColor.BLACK)) {
            return cBlack;
        }
        if (color.equals((Object)ChatColor.BLUE)) {
            return cBlue;
        }
        if (color.equals((Object)ChatColor.DARK_AQUA)) {
            return cDAqua;
        }
        if (color.equals((Object)ChatColor.DARK_BLUE)) {
            return cDBlue;
        }
        if (color.equals((Object)ChatColor.DARK_GRAY)) {
            return cDGray;
        }
        if (color.equals((Object)ChatColor.DARK_GREEN)) {
            return cDGreen;
        }
        if (color.equals((Object)ChatColor.DARK_PURPLE)) {
            return cDPurple;
        }
        if (color.equals((Object)ChatColor.DARK_RED)) {
            return cDRed;
        }
        if (color.equals((Object)ChatColor.GOLD)) {
            return cGold;
        }
        if (color.equals((Object)ChatColor.GRAY)) {
            return cGray;
        }
        if (color.equals((Object)ChatColor.GREEN)) {
            return cGreen;
        }
        if (color.equals((Object)ChatColor.LIGHT_PURPLE)) {
            return cPurple;
        }
        if (color.equals((Object)ChatColor.RED)) {
            return cRed;
        }
        if (color.equals((Object)ChatColor.WHITE)) {
            return cWhite;
        }
        if (color.equals((Object)ChatColor.YELLOW)) {
            return cYellow;
        }
        return "";
    }

    public static void reg() {
        replacements.put(ChatColor.BLACK, Ansi.ansi().a(Ansi.Attribute.RESET).fg(Ansi.Color.BLACK).boldOff().toString());
        replacements.put(ChatColor.DARK_BLUE, Ansi.ansi().a(Ansi.Attribute.RESET).fg(Ansi.Color.BLUE).boldOff().toString());
        replacements.put(ChatColor.DARK_GREEN, Ansi.ansi().a(Ansi.Attribute.RESET).fg(Ansi.Color.GREEN).boldOff().toString());
        replacements.put(ChatColor.DARK_AQUA, Ansi.ansi().a(Ansi.Attribute.RESET).fg(Ansi.Color.CYAN).boldOff().toString());
        replacements.put(ChatColor.DARK_RED, Ansi.ansi().a(Ansi.Attribute.RESET).fg(Ansi.Color.RED).boldOff().toString());
        replacements.put(ChatColor.DARK_PURPLE, Ansi.ansi().a(Ansi.Attribute.RESET).fg(Ansi.Color.MAGENTA).boldOff().toString());
        replacements.put(ChatColor.GOLD, Ansi.ansi().a(Ansi.Attribute.RESET).fg(Ansi.Color.YELLOW).boldOff().toString());
        replacements.put(ChatColor.GRAY, Ansi.ansi().a(Ansi.Attribute.RESET).fg(Ansi.Color.WHITE).boldOff().toString());
        replacements.put(ChatColor.DARK_GRAY, Ansi.ansi().a(Ansi.Attribute.RESET).fg(Ansi.Color.BLACK).bold().toString());
        replacements.put(ChatColor.BLUE, Ansi.ansi().a(Ansi.Attribute.RESET).fg(Ansi.Color.BLUE).bold().toString());
        replacements.put(ChatColor.GREEN, Ansi.ansi().a(Ansi.Attribute.RESET).fg(Ansi.Color.GREEN).bold().toString());
        replacements.put(ChatColor.AQUA, Ansi.ansi().a(Ansi.Attribute.RESET).fg(Ansi.Color.CYAN).bold().toString());
        replacements.put(ChatColor.RED, Ansi.ansi().a(Ansi.Attribute.RESET).fg(Ansi.Color.RED).bold().toString());
        replacements.put(ChatColor.LIGHT_PURPLE, Ansi.ansi().a(Ansi.Attribute.RESET).fg(Ansi.Color.MAGENTA).bold().toString());
        replacements.put(ChatColor.YELLOW, Ansi.ansi().a(Ansi.Attribute.RESET).fg(Ansi.Color.YELLOW).bold().toString());
        replacements.put(ChatColor.WHITE, Ansi.ansi().a(Ansi.Attribute.RESET).fg(Ansi.Color.WHITE).bold().toString());
        replacements.put(ChatColor.MAGIC, Ansi.ansi().a(Ansi.Attribute.BLINK_SLOW).toString());
        replacements.put(ChatColor.BOLD, Ansi.ansi().a(Ansi.Attribute.UNDERLINE_DOUBLE).toString());
        replacements.put(ChatColor.STRIKETHROUGH, Ansi.ansi().a(Ansi.Attribute.STRIKETHROUGH_ON).toString());
        replacements.put(ChatColor.UNDERLINE, Ansi.ansi().a(Ansi.Attribute.UNDERLINE).toString());
        replacements.put(ChatColor.ITALIC, Ansi.ansi().a(Ansi.Attribute.ITALIC).toString());
        replacements.put(ChatColor.RESET, Ansi.ansi().a(Ansi.Attribute.RESET).toString());
    }

    public static String consoleConvert(String message) {
        String result = message;
        ChatColor[] arg2 = colors;
        int arg3 = colors.length;
        for (int arg4 = 0; arg4 < arg3; ++arg4) {
            ChatColor color = arg2[arg4];
            result = replacements.containsKey((Object)color) ? result.replaceAll("(?i)" + color.toString(), replacements.get((Object)color)) : result.replaceAll("(?i)" + color.toString(), "");
        }
        return result;
    }
}
