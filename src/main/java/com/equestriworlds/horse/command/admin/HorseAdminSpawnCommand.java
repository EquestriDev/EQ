/*
 * Decompiled with CFR 0_133.
 * 
 * Could not load the following classes:
 *  org.bukkit.entity.Entity
 *  org.bukkit.entity.Player
 */
package com.equestriworlds.horse.command.admin;

import com.equestriworlds.MiniPlugin;
import com.equestriworlds.account.CoreClientManager;
import com.equestriworlds.command.CommandBase;
import com.equestriworlds.common.Rank;
import com.equestriworlds.horse.HorseManager;
import com.equestriworlds.horse.config.CustomHorse;
import com.equestriworlds.horse.config.HorseConfig;
import com.equestriworlds.horse.gui.spawner.HorseSpawnerMenu;
import com.equestriworlds.util.F;
import com.equestriworlds.util.UtilPlayer;
import java.util.Collection;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

public class HorseAdminSpawnCommand
extends CommandBase<HorseManager> {
    public HorseAdminSpawnCommand(HorseManager plugin) {
        super(plugin, Rank.TJRMOD, "spawn");
    }

    @Override
    public void Execute(Player caller, String[] args) {
        String id = null;
        if (args != null) {
            id = args[0];
            for (CustomHorse allHorses : ((HorseManager)this.Plugin).config.horses.values()) {
                if (!allHorses.id.toLowerCase().equals(id.toLowerCase())) continue;
                UtilPlayer.message((Entity)caller, F.main(((HorseManager)this.Plugin).getName(), "Horse under this ID already exists."));
                return;
            }
            if (id.length() > 16) {
                UtilPlayer.message((Entity)caller, F.main(((HorseManager)this.Plugin).getName(), "The ID cannot exceed the maximum amount of characters (16)"));
                return;
            }
            String regex = "^[a-zA-Z0-9]+$";
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(id);
            if (!matcher.matches()) {
                UtilPlayer.message((Entity)caller, F.main(((HorseManager)this.Plugin).getName(), "The ID can only have alphanumeric characters."));
                return;
            }
        }
        HorseSpawnerMenu menu = new HorseSpawnerMenu((HorseManager)this.Plugin, ((HorseManager)this.Plugin).clientManager, id);
        menu.attemptShopOpen(caller);
    }
}
