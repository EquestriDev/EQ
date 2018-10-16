package com.equestriworlds.horse.command.vet.breeding;

import com.equestriworlds.MiniPlugin;
import com.equestriworlds.command.CommandBase;
import com.equestriworlds.common.Rank;
import com.equestriworlds.horse.HorseManager;
import com.equestriworlds.horse.config.CustomHorse;
import com.equestriworlds.horse.config.HorseConfig;
import com.equestriworlds.util.F;
import com.equestriworlds.util.UtilPlayer;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftAbstractHorse;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

public class HorseBreedingtestBreedCommand
extends CommandBase<HorseManager> {
    public HorseBreedingtestBreedCommand(HorseManager plugin) {
        super(plugin, Rank.VET, "testbreed");
    }

    @Override
    public void Execute(Player caller, String[] args) {
        if (args == null || args.length != 2) {
            UtilPlayer.message((Entity)caller, F.help("/vet testbreed (ID) (MateID)", "Make a baby with a horse", Rank.VET));
        } else {
            String id = args[0];
            CustomHorse horse = ((HorseManager)this.Plugin).config.getHorseById(id);
            String idMate = args[1];
            CustomHorse mate = ((HorseManager)this.Plugin).config.getHorseById(idMate);
            if (horse == null || mate == null) {
                UtilPlayer.message((Entity)caller, F.main(((HorseManager)this.Plugin).getName(), "Horse under the ID " + F.elem(new StringBuilder().append(id).append("").toString()) + " doesn't exist."));
                return;
            }
            if (horse.horse.getType() == EntityType.MULE || mate.horse.getType() == EntityType.MULE) {
                UtilPlayer.message((Entity)caller, F.main(((HorseManager)this.Plugin).getName(), "You cannot breed mules."));
                return;
            }
            if (horse.horse.getType() == EntityType.LLAMA && mate.horse.getType() == EntityType.HORSE) {
                UtilPlayer.message((Entity)caller, F.main(((HorseManager)this.Plugin).getName(), "You cannot breed llamas and horses."));
                return;
            }
            if (mate.horse.getType() == EntityType.LLAMA && horse.horse.getType() == EntityType.HORSE) {
                UtilPlayer.message((Entity)caller, F.main(((HorseManager)this.Plugin).getName(), "You cannot breed llamas and horses."));
                return;
            }
            EntityType foalType = this.getEntityType(horse.horse.getType(), mate.horse.getType());
            UtilPlayer.message((Entity)caller, F.main("Vet Manager", "Foal Type: " + (Object)foalType));
        }
    }

    private EntityType getEntityType(EntityType mare, EntityType stallion) {
        if (stallion == null) {
            return mare;
        }
        if (mare == stallion) {
            return mare;
        }
        if (mare == EntityType.DONKEY && stallion == EntityType.HORSE) {
            return EntityType.MULE;
        }
        if (mare == EntityType.HORSE && stallion == EntityType.DONKEY) {
            return EntityType.MULE;
        }
        return EntityType.HORSE;
    }
}
