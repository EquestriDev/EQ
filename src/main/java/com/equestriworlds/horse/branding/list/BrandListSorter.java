package com.equestriworlds.horse.branding.list;

import com.equestriworlds.horse.branding.Brand;
import com.equestriworlds.horse.branding.BrandToken;
import java.util.Comparator;
import java.util.UUID;
import org.bukkit.ChatColor;

/**
 * Comparator
 */
public class BrandListSorter
implements Comparator<Brand> {
    @Override
    public int compare(Brand h1, Brand h2) {
        if (h1.token.format.equals(h2.token.format)) {
            return h1.id.compareTo(h2.id);
        }
        return ChatColor.stripColor((String)h1.token.format).compareTo(ChatColor.stripColor((String)h2.token.format));
    }
}
