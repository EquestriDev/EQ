package com.equestriworlds.horse.command;

import com.equestriworlds.MiniPlugin;
import com.equestriworlds.command.CommandBase;
import com.equestriworlds.common.Rank;
import com.equestriworlds.horse.HorseManager;
import com.equestriworlds.horse.config.CustomHorse;
import com.equestriworlds.horse.config.CustomHorseToken;
import com.equestriworlds.horse.config.HorseConfig;
import com.equestriworlds.util.C;
import com.equestriworlds.util.F;
import com.equestriworlds.util.UtilPlayer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftEntity;
import org.bukkit.entity.AbstractHorse;
import org.bukkit.entity.AnimalTamer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Tameable;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class HorseClaimCommand
extends CommandBase<HorseManager> {
    HorseClaimCommand(HorseManager plugin) {
        super(plugin, Rank.PLAYER, "claim");
    }

    @Override
    public void Execute(Player caller, String[] args) {
        if (caller.getLocation().getWorld().getName().equals("Survival")) {
            UtilPlayer.message((Entity)caller, F.main(((HorseManager)this.Plugin).getName(), "You cannot use this in Survival world."));
            return;
        }
        if (args == null) {
            UtilPlayer.message((Entity)caller, F.main(((HorseManager)this.Plugin).getName(), "Correct usage:"));
            UtilPlayer.message((Entity)caller, F.help("/horse claim (ID)", "Claim the horse that you leashed", Rank.PLAYER));
        } else {
            ArrayList<AbstractHorse> leashedHorses = new ArrayList<AbstractHorse>();
            for (Object entity : caller.getNearbyEntities(10.0, 10.0, 10.0)) {
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
            Entity horse = (Entity)leashedHorses.get(0);
            for (PotionEffect potionEffect : ((AbstractHorse)horse).getActivePotionEffects()) {
                if (!potionEffect.getType().equals((Object)PotionEffectType.INVISIBILITY)) continue;
                UtilPlayer.message((Entity)caller, F.main(((HorseManager)this.Plugin).getName(), "You cannot claim this horse"));
                UtilPlayer.message((Entity)caller, F.main(((HorseManager)this.Plugin).getName(), "Is abusing glitches really where you want to be?"));
                return;
            }
            CustomHorse customHorse = ((HorseManager)this.Plugin).config.getHorseByHorse((CraftEntity)horse);
            if (customHorse != null && !customHorse.token.free) {
                if (((Tameable)horse).getOwner() == caller) {
                    UtilPlayer.message((Entity)caller, F.main(((HorseManager)this.Plugin).getName(), "This horse is already yours."));
                    return;
                }
                UtilPlayer.message((Entity)caller, F.main(((HorseManager)this.Plugin).getName(), "This horse is already claimed by somebody else."));
                return;
            }
            String id = args[0];
            boolean changeid = false;
            for (CustomHorse allHorses : ((HorseManager)this.Plugin).config.horses.values()) {
                if (allHorses.equals(customHorse)) {
                    changeid = true;
                    continue;
                }
                if (!allHorses.id.toLowerCase().equals(id.toLowerCase())) continue;
                UtilPlayer.message((Entity)caller, F.main(((HorseManager)this.Plugin).getName(), "Horse under this ID already exists."));
                return;
            }
            if (id.length() > 16) {
                UtilPlayer.message((Entity)caller, F.main(((HorseManager)this.Plugin).getName(), "The ID cannot exceed the maximum amount of characters (16)"));
                return;
            }
            String regex = "^[a-zA-Z0-9]+$";
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(id);
            if (!matcher.matches()) {
                UtilPlayer.message((Entity)caller, F.main(((HorseManager)this.Plugin).getName(), "The ID can only have alphanumeric characters."));
                return;
            }
            if (changeid) {
                ((HorseManager)this.Plugin).config.changeId(customHorse.id, id);
            }
            ((HorseManager)this.Plugin).claim(caller, (AbstractHorse)horse, id);
            CustomHorse custom = ((HorseManager)this.Plugin).config.getHorseByHorse((CraftEntity)horse);
            if (custom.token.age == null) {
                custom.setAge(System.currentTimeMillis());
            }
            String Line = ChatColor.translateAlternateColorCodes((char)'&', (String)"&7&m&l\u2055--------------------------&7&l\u2055");
            UtilPlayer.message((Entity)caller, Line);
            UtilPlayer.message((Entity)caller, "");
            UtilPlayer.message((Entity)caller, F.main(((HorseManager)this.Plugin).getName(), "You successfully claimed the horse by the ID of " + F.elem(id) + "."));
            UtilPlayer.message((Entity)caller, "");
            UtilPlayer.message((Entity)caller, F.main(((HorseManager)this.Plugin).getName(), "If you would like to change the name of the horse, type:"));
            UtilPlayer.message((Entity)caller, F.main(((HorseManager)this.Plugin).getName(), F.elem("/horse rename " + F.time(id) + C.cDAqua + " (Name)")));
            UtilPlayer.message((Entity)caller, "");
            UtilPlayer.message((Entity)caller, Line);
        }
    }
}
