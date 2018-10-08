/*
 * Decompiled with CFR 0_133.
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.entity.Entity
 *  org.bukkit.entity.Player
 */
package com.equestriworlds.horse.command.admin;

import com.equestriworlds.MiniPlugin;
import com.equestriworlds.command.CommandBase;
import com.equestriworlds.common.Rank;
import com.equestriworlds.horse.HorseManager;
import com.equestriworlds.horse.config.CustomHorse;
import com.equestriworlds.horse.config.CustomHorseToken;
import com.equestriworlds.horse.config.HorseConfig;
import com.equestriworlds.util.F;
import com.equestriworlds.util.UtilPlayer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

public class HorseAdminDespawnCommand
extends CommandBase<HorseManager> {
    HorseAdminDespawnCommand(HorseManager plugin) {
        super(plugin, Rank.ADMIN, "despawn");
    }

    @Override
    public void Execute(Player caller, String[] args) {
        if (args == null || args.length != 1) {
            UtilPlayer.message((Entity)caller, F.main(((HorseManager)this.Plugin).getName(), "Correct usage:"));
            UtilPlayer.message((Entity)caller, F.help("/ha despawn (ID)", "despawn", Rank.ADMIN));
        } else {
            Player player = Bukkit.getPlayer((String)args[0]);
            if (player == null) {
                UtilPlayer.message((Entity)caller, F.main(((HorseManager)this.Plugin).getName(), F.name(args[0]) + " isn't online"));
                return;
            }
            ArrayList<String> horses = new ArrayList<String>();
            UUID uuid = player.getUniqueId();
            for (CustomHorse horse : ((HorseManager)this.Plugin).config.horses.values()) {
                if (horse.token.owner == null || !horse.token.owner.equals(uuid) || !horse.alive()) continue;
                horses.add(horse.id);
                horse.remove(false);
            }
            caller.sendMessage(horses.toString());
        }
    }
}
