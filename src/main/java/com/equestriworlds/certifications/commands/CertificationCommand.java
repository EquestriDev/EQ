package com.equestriworlds.certifications.commands;

import com.equestriworlds.MiniPlugin;
import com.equestriworlds.certifications.CertificationsManager;
import com.equestriworlds.certifications.commands.CertificationListCommand;
import com.equestriworlds.certifications.commands.CertificationStartCommand;
import com.equestriworlds.command.ICommand;
import com.equestriworlds.command.MultiCommandBase;
import com.equestriworlds.common.Rank;
import com.equestriworlds.util.F;
import com.equestriworlds.util.UtilPlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

public class CertificationCommand
extends MultiCommandBase<CertificationsManager> {
    public CertificationCommand(CertificationsManager plugin) {
        super(plugin, Rank.PLAYER, "cert", "certs", "certification", "certifications", "awesomeo");
        this.AddCommand(new CertificationListCommand((CertificationsManager)this.Plugin));
        this.AddCommand(new CertificationStartCommand((CertificationsManager)this.Plugin));
    }

    @Override
    protected void Help(Player caller, String[] args) {
        UtilPlayer.message((Entity)caller, F.main(((CertificationsManager)this.Plugin).getName(), "Available commands:"));
        UtilPlayer.message((Entity)caller, F.help("/cert list", "List all disciplines", Rank.PLAYER));
        UtilPlayer.message((Entity)caller, F.help("/cert start <Player> <Discipline>", "Start the player's test", Rank.JRMOD));
    }
}
