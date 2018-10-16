package com.equestriworlds.barn.commands;

import com.equestriworlds.MiniPlugin;
import com.equestriworlds.barn.BarnManager;
import com.equestriworlds.barn.commands.BarnSeeCommand;
import com.equestriworlds.barn.commands.BarnShopCommand;
import com.equestriworlds.command.ICommand;
import com.equestriworlds.command.MultiCommandBase;
import com.equestriworlds.common.Rank;
import com.equestriworlds.util.F;
import com.equestriworlds.util.UtilPlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

public class BarnCommand
extends MultiCommandBase<BarnManager> {
    public BarnCommand(BarnManager plugin) {
        super(plugin, Rank.PLAYER, "barn", "b");
        this.AddCommand(new BarnShopCommand((BarnManager)this.Plugin));
        this.AddCommand(new BarnSeeCommand((BarnManager)this.Plugin));
    }

    @Override
    protected void Help(Player caller, String[] args) {
        UtilPlayer.message((Entity)caller, F.main(((BarnManager)this.Plugin).getName(), "Available commands:"));
        UtilPlayer.message((Entity)caller, F.help("/barn shop", "Open the barn shop", Rank.PLAYER));
        UtilPlayer.message((Entity)caller, F.help("/barn see", "Look what you have bought for your barn", Rank.PLAYER));
    }
}
