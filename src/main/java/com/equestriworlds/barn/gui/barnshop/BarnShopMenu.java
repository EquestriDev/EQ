/*
 * Decompiled with CFR 0_133.
 * 
 * Could not load the following classes:
 *  org.bukkit.entity.Player
 */
package com.equestriworlds.barn.gui.barnshop;

import com.equestriworlds.account.CoreClientManager;
import com.equestriworlds.barn.BarnManager;
import com.equestriworlds.barn.gui.barnshop.BarnShopPage;
import com.equestriworlds.menu.MenuBase;
import com.equestriworlds.menu.page.MenuPageBase;
import org.bukkit.entity.Player;

public class BarnShopMenu
extends MenuBase<BarnManager> {
    public BarnShopMenu(BarnManager plugin, CoreClientManager clientManager) {
        super(plugin, clientManager, "Barn Shop");
    }

    @Override
    protected MenuPageBase<BarnManager, ? extends MenuBase<BarnManager>> buildPagesFor(Player player) {
        return new BarnShopPage((BarnManager)this.getPlugin(), this, this.getClientManager(), this.getName(), player);
    }
}
