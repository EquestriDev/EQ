package com.equestriworlds.horse.gui.spawner;

import com.equestriworlds.account.CoreClientManager;
import com.equestriworlds.horse.HorseManager;
import com.equestriworlds.horse.gui.spawner.HorseSpawnerPage;
import com.equestriworlds.menu.MenuBase;
import com.equestriworlds.menu.page.MenuPageBase;
import org.bukkit.entity.Player;

public class HorseSpawnerMenu
extends MenuBase<HorseManager> {
    String id;

    public HorseSpawnerMenu(HorseManager plugin, CoreClientManager clientManager, String id) {
        super(plugin, clientManager, "Spawner");
        this.id = id;
    }

    @Override
    protected MenuPageBase<HorseManager, ? extends MenuBase<HorseManager>> buildPagesFor(Player player) {
        return new HorseSpawnerPage((HorseManager)this.getPlugin(), this, this.getClientManager(), player, this.id);
    }
}
