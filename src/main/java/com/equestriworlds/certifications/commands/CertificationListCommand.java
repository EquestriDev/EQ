/*
 * Decompiled with CFR 0_133.
 * 
 * Could not load the following classes:
 *  org.bukkit.configuration.file.YamlConfiguration
 *  org.bukkit.entity.Entity
 *  org.bukkit.entity.Player
 */
package com.equestriworlds.certifications.commands;

import com.equestriworlds.MiniPlugin;
import com.equestriworlds.certifications.CertificationsManager;
import com.equestriworlds.certifications.configurations.configuration;
import com.equestriworlds.command.CommandBase;
import com.equestriworlds.common.Rank;
import com.equestriworlds.util.C;
import com.equestriworlds.util.F;
import com.equestriworlds.util.UtilPlayer;
import java.util.List;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

public class CertificationListCommand
extends CommandBase<CertificationsManager> {
    CertificationListCommand(CertificationsManager plugin) {
        super(plugin, Rank.PLAYER, "list");
    }

    @Override
    public void Execute(Player caller, String[] args) {
        if (args == null || args.length != 0) {
            for (String string : configuration.config.getStringList("Disciplines")) {
                caller.sendMessage(C.convert(string));
            }
        } else {
            UtilPlayer.message((Entity)caller, F.main(((CertificationsManager)this.Plugin).getName(), "Correct usage:"));
            UtilPlayer.message((Entity)caller, F.help("/cert list", "List all disciplines", Rank.PLAYER));
        }
    }
}
