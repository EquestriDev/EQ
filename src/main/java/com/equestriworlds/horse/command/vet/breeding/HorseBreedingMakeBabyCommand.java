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
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

public class HorseBreedingMakeBabyCommand
extends CommandBase<HorseManager> {
    public HorseBreedingMakeBabyCommand(HorseManager plugin) {
        super(plugin, Rank.VET, "createfoal");
    }

    @Override
    public void Execute(Player caller, String[] args) {
        if (args == null || args.length != 2) {
            UtilPlayer.message((Entity)caller, F.help("/vet createfoal (ID) (MateID)", "Make a baby with a horse", Rank.VET));
        } else {
            String id = args[0];
            CustomHorse horse = ((HorseManager)this.Plugin).config.getHorseById(id);
            String idMate = args[1];
            CustomHorse mate = ((HorseManager)this.Plugin).config.getHorseById(idMate);
            if (horse == null || mate == null) {
                UtilPlayer.message((Entity)caller, F.main(((HorseManager)this.Plugin).getName(), "Horse under the ID " + F.elem(new StringBuilder().append(id).append("").toString()) + " doesn't exist."));
                return;
            }
            if (horse.token.gender.equals((Object)Gender.GELDING) || mate.token.gender.equals((Object)Gender.GELDING)) {
                UtilPlayer.message((Entity)caller, F.main("Vet Manager", "You cannot breed " + F.elem("Gelding") + " with any horse."));
                return;
            }
            if (horse.token.gender.equals((Object)mate.token.gender)) {
                UtilPlayer.message((Entity)caller, F.main("Vet Manager", "You cannot breed horses with same genders."));
                return;
            }
            if (horse.token.gender == Gender.MARE) {
                if (horse.token.stage.equals((Object)breedingStages.PREGNANT)) {
                    UtilPlayer.message((Entity)caller, F.main("Vet Manager", "That mare is already pregnant."));
                    return;
                }
                if (horse.token.stage.equals((Object)breedingStages.MARERECOVERY)) {
                    UtilPlayer.message((Entity)caller, F.main("Vet Manager", "That mare is in recovery."));
                    return;
                }
                if (mate.token.stage.equals((Object)breedingStages.STALLIONRECOVERY)) {
                    UtilPlayer.message((Entity)caller, F.main("Vet Manager", "That stallion has recently bred."));
                    return;
                }
            } else {
                if (horse.token.stage.equals((Object)breedingStages.STALLIONRECOVERY)) {
                    UtilPlayer.message((Entity)caller, F.main("Vet Manager", "That stallion has recently bred."));
                    return;
                }
                if (mate.token.stage.equals((Object)breedingStages.PREGNANT)) {
                    UtilPlayer.message((Entity)caller, F.main("Vet Manager", "That mare is already pregnant."));
                    return;
                }
                if (mate.token.stage.equals((Object)breedingStages.MARERECOVERY)) {
                    UtilPlayer.message((Entity)caller, F.main("Vet Manager", "That mare is in recovery."));
                    return;
                }
                if (mate.token.stage.equals((Object)breedingStages.ABORT)) {
                    UtilPlayer.message((Entity)caller, F.main("Vet Manager", "That mare is in recovery."));
                    return;
                }
            }
            boolean horseAlive = horse.alive();
            boolean mateAlive = mate.alive();
            if (!horseAlive) {
                horse.spawn(caller.getLocation());
            }
            if (!mateAlive) {
                horse.spawn(caller.getLocation());
            }
            UtilPlayer.message((Entity)caller, F.main("Vet Manager", horse.token.name + " and " + mate.token.name + " have bred successfully"));
            if (horse.token.gender == Gender.MARE) {
                this.setPregnant(horse, mate);
            } else {
                this.setPregnant(mate, horse);
            }
            if (!horseAlive) {
                horse.remove(false);
            }
            if (!mateAlive) {
                horse.remove(false);
            }
        }
    }

    public void setPregnant(CustomHorse mother, CustomHorse father) {
        mother.setBreedingTime(System.currentTimeMillis() + 864000000L);
        mother.setBreedingStage(breedingStages.PREGNANT);
        mother.setPartner(father.id);
        father.setBreedingTime(System.currentTimeMillis() + 172800000L);
        father.setBreedingStage(breedingStages.STALLIONRECOVERY);
    }
}
