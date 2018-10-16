package com.equestriworlds.horse.command.brand;

import com.equestriworlds.MiniPlugin;
import com.equestriworlds.account.CoreClientManager;
import com.equestriworlds.command.CommandBase;
import com.equestriworlds.common.Rank;
import com.equestriworlds.horse.HorseManager;
import com.equestriworlds.horse.branding.list.BrandListMenu;
import org.bukkit.entity.Player;

public class HorseBrandListCommand
extends CommandBase<HorseManager> {
    public HorseBrandListCommand(HorseManager plugin) {
        super(plugin, Rank.PLAYER, "brandlist", "bl");
    }

    @Override
    public void Execute(Player caller, String[] args) {
        String playerView = caller.getName();
        BrandListMenu listMenu = new BrandListMenu((HorseManager)this.Plugin, ((HorseManager)this.Plugin).clientManager, playerView);
        listMenu.attemptShopOpen(caller);
    }
}
