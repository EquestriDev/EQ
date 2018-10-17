package com.equestriworlds.horse.command.vet.breeding;

import com.equestriworlds.MiniPlugin;
import com.equestriworlds.command.CommandBase;
import com.equestriworlds.common.Rank;
import com.equestriworlds.horse.HorseManager;
import com.equestriworlds.horse.breeding.breedingStages;
import com.equestriworlds.horse.config.CustomHorse;
import com.equestriworlds.horse.config.CustomHorseToken;
import com.equestriworlds.horse.config.Gender;
import com.equestriworlds.horse.config.HorseConfig;
import com.equestriworlds.util.F;
import com.equestriworlds.util.UtilPlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

/**
 * Literal abortion by a vet.
 */
public class HorseBreedingAbortCommand
extends CommandBase<HorseManager> {
    public HorseBreedingAbortCommand(HorseManager plugin) {
        super(plugin, Rank.VET, "abort");
    }

    @Override
    public void Execute(Player caller, String[] args) {
        if (args == null || args.length != 1) {
            UtilPlayer.message((Entity)caller, F.help("/vet abort (ID)", "End the pregnancy of a mare", Rank.VET));
        } else {
            String id = args[0];
            CustomHorse horse = ((HorseManager)this.Plugin).config.getHorseById(id);
            if (horse == null) {
                UtilPlayer.message((Entity)caller, F.main("Vet Manager", "Horse under the ID " + F.elem(new StringBuilder().append(id).append("").toString()) + " doesn't exist."));
                return;
            }
            if (!horse.token.gender.equals((Object)Gender.MARE)) {
                UtilPlayer.message((Entity)caller, F.main("Vet Manager", F.elem(new StringBuilder().append(id).append("").toString()) + " isn't a mare."));
                return;
            }
            if (!horse.token.stage.equals((Object)breedingStages.PREGNANT)) {
                UtilPlayer.message((Entity)caller, F.main("Vet Manager", F.elem(new StringBuilder().append(id).append("").toString()) + " isn't pregnant."));
                return;
            }
            horse.setBreedingStage(breedingStages.ABORT);
            horse.setBreedingTime(System.currentTimeMillis() + 172800000L);
            UtilPlayer.message((Entity)caller, F.main("Vet Manager", F.elem(new StringBuilder().append(id).append("").toString()) + " has had her pregnancy ended."));
        }
    }
}
