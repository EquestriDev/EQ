/*
 * Decompiled with CFR 0_133.
 * 
 * Could not load the following classes:
 *  org.bukkit.entity.Player
 */
package com.equestriworlds.horse.gui.list;

import com.equestriworlds.account.CoreClientManager;
import com.equestriworlds.horse.HorseManager;
import com.equestriworlds.horse.gui.list.HorseListPage;
import com.equestriworlds.menu.MenuBase;
import com.equestriworlds.menu.page.MenuPageBase;
import org.bukkit.entity.Player;

public class HorseListMenu
extends MenuBase<HorseManager> {
    private String playerView;

    public HorseListMenu(HorseManager plugin, CoreClientManager clientManager, String playerView) {
        super(plugin, clientManager, "List");
        this.playerView = playerView;
    }

    @Override
    protected MenuPageBase<HorseManager, ? extends MenuBase<HorseManager>> buildPagesFor(Player player) {
        return new HorseListPage((HorseManager)this.getPlugin(), this, this.getClientManager(), player, this.playerView);
    }
}
