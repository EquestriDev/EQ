package com.equestriworlds.horse.command;

import com.equestriworlds.MiniPlugin;
import com.equestriworlds.command.CommandBase;
import com.equestriworlds.common.Rank;
import com.equestriworlds.horse.HorseManager;
import com.equestriworlds.horse.config.CustomHorse;
import com.equestriworlds.horse.config.CustomHorseToken;
import com.equestriworlds.horse.config.HorseConfig;
import com.equestriworlds.util.F;
import com.equestriworlds.util.UtilPlayer;
import java.util.List;
import java.util.UUID;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftAbstractHorse;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

public class HorseTeleportCommand
extends CommandBase<HorseManager> {
    public HorseTeleportCommand(HorseManager plugin) {
        super(plugin, Rank.PLAYER, "teleport", "tp");
    }

    @Override
    public void Execute(Player caller, String[] args) {
        if (args == null) {
            UtilPlayer.message((Entity)caller, F.main(((HorseManager)this.Plugin).getName(), "Correct usage:"));
            UtilPlayer.message((Entity)caller, F.help("/horse tp (ID)", "Teleport to your horse", Rank.PLAYER));
        } else {
            String id = args[0];
            CustomHorse horse = ((HorseManager)this.Plugin).config.getHorseById(id);
            if (horse == null) {
                UtilPlayer.message((Entity)caller, F.main(((HorseManager)this.Plugin).getName(), "Horse under the ID " + F.elem(new StringBuilder().append(id).append("").toString()) + " doesn't exist."));
                return;
            }
            for (UUID uuid : horse.token.trusted) {
                if (!caller.getUniqueId().equals(uuid)) continue;
                Location loc = horse.horse.getLocation();
                loc.setYaw(caller.getLocation().getYaw());
                loc.setPitch(caller.getLocation().getPitch());
                caller.teleport(loc);
                UtilPlayer.message((Entity)caller, F.main(((HorseManager)this.Plugin).getName(), "You teleported to " + F.elem(horse.token.name) + "."));
                return;
            }
            if (horse.token.owner == null || !caller.getUniqueId().equals(horse.token.owner)) {
                UtilPlayer.message((Entity)caller, F.main(((HorseManager)this.Plugin).getName(), "That horse does not belong to you."));
                return;
            }
            if (!horse.alive()) {
                UtilPlayer.message((Entity)caller, F.main(((HorseManager)this.Plugin).getName(), "That horse is not spawned"));
                return;
            }
            Location loc = horse.horse.getLocation();
            loc.setYaw(caller.getLocation().getYaw());
            loc.setPitch(caller.getLocation().getPitch());
            caller.teleport(loc);
            UtilPlayer.message((Entity)caller, F.main(((HorseManager)this.Plugin).getName(), "You teleported to " + F.elem(horse.token.name) + "."));
        }
    }
}
