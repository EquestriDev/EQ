package com.equestriworlds.horse.command.vet;

import com.equestriworlds.MiniPlugin;
import com.equestriworlds.account.CoreClientManager;
import com.equestriworlds.command.CommandBase;
import com.equestriworlds.common.Rank;
import com.equestriworlds.horse.HorseManager;
import com.equestriworlds.util.F;
import com.equestriworlds.util.UtilPlayer;
import com.equestriworlds.vet.surgery.SurgeryMenu;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

public class HorseVetSurgeryCommand
extends CommandBase<HorseManager> {
    HorseVetSurgeryCommand(HorseManager plugin) {
        super(plugin, Rank.VET, "surgery");
    }

    @Override
    public void Execute(Player caller, String[] args) {
        if (args != null) {
            UtilPlayer.message((Entity)caller, F.help("/vet surgery", "View the surgery menu", Rank.VET));
        } else {
            SurgeryMenu listMenu = new SurgeryMenu((HorseManager)this.Plugin, ((HorseManager)this.Plugin).clientManager);
            listMenu.attemptShopOpen(caller);
        }
    }
}
