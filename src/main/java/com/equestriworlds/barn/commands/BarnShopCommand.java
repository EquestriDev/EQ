package com.equestriworlds.barn.commands;

import com.equestriworlds.MiniPlugin;
import com.equestriworlds.account.CoreClientManager;
import com.equestriworlds.barn.BarnManager;
import com.equestriworlds.barn.gui.barnshop.BarnShopMenu;
import com.equestriworlds.command.CommandBase;
import com.equestriworlds.common.Rank;
import org.bukkit.entity.Player;

public class BarnShopCommand
extends CommandBase<BarnManager> {
    BarnShopCommand(BarnManager plugin) {
        super(plugin, Rank.PLAYER, "shop");
    }

    @Override
    public void Execute(Player caller, String[] args) {
        BarnShopMenu menu = new BarnShopMenu((BarnManager)this.Plugin, ((BarnManager)this.Plugin).clientManager);
        menu.attemptShopOpen(caller);
    }
}
