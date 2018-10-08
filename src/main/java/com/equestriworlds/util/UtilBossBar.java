/*
 * Decompiled with CFR 0_133.
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.boss.BarColor
 *  org.bukkit.boss.BarFlag
 *  org.bukkit.boss.BarStyle
 *  org.bukkit.boss.BossBar
 */
package com.equestriworlds.util;

import org.bukkit.Bukkit;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarFlag;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;

public class UtilBossBar {
    public static BossBar createBar(String title, BarColor color, BarStyle style) {
        return Bukkit.createBossBar((String)title, (BarColor)color, (BarStyle)style, (BarFlag[])new BarFlag[0]);
    }
}
