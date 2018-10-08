/*
 * Decompiled with CFR 0_133.
 * 
 * Could not load the following classes:
 *  net.md_5.bungee.api.ChatColor
 *  org.bukkit.craftbukkit.v1_12_R1.entity.CraftEntity
 *  org.bukkit.entity.AbstractHorse
 *  org.bukkit.entity.Entity
 *  org.bukkit.entity.LivingEntity
 *  org.bukkit.entity.Player
 */
package com.equestriworlds.horse.command.vet;

import com.equestriworlds.MiniPlugin;
import com.equestriworlds.command.CommandBase;
import com.equestriworlds.common.Rank;
import com.equestriworlds.horse.HorseManager;
import com.equestriworlds.horse.config.CustomHorse;
import com.equestriworlds.horse.config.CustomHorseToken;
import com.equestriworlds.horse.config.HorseConfig;
import com.equestriworlds.util.F;
import com.equestriworlds.util.UtilPlayer;
import com.equestriworlds.vet.enums.Symptom;
import java.util.ArrayList;
import java.util.List;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftEntity;
import org.bukkit.entity.AbstractHorse;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

public class HorseVetSymptomsCommand
extends CommandBase<HorseManager> {
    HorseVetSymptomsCommand(HorseManager plugin) {
        super(plugin, Rank.VET, "symptoms");
    }

    @Override
    public void Execute(Player caller, String[] args) {
        if (args != null) {
            UtilPlayer.message((Entity)caller, F.help("/vet symptoms", "View the symptoms of the horse on the leash", Rank.VET));
        } else {
            ArrayList<AbstractHorse> leashedHorses = new ArrayList<AbstractHorse>();
            for (Entity entity : caller.getNearbyEntities(10.0, 10.0, 10.0)) {
                if (!(entity instanceof AbstractHorse) || !((LivingEntity)entity).isLeashed() || !(((LivingEntity)entity).getLeashHolder() instanceof Player) || !((LivingEntity)entity).getLeashHolder().getName().equals(caller.getName())) continue;
                leashedHorses.add((AbstractHorse)entity);
            }
            if (leashedHorses.isEmpty()) {
                UtilPlayer.message((Entity)caller, F.main(((HorseManager)this.Plugin).getName(), "You need to leash a horse."));
                return;
            }
            if (leashedHorses.size() > 1) {
                UtilPlayer.message((Entity)caller, F.main(((HorseManager)this.Plugin).getName(), "You have more than one horse leashed."));
                return;
            }
            CustomHorse horse = ((HorseManager)this.Plugin).config.getHorseByHorse((CraftEntity)leashedHorses.get(0));
            if (horse == null) {
                UtilPlayer.message((Entity)caller, F.main("Vet Manager", "This horse is not claimed."));
                return;
            }
            int symptomsCount = 1;
            UtilPlayer.message((Entity)caller, ChatColor.translateAlternateColorCodes((char)'&', (String)"&7&m&l\u2055--------------------------&7&l\u2055"));
            UtilPlayer.message((Entity)caller, F.main("Vet Manager", "Medical information about the horse."));
            UtilPlayer.message((Entity)caller, F.desc("Symptoms", ""));
            for (Symptom symptom : horse.token.symptoms) {
                UtilPlayer.message((Entity)caller, F.desc(String.valueOf(symptomsCount), symptom.name));
                ++symptomsCount;
            }
            UtilPlayer.message((Entity)caller, ChatColor.translateAlternateColorCodes((char)'&', (String)"&7&m&l\u2055--------------------------&7&l\u2055"));
        }
    }
}
