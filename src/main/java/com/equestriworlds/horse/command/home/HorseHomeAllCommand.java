/*
 * Decompiled with CFR 0_133.
 * 
 * Could not load the following classes:
 *  org.bukkit.Location
 *  org.bukkit.entity.Entity
 *  org.bukkit.entity.Player
 */
package com.equestriworlds.horse.command.home;

import com.equestriworlds.MiniPlugin;
import com.equestriworlds.command.CommandBase;
import com.equestriworlds.common.Rank;
import com.equestriworlds.horse.HorseManager;
import com.equestriworlds.horse.config.CustomHorse;
import com.equestriworlds.horse.config.CustomHorseToken;
import com.equestriworlds.horse.config.HorseConfig;
import com.equestriworlds.util.F;
import com.equestriworlds.util.UtilPlayer;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.UUID;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

public class HorseHomeAllCommand
extends CommandBase<HorseManager> {
    public HorseHomeAllCommand(HorseManager plugin) {
        super(plugin, Rank.PLAYER, "homeall", "allhome");
    }

    @Override
    public void Execute(Player caller, String[] args) {
        int i = 0;
        for (CustomHorse horse : ((HorseManager)this.Plugin).config.horses.values()) {
            Collection<Location> locs;
            if (horse.token.owner == null || !horse.token.owner.equals(caller.getUniqueId()) || (locs = horse.token.homes.values()).isEmpty()) continue;
            Location loc = locs.iterator().next();
            horse.spawn(loc);
            ++i;
        }
        UtilPlayer.message((Entity)caller, F.main(((HorseManager)this.Plugin).getName(), "Successfully teleported " + F.elem(new StringBuilder().append(i).append(i == 1 ? " Horse" : " Horses").toString()) + "."));
    }
}
