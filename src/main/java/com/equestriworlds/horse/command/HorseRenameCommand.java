/*
 * Decompiled with CFR 0_133.
 * 
 * Could not load the following classes:
 *  net.md_5.bungee.api.ChatColor
 *  org.bukkit.entity.Entity
 *  org.bukkit.entity.Player
 */
package com.equestriworlds.horse.command;

import com.equestriworlds.MiniPlugin;
import com.equestriworlds.command.CommandBase;
import com.equestriworlds.common.Rank;
import com.equestriworlds.horse.HorseManager;
import com.equestriworlds.horse.config.CustomHorse;
import com.equestriworlds.horse.config.CustomHorseToken;
import com.equestriworlds.horse.config.HorseConfig;
import com.equestriworlds.util.C;
import com.equestriworlds.util.F;
import com.equestriworlds.util.UtilPlayer;
import java.util.UUID;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

public class HorseRenameCommand
extends CommandBase<HorseManager> {
    public HorseRenameCommand(HorseManager plugin) {
        super(plugin, Rank.PLAYER, "setname", "changename", "name", "rename");
    }

    @Override
    public void Execute(Player caller, String[] args) {
        if (args == null || args.length <= 1) {
            UtilPlayer.message((Entity)caller, F.main(((HorseManager)this.Plugin).getName(), "Correct usage:"));
            UtilPlayer.message((Entity)caller, F.help("/horse rename (ID) (Name)", "Rename the horse", Rank.PLAYER));
        } else {
            String id = args[0];
            CustomHorse horse = ((HorseManager)this.Plugin).config.getHorseById(id);
            if (horse == null) {
                UtilPlayer.message((Entity)caller, F.main(((HorseManager)this.Plugin).getName(), "Horse under the ID " + F.elem(new StringBuilder().append(id).append("").toString()) + " doesn't exist."));
                return;
            }
            if (horse.token.owner == null || !caller.getUniqueId().equals(horse.token.owner)) {
                UtilPlayer.message((Entity)caller, F.main(((HorseManager)this.Plugin).getName(), "That horse does not belong to you."));
                return;
            }
            String name = C.convert(F.combine(args, 1, ""));
            String colorlessName = ChatColor.stripColor((String)name);
            if (colorlessName.length() > 32) {
                UtilPlayer.message((Entity)caller, F.main(((HorseManager)this.Plugin).getName(), "The name cannot exceed the maximum amount of characters (32)"));
                return;
            }
            horse.rename(name);
            UtilPlayer.message((Entity)caller, F.main(((HorseManager)this.Plugin).getName(), "You renamed your horse to " + F.elem(name) + "."));
        }
    }
}
