package com.equestriworlds.horse.branding.list;

import com.equestriworlds.account.CoreClientManager;
import com.equestriworlds.horse.HorseManager;
import com.equestriworlds.horse.branding.list.BrandListPage;
import com.equestriworlds.menu.MenuBase;
import com.equestriworlds.menu.page.MenuPageBase;
import org.bukkit.entity.Player;

/**
 * /brandlist, /bl (Unused?)
 * Brand chest menu
 */
public class BrandListMenu
extends MenuBase<HorseManager> {
    public BrandListMenu(HorseManager plugin, CoreClientManager clientManager, String playerView) {
        super(plugin, clientManager, "List");
    }

    @Override
    protected MenuPageBase<HorseManager, ? extends MenuBase<HorseManager>> buildPagesFor(Player player) {
        return new BrandListPage((HorseManager)this.getPlugin(), this, this.getClientManager(), player);
    }
}
