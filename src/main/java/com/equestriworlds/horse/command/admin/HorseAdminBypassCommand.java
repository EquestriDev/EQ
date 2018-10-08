/*
 * Decompiled with CFR 0_133.
 * 
 * Could not load the following classes:
 *  org.bukkit.entity.Entity
 *  org.bukkit.entity.Player
 */
package com.equestriworlds.horse.command.admin;

import com.equestriworlds.MiniPlugin;
import com.equestriworlds.command.CommandBase;
import com.equestriworlds.common.Rank;
import com.equestriworlds.horse.HorseManager;
import com.equestriworlds.util.F;
import com.equestriworlds.util.UtilPlayer;
import java.util.HashSet;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

public class HorseAdminBypassCommand
extends CommandBase<HorseManager> {
    public HorseAdminBypassCommand(HorseManager plugin) {
        super(plugin, Rank.JRMOD, "bypass");
    }

    @Override
    public void Execute(Player caller, String[] args) {
        if (((HorseManager)this.Plugin).bypass.contains((Object)caller)) {
            ((HorseManager)this.Plugin).bypass.remove((Object)caller);
        } else {
            ((HorseManager)this.Plugin).bypass.add(caller);
        }
        UtilPlayer.message((Entity)caller, F.main("Horse Admin", "Horse Bypass Mode: " + F.ed(((HorseManager)this.Plugin).bypass.contains((Object)caller))));
    }
}
