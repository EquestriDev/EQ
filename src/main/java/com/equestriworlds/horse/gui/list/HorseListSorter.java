/*
 * Decompiled with CFR 0_133.
 * 
 * Could not load the following classes:
 *  org.bukkit.ChatColor
 */
package com.equestriworlds.horse.gui.list;

import com.equestriworlds.horse.config.CustomHorse;
import com.equestriworlds.horse.config.CustomHorseToken;
import java.util.Comparator;
import org.bukkit.ChatColor;

public class HorseListSorter
implements Comparator<CustomHorse> {
    @Override
    public int compare(CustomHorse h1, CustomHorse h2) {
        if (h1.token.name.equals(h2.token.name)) {
            return h1.id.compareTo(h2.id);
        }
        return ChatColor.stripColor((String)h1.token.name).compareTo(ChatColor.stripColor((String)h2.token.name));
    }
}
