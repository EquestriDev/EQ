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
import com.equestriworlds.util.UtilMath;
import com.equestriworlds.util.UtilPlayer;
import java.io.PrintStream;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftAbstractHorse;
import org.bukkit.entity.AbstractHorse;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Horse;
import org.bukkit.entity.Llama;
import org.bukkit.entity.Player;

public class HorseBreedingForceBirthCommand
extends CommandBase<HorseManager> {
    public HorseBreedingForceBirthCommand(HorseManager plugin) {
        super(plugin, Rank.VET, "forcebirth");
    }

    @Override
    public void Execute(Player caller, String[] args) {
        if (args == null || args.length != 1) {
            UtilPlayer.message((Entity)caller, F.help("/vet createfoal", "Make a baby with a horse", Rank.VET));
        } else {
            String id = args[0];
            CustomHorse horse = ((HorseManager)this.Plugin).config.getHorseById(id);
            if (horse == null) {
                UtilPlayer.message((Entity)caller, F.main(((HorseManager)this.Plugin).getName(), "Horse under the ID " + F.elem(new StringBuilder().append(id).append("").toString()) + " doesn't exist."));
                return;
            }
            if (!horse.token.gender.equals((Object)Gender.MARE)) {
                UtilPlayer.message((Entity)caller, F.main("Vet Manager", "That horse is not a mare and so can't be pregnant"));
                return;
            }
            if (horse.token.stage != breedingStages.PREGNANT) {
                UtilPlayer.message((Entity)caller, F.main("Vet Manager", "That mare is not pregnant"));
                return;
            }
            this.createFoal(horse);
        }
    }

    private void createFoal(CustomHorse mother) {
        CustomHorse maybeFoal;
        CustomHorse father = ((HorseManager)this.Plugin).config.getHorseById(mother.token.partnerID);
        Player player = Bukkit.getServer().getPlayer(mother.token.owner);
        if (player == null) {
            return;
        }
        if (mother.horse == null) {
            UtilPlayer.message((Entity)Bukkit.getPlayer((UUID)mother.token.owner), F.main("Vet", "You need to spawn " + mother.token.name + " so she can give birth"));
            return;
        }
        Location location = mother.horse.getLocation();
        AbstractHorse foal = (AbstractHorse)location.getWorld().spawnEntity(location, this.getEntityType(mother.horse.getType(), father == null ? null : (father.horse == null ? null : father.horse.getType())));
        foal.setBaby();
        String name = mother.id;
        if (father != null) {
            name = name + father.id;
        }
        if ((maybeFoal = ((HorseManager)this.Plugin).config.getHorseById(name)) != null) {
            CustomHorse namedHorse;
            name = name + String.valueOf(0);
            for (int i = 1; i < 64 && (namedHorse = ((HorseManager)this.Plugin).config.getHorseById(name)) != null; ++i) {
                name = this.replaceLast(name, String.valueOf(i - 1), String.valueOf(i));
            }
        }
        ((HorseManager)this.Plugin).claim(player, foal, name);
        CustomHorse customFoal = ((HorseManager)this.Plugin).config.getHorseById(name);
        customFoal.changeGender(Gender.random());
        EntityType entityType = this.getEntityType(mother.horse.getType(), father == null ? null : (father.horse == null ? null : father.horse.getType()));
        if (entityType == EntityType.LLAMA) {
            customFoal.setLlamaColor(UtilMath.randInt(0, father == null ? 0 : 1) == 0 ? mother.token.llamaColor : father.token.llamaColor);
        } else if (entityType == EntityType.HORSE) {
            customFoal.setColor(UtilMath.randInt(0, father == null ? 0 : 1) == 0 ? mother.token.color : father.token.color);
            customFoal.setStyle(UtilMath.randInt(0, father == null ? 0 : 1) == 0 ? mother.token.style : father.token.style);
        }
        if (father != null) {
            customFoal.setSpeed((mother.token.speed + father.token.speed) / 2.0);
            customFoal.setJump((mother.token.jump + father.token.jump) / 2.0);
        } else {
            customFoal.setSpeed(mother.token.speed);
            customFoal.setJump(mother.token.jump);
        }
        UtilPlayer.message((Entity)Bukkit.getPlayer((UUID)mother.token.owner), F.main("Vet", "Your new foal has been born!"));
        System.out.print((Object)ChatColor.RED + customFoal.id + " has been born by owner of " + player.getName());
        mother.setBreedingStage(breedingStages.MARERECOVERY);
        mother.setBreedingTime(System.currentTimeMillis() + 1209600000L);
        customFoal.setBreedingTime(System.currentTimeMillis() + 604800000L);
        customFoal.setBreedingStage(breedingStages.FOAL);
    }

    private String replaceLast(String text, String regex, String replacement) {
        return text.replaceFirst("(?s)(.*)" + regex, "$1" + replacement);
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
