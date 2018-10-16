package com.equestriworlds.misc.commands;

import com.equestriworlds.command.MultiCommandBase;
import com.equestriworlds.common.Rank;
import com.equestriworlds.horse.HorseManager;
import com.equestriworlds.util.F;
import com.equestriworlds.util.UtilPlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

public class TestingCommand
extends MultiCommandBase<HorseManager> {
    public TestingCommand(HorseManager plugin) {
        super(plugin, Rank.DEV, "test");
    }

    @Override
    protected void Help(Player caller, String[] args) {
        if (args != null) {
            UtilPlayer.message((Entity)caller, F.main("Server Manager", "Invalid arguments! Usage: /test"));
            return;
        }
    }
}
