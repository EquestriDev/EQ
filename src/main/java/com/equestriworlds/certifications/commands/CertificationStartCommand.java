package com.equestriworlds.certifications.commands;

import com.equestriworlds.MiniPlugin;
import com.equestriworlds.certifications.CertificationsManager;
import com.equestriworlds.certifications.enums.Discipline;
import com.equestriworlds.certifications.enums.TestingPlayer;
import com.equestriworlds.command.CommandBase;
import com.equestriworlds.common.Rank;
import com.equestriworlds.util.C;
import com.equestriworlds.util.F;
import com.equestriworlds.util.UtilPlayer;
import java.util.HashMap;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

public class CertificationStartCommand
extends CommandBase<CertificationsManager> {
    CertificationStartCommand(CertificationsManager plugin) {
        super(plugin, Rank.PLAYER, "start");
    }

    @Override
    public void Execute(Player caller, String[] args) {
        if (args == null || args.length != 2) {
            UtilPlayer.message((Entity)caller, F.main(((CertificationsManager)this.Plugin).getName(), "Correct usage:"));
            UtilPlayer.message((Entity)caller, F.help("/cert start <Player> <Discipline>", "List all disciplines", Rank.PLAYER));
        } else {
            Player player = Bukkit.getPlayer((String)args[0]);
            if (player == null) {
                UtilPlayer.message((Entity)caller, F.main(((CertificationsManager)this.Plugin).getName(), C.convert("&c&l" + args[0] + " &cwas unable to be found")));
                return;
            }
            if (((CertificationsManager)this.Plugin).test.containsKey((Object)player)) {
                UtilPlayer.message((Entity)caller, F.main(((CertificationsManager)this.Plugin).getName(), C.convert("&c&l" + args[0] + " &cis already in a test")));
                return;
            }
            switch (args[1].toLowerCase()) {
                case "dressage": {
                    ((CertificationsManager)this.Plugin).test.put(player, new TestingPlayer(player, Discipline.DRESSAGE));
                    break;
                }
                case "showjumping": {
                    ((CertificationsManager)this.Plugin).test.put(player, new TestingPlayer(player, Discipline.SHOWJUMPING));
                    break;
                }
                case "sj": {
                    ((CertificationsManager)this.Plugin).test.put(player, new TestingPlayer(player, Discipline.SHOWJUMPING));
                    break;
                }
                case "crosscountry": {
                    ((CertificationsManager)this.Plugin).test.put(player, new TestingPlayer(player, Discipline.CROSSCOUNTRY));
                    break;
                }
                case "xc": {
                    ((CertificationsManager)this.Plugin).test.put(player, new TestingPlayer(player, Discipline.CROSSCOUNTRY));
                    break;
                }
                case "racing": {
                    ((CertificationsManager)this.Plugin).test.put(player, new TestingPlayer(player, Discipline.RACING));
                    break;
                }
                case "western": {
                    ((CertificationsManager)this.Plugin).test.put(player, new TestingPlayer(player, Discipline.WESTERN));
                    break;
                }
                case "trails": {
                    ((CertificationsManager)this.Plugin).test.put(player, new TestingPlayer(player, Discipline.TRAILS));
                    break;
                }
                case "hunters": {
                    ((CertificationsManager)this.Plugin).test.put(player, new TestingPlayer(player, Discipline.HUNTERS));
                    break;
                }
                case "eventing": {
                    ((CertificationsManager)this.Plugin).test.put(player, new TestingPlayer(player, Discipline.EVENTING));
                    break;
                }
                default: {
                    UtilPlayer.message((Entity)caller, F.main(((CertificationsManager)this.Plugin).getName(), C.convert("&c&l" + args[1] + " &cis not a discipline!")));
                    return;
                }
            }
            ((CertificationsManager)this.Plugin).sendQuestion(((CertificationsManager)this.Plugin).test.get((Object)player));
        }
    }
}
