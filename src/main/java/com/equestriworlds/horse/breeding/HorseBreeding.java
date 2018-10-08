/*
 * Decompiled with CFR 0_133.
 * 
 * Could not load the following classes:
 *  net.minecraft.server.v1_12_R1.EntityHorseAbstract
 *  org.bukkit.Bukkit
 *  org.bukkit.ChatColor
 *  org.bukkit.Location
 *  org.bukkit.World
 *  org.bukkit.craftbukkit.v1_12_R1.entity.CraftAbstractHorse
 *  org.bukkit.craftbukkit.v1_12_R1.entity.CraftEntity
 *  org.bukkit.entity.AbstractHorse
 *  org.bukkit.entity.Entity
 *  org.bukkit.entity.EntityType
 *  org.bukkit.entity.Horse
 *  org.bukkit.entity.Horse$Color
 *  org.bukkit.entity.Horse$Style
 *  org.bukkit.entity.LivingEntity
 *  org.bukkit.entity.Llama
 *  org.bukkit.entity.Llama$Color
 *  org.bukkit.entity.Player
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.entity.EntityBreedEvent
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.plugin.java.JavaPlugin
 *  org.bukkit.scheduler.BukkitRunnable
 *  org.bukkit.scheduler.BukkitTask
 */
package com.equestriworlds.horse.breeding;

import com.equestriworlds.MiniModule;
import com.equestriworlds.MiniPlugin;
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
import java.util.Collection;
import java.util.HashMap;
import java.util.UUID;
import net.minecraft.server.v1_12_R1.EntityHorseAbstract;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftAbstractHorse;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftEntity;
import org.bukkit.entity.AbstractHorse;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Horse;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Llama;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityBreedEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

public class HorseBreeding
extends MiniModule<HorseManager> {
    public HorseBreeding(HorseManager plugin) {
        super("Horse Breeding", plugin);
        this.breedingRunnable();
    }

    private void breedingRunnable() {
        new BukkitRunnable(){

            public void run() {
                for (CustomHorse horse : ((HorseManager)Plugin).config.horses.values()) {
                    if (horse.token.stage == breedingStages.NONE) continue;
                    if (horse.token.stage == breedingStages.PREGNANT) {
                        Player owner;
                        Long twoHoursEarlier = horse.token.breedingTime - 7200000L;
                        if (twoHoursEarlier - 22500L < System.currentTimeMillis() && System.currentTimeMillis() < twoHoursEarlier + 22500L && (owner = Bukkit.getPlayer((UUID)horse.token.owner)) != null) {
                            UtilPlayer.message((Entity)owner, F.main(HorseBreeding.this.getName(), horse.token.name + " is going into labour! Keep an eye on her!"));
                        }
                        if (System.currentTimeMillis() < horse.token.breedingTime) continue;
                        ((HorseManager)HorseBreeding.this.Plugin).runSync(() -> {
                            if (horse.alive()) {
                                HorseBreeding.this.createFoal(horse);
                            }
                        });
                        continue;
                    }
                    if (horse.token.stage == breedingStages.MARERECOVERY) {
                        if (System.currentTimeMillis() <= horse.token.breedingTime) continue;
                        horse.setBreedingStage(breedingStages.NONE);
                        continue;
                    }
                    if (horse.token.stage == breedingStages.STALLIONRECOVERY) {
                        if (System.currentTimeMillis() <= horse.token.breedingTime) continue;
                        horse.setBreedingStage(breedingStages.NONE);
                        continue;
                    }
                    if (horse.token.stage == breedingStages.ABORT) {
                        if (System.currentTimeMillis() <= horse.token.breedingTime) continue;
                        horse.setBreedingStage(breedingStages.NONE);
                        continue;
                    }
                    if (horse.token.stage == breedingStages.FOAL) {
                        if (horse.token.age >= System.currentTimeMillis() - 604800000L) continue;
                        horse.token.adult = true;
                        horse.setBreedingStage(breedingStages.FOALSTAGE2);
                        horse.setBreedingTime(System.currentTimeMillis() + 604800000L);
                        if (!horse.alive()) continue;
                        horse.horse.setAdult();
                        continue;
                    }
                    if (horse.token.stage != breedingStages.FOALSTAGE2 || System.currentTimeMillis() <= horse.token.breedingTime) continue;
                    horse.setBreedingStage(breedingStages.NONE);
                }
            }
        }.runTaskTimerAsynchronously((Plugin)((HorseManager)this.Plugin).getPlugin(), 800L, 800L);
    }

    @EventHandler
    public void breed(EntityBreedEvent event) {
        if (!(event.getMother() instanceof AbstractHorse && event.getFather() instanceof AbstractHorse && event.getBreeder() instanceof Player)) {
            return;
        }
        Player player = (Player)event.getBreeder();
        CustomHorse mother = ((HorseManager)this.Plugin).config.getHorseByHorse((CraftEntity)event.getMother());
        CustomHorse father = ((HorseManager)this.Plugin).config.getHorseByHorse((CraftEntity)event.getFather());
        if (!Rank.Has(player, Rank.VET, false)) {
            UtilPlayer.message((Entity)player, F.main(this.getName(), "You need to be a vet to do this!."));
            event.setCancelled(true);
            this.reset(mother, father);
            return;
        }
        if (mother == null || father == null) {
            UtilPlayer.message((Entity)player, F.main(this.getName(), "You can only breed claimed horses."));
            event.setCancelled(true);
            this.reset(mother, father);
            return;
        }
        if (mother.horse.getType() == EntityType.MULE || father.horse.getType() == EntityType.MULE) {
            UtilPlayer.message((Entity)player, F.main(this.getName(), "You cannot breed mules."));
            event.setCancelled(true);
            this.reset(mother, father);
            return;
        }
        if (mother.horse.getType() == EntityType.LLAMA && father.horse.getType() == EntityType.HORSE) {
            UtilPlayer.message((Entity)player, F.main(this.getName(), "You cannot breed llamas and horses."));
            event.setCancelled(true);
            this.reset(mother, father);
            return;
        }
        if (father.horse.getType() == EntityType.LLAMA && mother.horse.getType() == EntityType.HORSE) {
            UtilPlayer.message((Entity)player, F.main(this.getName(), "You cannot breed llamas and horses."));
            event.setCancelled(true);
            this.reset(mother, father);
            return;
        }
        if (!(mother.horse.getType() != EntityType.LLAMA && father.horse.getType() != EntityType.LLAMA || mother.horse.getType() != EntityType.HORSE && father.horse.getType() != EntityType.HORSE)) {
            UtilPlayer.message((Entity)player, F.main(this.getName(), "You cannot breed llamas and horses."));
            event.setCancelled(true);
            this.reset(mother, father);
            return;
        }
        if (mother.token.gender == Gender.GELDING || father.token.gender == Gender.GELDING) {
            UtilPlayer.message((Entity)player, F.main(this.getName(), "You cannot breed " + F.elem("Gelding") + " with any horse."));
            event.setCancelled(true);
            this.reset(mother, father);
            return;
        }
        if (mother.token.gender == father.token.gender) {
            UtilPlayer.message((Entity)player, F.main(this.getName(), "You cannot breed horses with same genders."));
            event.setCancelled(true);
            this.reset(mother, father);
            return;
        }
        if (mother.token.stage == breedingStages.FOALSTAGE2 || father.token.stage == breedingStages.FOALSTAGE2) {
            UtilPlayer.message((Entity)player, F.main(this.getName(), "You cannot breed foals!"));
            event.setCancelled(true);
            this.reset(mother, father);
            return;
        }
        if (mother.token.gender == Gender.MARE) {
            if (mother.token.stage == breedingStages.PREGNANT) {
                UtilPlayer.message((Entity)player, F.main(this.getName(), "That mare is already pregnant."));
                event.setCancelled(true);
                this.reset(mother, father);
                return;
            }
            if (mother.token.stage == breedingStages.MARERECOVERY) {
                UtilPlayer.message((Entity)player, F.main(this.getName(), "That mare is in recovery."));
                event.setCancelled(true);
                this.reset(mother, father);
                return;
            }
            if (father.token.stage == breedingStages.STALLIONRECOVERY) {
                UtilPlayer.message((Entity)player, F.main(this.getName(), "That stallion has recently bred."));
                event.setCancelled(true);
                this.reset(mother, father);
                return;
            }
        } else {
            if (mother.token.stage == breedingStages.STALLIONRECOVERY) {
                UtilPlayer.message((Entity)player, F.main(this.getName(), "That stallion has recently bred."));
                event.setCancelled(true);
                this.reset(mother, father);
                return;
            }
            if (father.token.stage == breedingStages.PREGNANT) {
                UtilPlayer.message((Entity)player, F.main(this.getName(), "That mare is already pregnant."));
                event.setCancelled(true);
                this.reset(mother, father);
                return;
            }
            if (father.token.stage == breedingStages.MARERECOVERY) {
                UtilPlayer.message((Entity)player, F.main(this.getName(), "That mare is in recovery."));
                event.setCancelled(true);
                this.reset(mother, father);
                return;
            }
        }
        event.setCancelled(true);
        this.reset(mother, father);
        UtilPlayer.message((Entity)player, F.main(this.getName(), mother.token.name + " and " + father.token.name + " have bred successfully"));
        if (mother.token.gender == Gender.MARE) {
            this.setPregnant(mother, father);
        } else {
            this.setPregnant(father, mother);
        }
    }

    private void reset(CustomHorse mother, CustomHorse father) {
        mother.horse.getHandle().resetLove();
        father.horse.getHandle().resetLove();
    }

    private void setPregnant(CustomHorse mother, CustomHorse father) {
        mother.setBreedingTime(System.currentTimeMillis() + 864000000L);
        mother.setBreedingStage(breedingStages.PREGNANT);
        mother.setPartner(father.id);
        father.setBreedingTime(System.currentTimeMillis() + 172800000L);
        father.setBreedingStage(breedingStages.STALLIONRECOVERY);
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
}
