package com.equestriworlds.barn.commands;

import com.equestriworlds.MiniPlugin;
import com.equestriworlds.account.CoreClientManager;
import com.equestriworlds.barn.BarnManager;
import com.equestriworlds.barn.gui.barnlookup.BarnLookupMenu;
import com.equestriworlds.command.CommandBase;
import com.equestriworlds.common.Rank;
import com.equestriworlds.util.F;
import com.equestriworlds.util.UtilPlayer;
import com.intellectualcrafters.plot.api.PlotAPI;
import com.intellectualcrafters.plot.object.Plot;
import java.util.HashSet;
import java.util.UUID;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

public class BarnSeeCommand
extends CommandBase<BarnManager> {
    BarnSeeCommand(BarnManager plugin) {
        super(plugin, Rank.PLAYER, "see", "lookup");
    }

    @Override
    public void Execute(Player caller, String[] args) {
        Plot plot = ((BarnManager)this.Plugin).plot.getPlot(caller);
        if (Rank.Has(caller, Rank.TJRMOD, false)) {
            if (plot == null || plot.temp == 0) {
                UtilPlayer.message((Entity)caller, F.main("Barn", "You need to stand on a claimed plot."));
            } else {
                new BarnLookupMenu((BarnManager)this.Plugin, ((BarnManager)this.Plugin).clientManager).attemptShopOpen(caller);
            }
        } else if (plot == null || !plot.isOwner(caller.getUniqueId()) && !plot.getTrusted().contains(caller.getUniqueId())) {
            UtilPlayer.message((Entity)caller, F.main("Barn", "You need to stand on your plot."));
        } else {
            new BarnLookupMenu((BarnManager)this.Plugin, ((BarnManager)this.Plugin).clientManager).attemptShopOpen(caller);
        }
    }
}
