package com.equestriworlds.barn.gui.barnlookup;

import com.equestriworlds.account.CoreClientManager;
import com.equestriworlds.barn.BarnManager;
import com.equestriworlds.barn.gui.barnlookup.BarnLookupPage;
import com.equestriworlds.menu.MenuBase;
import com.equestriworlds.menu.page.MenuPageBase;
import org.bukkit.entity.Player;

public class BarnLookupMenu
extends MenuBase<BarnManager> {
    public BarnLookupMenu(BarnManager plugin, CoreClientManager clientManager) {
        super(plugin, clientManager, "Barn Lookup");
    }

    @Override
    protected MenuPageBase<BarnManager, ? extends MenuBase<BarnManager>> buildPagesFor(Player player) {
        return new BarnLookupPage((BarnManager)this.getPlugin(), this, this.getClientManager(), this.getName(), player);
    }
}
