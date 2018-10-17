package com.equestriworlds.rpmanager.commands;

import com.equestriworlds.MiniPlugin;
import com.equestriworlds.command.CommandBase;
import com.equestriworlds.common.Rank;
import com.equestriworlds.rpmanager.ResourcePack;
import com.equestriworlds.rpmanager.ResourcePackManager;
import com.equestriworlds.util.F;
import com.equestriworlds.util.UtilPlayer;
import java.util.List;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

/**
 * Change resource pack via command.
 */
public class ResourcePackCommand
extends CommandBase<ResourcePackManager> {
    public ResourcePackCommand(ResourcePackManager plugin) {
        super(plugin, Rank.PLAYER, "resourcepack", "rp");
    }

    @Override
    public void Execute(Player caller, String[] args) {
        if (args == null) {
            UtilPlayer.message((Entity)caller, F.help("/rp (Name)", "Set the Resource Pack.", Rank.PLAYER));
            return;
        }
        ResourcePack pack = null;
        try {
            pack = ResourcePack.valueOf(args[0]);
        }
        catch (Exception e) {
            UtilPlayer.message((Entity)caller, F.main(((ResourcePackManager)this.Plugin).getName(), "No such resourcepack"));
            return;
        }
        UtilPlayer.message((Entity)caller, F.main(((ResourcePackManager)this.Plugin).getName(), "Loading: " + F.elem(pack.name)));
        UtilPlayer.message((Entity)caller, F.main(((ResourcePackManager)this.Plugin).getName(), "Depends on the file size, the loading will take some time, so please, be patient. Thank you! :)"));
        ((ResourcePackManager)this.Plugin).load(caller, pack);
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, String commandLabel, String[] args) {
        String lastArg = args[args.length - 1];
        return this.getMatches(lastArg, ResourcePack.values());
    }
}
