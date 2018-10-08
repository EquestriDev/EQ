/*
 * Decompiled with CFR 0_133.
 * 
 * Could not load the following classes:
 *  org.bukkit.entity.Player
 */
package com.equestriworlds.horse.command.admin;

import com.equestriworlds.command.CommandBase;
import com.equestriworlds.common.Rank;
import com.equestriworlds.horse.HorseManager;
import org.bukkit.entity.Player;

public class HorseAdminTestCommand
extends CommandBase<HorseManager> {
    public HorseAdminTestCommand(HorseManager plugin) {
        super(plugin, Rank.OWNER, new Rank[]{Rank.DEV}, "test");
    }

    @Override
    public void Execute(Player caller, String[] args) {
    }
}
