/*
 * Decompiled with CFR 0_133.
 * 
 * Could not load the following classes:
 *  org.bukkit.entity.Player
 */
package com.equestriworlds.vet.surgery;

import com.equestriworlds.account.CoreClientManager;
import com.equestriworlds.horse.HorseManager;
import com.equestriworlds.menu.MenuBase;
import com.equestriworlds.menu.page.MenuPageBase;
import com.equestriworlds.vet.surgery.SurgeryPage;
import org.bukkit.entity.Player;

public class SurgeryMenu
extends MenuBase<HorseManager> {
    public SurgeryMenu(HorseManager plugin, CoreClientManager clientManager) {
        super(plugin, clientManager, "Surgery Menu");
    }

    @Override
    protected MenuPageBase<HorseManager, ? extends MenuBase<HorseManager>> buildPagesFor(Player player) {
        return new SurgeryPage((HorseManager)this.getPlugin(), this, this.getClientManager(), this.getName(), player, 18);
    }
}
