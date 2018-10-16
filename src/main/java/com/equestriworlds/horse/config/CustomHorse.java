package com.equestriworlds.horse.config;

import com.equestriworlds.horse.branding.Brand;
import com.equestriworlds.horse.branding.BrandToken;
import com.equestriworlds.horse.breeding.breedingStages;
import com.equestriworlds.horse.config.CustomHorseToken;
import com.equestriworlds.horse.config.Gender;
import com.equestriworlds.horse.config.HorseAccess;
import com.equestriworlds.horse.pathfinders.HorseBreedPathfinder;
import com.equestriworlds.horse.pathfinders.HorseFollowPathfinder;
import com.equestriworlds.util.C;
import com.equestriworlds.util.UtilEnt;
import com.equestriworlds.util.UtilServer;
import com.equestriworlds.vet.enums.HappySymptom;
import com.equestriworlds.vet.enums.Symptom;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import net.minecraft.server.v1_12_R1.AttributeInstance;
import net.minecraft.server.v1_12_R1.EntityAnimal;
import net.minecraft.server.v1_12_R1.EntityCreature;
import net.minecraft.server.v1_12_R1.EntityHorseAbstract;
import net.minecraft.server.v1_12_R1.EntityHuman;
import net.minecraft.server.v1_12_R1.EntityInsentient;
import net.minecraft.server.v1_12_R1.GenericAttributes;
import net.minecraft.server.v1_12_R1.IAttribute;
import net.minecraft.server.v1_12_R1.PathfinderGoal;
import net.minecraft.server.v1_12_R1.PathfinderGoalBreed;
import net.minecraft.server.v1_12_R1.PathfinderGoalFloat;
import net.minecraft.server.v1_12_R1.PathfinderGoalLookAtPlayer;
import net.minecraft.server.v1_12_R1.PathfinderGoalPanic;
import net.minecraft.server.v1_12_R1.PathfinderGoalRandomLookaround;
import net.minecraft.server.v1_12_R1.PathfinderGoalRandomStrollLand;
import net.minecraft.server.v1_12_R1.PathfinderGoalTame;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftAbstractHorse;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftHorse;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftLlama;
import org.bukkit.entity.AbstractHorse;
import org.bukkit.entity.AnimalTamer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Horse;
import org.bukkit.entity.Llama;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

/**
 * Many setters with Entity writethrough.
 * Entity update methods.
 */
public class CustomHorse {
    public String id;
    public CustomHorseToken token;
    public CraftAbstractHorse horse;
    public boolean follow = false;

    public CustomHorse(String id, String name, UUID owner, Gender gender, Brand brand, Long age, Horse.Variant variant, Horse.Color color, Horse.Style style, Llama.Color llamaColor, boolean adult) {
        this(id, name, owner, gender, brand, age, variant, color, style, llamaColor, adult, new Random(1L).nextDouble(), new Random(1L).nextDouble(), false, null, false, null, HorseAccess.FRIENDS, new ArrayList<UUID>(), new ArrayList<UUID>(), new HashMap<String, Location>(), false, false, false, null, null);
    }

    public CustomHorse(String id, String name, UUID owner, Gender gender, Brand brand, Long age, Horse.Variant variant, Horse.Color color, Horse.Style style, Llama.Color llamaColor, boolean adult, double speed, double jump, boolean spawn, Location location, boolean leash, Location leashHolder, HorseAccess access, List<UUID> trusted, List<UUID> friends, HashMap<String, Location> homes, boolean free, boolean sale, boolean backedup, ItemStack saddle, ItemStack armor) {
        this.id = id;
        this.token = new CustomHorseToken();
        this.token.name = name;
        this.token.owner = owner;
        this.token.gender = gender;
        this.token.brand = brand;
        this.token.age = age;
        this.token.variant = variant;
        this.token.color = color;
        this.token.style = style;
        this.token.llamaColor = llamaColor;
        this.token.adult = adult;
        this.token.speed = speed;
        this.token.jump = jump;
        if (spawn && location != null) {
            this.token.lastKnown = location;
        }
        this.token.access = access;
        this.token.trusted = trusted;
        this.token.friends = friends;
        this.token.homes = homes;
        this.token.free = free;
        this.token.sale = sale;
        this.token.saddle = saddle;
        this.token.armor = armor;
        this.randomizeSymptoms();
        this.token.appearance = 5;
    }

    public CustomHorse(String id, AbstractHorse horse) {
        this.id = id;
        this.token = new CustomHorseToken();
        this.token.name = horse.getCustomName();
        this.token.owner = horse.getOwner().getUniqueId();
        this.token.gender = Gender.values()[new Random().nextInt(2)];
        this.token.age = System.currentTimeMillis();
        this.token.variant = horse.getVariant();
        this.token.color = horse instanceof Horse ? ((CraftHorse)horse).getColor() : null;
        this.token.style = horse instanceof Horse ? ((CraftHorse)horse).getStyle() : null;
        this.token.llamaColor = horse instanceof Llama ? ((CraftLlama)horse).getColor() : null;
        this.token.adult = horse.isAdult();
        this.token.speed = ((CraftAbstractHorse)horse).getHandle().getAttributeInstance(GenericAttributes.MOVEMENT_SPEED).getValue();
        this.token.jump = horse.getJumpStrength();
        this.token.trusted = new ArrayList<UUID>();
        this.token.friends = new ArrayList<UUID>();
        this.token.homes = new HashMap();
        this.horse = (CraftAbstractHorse)horse;
        this.horse.setAgeLock(true);
        UtilEnt.removeGoalSelectors((Entity)horse);
        UtilEnt.addAI((Entity)horse, 0, new HorseFollowPathfinder(this, 1.0, 5.0f, 2.0f));
        UtilEnt.addAI((Entity)horse, 0, (PathfinderGoal)new PathfinderGoalFloat((EntityInsentient)((CraftAbstractHorse)horse).getHandle()));
        UtilEnt.addAI((Entity)horse, 1, (PathfinderGoal)new PathfinderGoalPanic((EntityCreature)((CraftAbstractHorse)horse).getHandle(), 1.2));
        UtilEnt.addAI((Entity)horse, 1, (PathfinderGoal)new PathfinderGoalTame(((CraftAbstractHorse)horse).getHandle(), 1.2));
        UtilEnt.addAI((Entity)horse, 2, new HorseBreedPathfinder((EntityAnimal)((CraftAbstractHorse)horse).getHandle(), 1.0, EntityHorseAbstract.class));
        UtilEnt.addAI((Entity)horse, 6, (PathfinderGoal)new PathfinderGoalRandomStrollLand((EntityCreature)((CraftAbstractHorse)horse).getHandle(), 0.7, 20.0f));
        UtilEnt.addAI((Entity)horse, 7, (PathfinderGoal)new PathfinderGoalLookAtPlayer((EntityInsentient)((CraftAbstractHorse)horse).getHandle(), EntityHuman.class, 6.0f));
        UtilEnt.addAI((Entity)horse, 8, (PathfinderGoal)new PathfinderGoalRandomLookaround((EntityInsentient)((CraftAbstractHorse)horse).getHandle()));
        this.randomizeSymptoms();
        this.token.appearance = 5;
    }

    public CustomHorse(String id, CustomHorseToken token) {
        this.id = id;
        this.token = token;
    }

    public void summonToOwner() {
        Player player = UtilServer.getServer().getPlayer(this.token.owner);
        if (player == null || !player.isOnline()) {
            return;
        }
        this.spawn(player.getLocation());
    }

    public void spawn(Location location) {
        if (location == null || location.getChunk() == null || !location.getChunk().isLoaded()) {
            return;
        }
        if (this.horse == null || !this.horse.getHandle().isAlive()) {
            if (this.token.variant.equals((Object)Horse.Variant.HORSE)) {
                this.horse = (CraftAbstractHorse)UtilServer.getServer().getWorld(location.getWorld().getName()).spawnEntity(location, EntityType.HORSE);
                if (this.token.color != null) {
                    ((CraftHorse)this.horse).setColor(this.token.color);
                } else {
                    System.out.print("COLOR IS NULL: " + this.token.name);
                }
                if (this.token.style != null) {
                    ((CraftHorse)this.horse).setStyle(this.token.style);
                } else {
                    System.out.print("STYLE IS NULL: " + this.token.name);
                }
            } else if (this.token.variant.equals((Object)Horse.Variant.DONKEY)) {
                this.horse = (CraftAbstractHorse)UtilServer.getServer().getWorld(location.getWorld().getName()).spawnEntity(location, EntityType.DONKEY);
            } else if (this.token.variant.equals((Object)Horse.Variant.MULE)) {
                this.horse = (CraftAbstractHorse)UtilServer.getServer().getWorld(location.getWorld().getName()).spawnEntity(location, EntityType.MULE);
            } else if (this.token.variant.equals((Object)Horse.Variant.LLAMA)) {
                this.horse = (CraftAbstractHorse)UtilServer.getServer().getWorld(location.getWorld().getName()).spawnEntity(location, EntityType.LLAMA);
                ((CraftLlama)this.horse).setColor(this.token.llamaColor);
            } else if (this.token.variant.equals((Object)Horse.Variant.SKELETON_HORSE)) {
                this.horse = (CraftAbstractHorse)UtilServer.getServer().getWorld(location.getWorld().getName()).spawnEntity(location, EntityType.SKELETON_HORSE);
            } else if (this.token.variant.equals((Object)Horse.Variant.UNDEAD_HORSE)) {
                this.horse = (CraftAbstractHorse)UtilServer.getServer().getWorld(location.getWorld().getName()).spawnEntity(location, EntityType.ZOMBIE_HORSE);
            }
            if (this.token.adult) {
                this.horse.setAdult();
            } else {
                this.horse.setBaby();
            }
            this.setJump(this.token.jump);
            this.setSpeed(this.token.speed);
            if (this.token.brand != null) {
                this.horse.setCustomName((this.token.sale ? new StringBuilder().append(C.cGold).append(C.Bold).append("For Sale ").append(C.Reset).toString() : "") + C.convert(new StringBuilder().append(this.token.brand.token.format).append("&r ").append(this.token.name).toString()) + " " + this.g());
            } else {
                this.horse.setCustomName((this.token.sale ? new StringBuilder().append(C.cGold).append(C.Bold).append("For Sale ").append(C.Reset).toString() : "") + C.convert(this.token.name) + " " + this.g());
            }
            this.horse.setCustomNameVisible(true);
            this.horse.getInventory().setItem(0, this.token.saddle);
            this.horse.getInventory().setItem(1, this.token.armor);
        } else {
            this.horse.teleport(location);
        }
        this.token.lastKnown = null;
        if (this.token.owner != null) {
            this.horse.setOwner((AnimalTamer)UtilServer.getServer().getOfflinePlayer(this.token.owner));
        }
        this.horse.setAgeLock(true);
        UtilEnt.removeGoalSelectors((Entity)this.horse);
        UtilEnt.addAI((Entity)this.horse, 0, new HorseFollowPathfinder(this, 1.0, 5.0f, 2.0f));
        UtilEnt.addAI((Entity)this.horse, 0, (PathfinderGoal)new PathfinderGoalFloat((EntityInsentient)this.horse.getHandle()));
        UtilEnt.addAI((Entity)this.horse, 1, (PathfinderGoal)new PathfinderGoalPanic((EntityCreature)this.horse.getHandle(), 1.2));
        UtilEnt.addAI((Entity)this.horse, 1, (PathfinderGoal)new PathfinderGoalTame(this.horse.getHandle(), 1.2));
        UtilEnt.addAI((Entity)this.horse, 2, (PathfinderGoal)new PathfinderGoalBreed((EntityAnimal)this.horse.getHandle(), 1.0, EntityHorseAbstract.class));
        UtilEnt.addAI((Entity)this.horse, 6, (PathfinderGoal)new PathfinderGoalRandomStrollLand((EntityCreature)this.horse.getHandle(), 0.7, 20.0f));
        UtilEnt.addAI((Entity)this.horse, 7, (PathfinderGoal)new PathfinderGoalLookAtPlayer((EntityInsentient)this.horse.getHandle(), EntityHuman.class, 6.0f));
        UtilEnt.addAI((Entity)this.horse, 8, (PathfinderGoal)new PathfinderGoalRandomLookaround((EntityInsentient)this.horse.getHandle()));
    }

    public boolean alive() {
        return this.horse != null && this.horse.getHandle().isAlive();
    }

    public void remove() {
        this.remove(true);
    }

    public void remove(boolean save) {
        if (this.horse == null) {
            return;
        }
        this.updateInventory();
        if (save) {
            this.token.lastKnown = this.horse.getLocation();
        }
        this.horse.remove();
    }

    public boolean ownerOnline() {
        return this.token.owner == null ? null : Boolean.valueOf(UtilServer.getServer().getOfflinePlayer(this.token.owner).isOnline());
    }

    public void rename(String newName) {
        this.token.name = newName;
        if (this.horse == null) {
            return;
        }
        if (this.alive()) {
            this.horse.setCustomName((this.token.sale ? new StringBuilder().append(C.cGold).append(C.Bold).append("For Sale ").append(C.Reset).toString() : "") + (this.token.brand != null ? C.convert(new StringBuilder().append(this.token.brand.token.format).append("&r ").toString()) : "") + this.token.name + " " + this.g());
        }
    }

    public void changeOwner(UUID newOwner) {
        this.token.owner = newOwner;
        if (this.horse == null) {
            return;
        }
        if (this.alive()) {
            this.horse.setOwner((AnimalTamer)UtilServer.getServer().getOfflinePlayer(newOwner));
        }
    }

    public void free() {
        this.token.free = true;
        this.token.owner = null;
        if (this.horse == null) {
            return;
        }
        if (this.alive()) {
            this.horse.setOwner(null);
        }
    }

    public void setSpeed(double speed) {
        if (this.horse == null) {
            return;
        }
        this.token.speed = speed;
        if (this.alive()) {
            this.horse.getHandle().getAttributeInstance(GenericAttributes.MOVEMENT_SPEED).setValue(speed);
        }
    }

    public void setBrand(Brand brand) {
        if (this.horse == null) {
            return;
        }
        this.token.brand = brand;
    }

    public void changeGender(Gender gender) {
        this.token.gender = gender;
        if (this.alive()) {
            this.horse.setCustomName((this.token.sale ? new StringBuilder().append(C.cGold).append(C.Bold).append("For Sale ").append(C.Reset).toString() : "") + (this.token.brand != null ? C.convert(new StringBuilder().append(this.token.brand.token.format).append("&r ").toString()) : "") + this.token.name + " " + this.g());
        }
    }

    public void setBreedingStage(breedingStages stage) {
        if (this.horse == null) {
            return;
        }
        this.token.stage = stage;
    }

    public void setJump(double jump) {
        if (this.horse == null) {
            return;
        }
        this.token.jump = jump;
        if (this.alive()) {
            this.horse.setJumpStrength(jump);
        }
    }

    public boolean toggleFollow() {
        this.follow = !this.follow;
        return this.follow;
    }

    public void setStyle(Horse.Style style) {
        if (this.horse == null) {
            return;
        }
        this.token.style = style;
        if (this.alive()) {
            ((CraftHorse)this.horse).setStyle(style);
        }
    }

    public void setColor(Horse.Color color) {
        if (this.horse == null) {
            return;
        }
        this.token.color = color;
        if (this.alive()) {
            ((CraftHorse)this.horse).setColor(color);
        }
    }

    public void setAge(Long time) {
        if (this.horse == null) {
            return;
        }
        this.token.age = time;
    }

    public void setBreedingTime(Long time) {
        if (this.horse == null) {
            return;
        }
        this.token.breedingTime = time;
    }

    public void setPartner(String partner) {
        if (this.horse == null) {
            return;
        }
        this.token.partnerID = partner;
    }

    public void setLlamaColor(Llama.Color llamaColor) {
        if (this.horse == null) {
            return;
        }
        this.token.llamaColor = llamaColor;
        if (this.alive()) {
            ((CraftLlama)this.horse).setColor(llamaColor);
        }
    }

    public boolean toggleSale() {
        boolean bl = this.token.sale = !this.token.sale;
        if (this.alive()) {
            this.horse.setCustomName((this.token.sale ? new StringBuilder().append(C.cGold).append(C.Bold).append("For Sale ").append(C.Reset).toString() : "") + (this.token.brand != null ? C.convert(new StringBuilder().append(this.token.brand.token.format).append("&r ").toString()) : "") + this.token.name + " " + this.g());
        }
        return this.token.sale;
    }

    public String g() {
        if (this.token.gender.equals((Object)Gender.MARE)) {
            return C.cRed + C.Bold + "M";
        }
        if (this.token.gender.equals((Object)Gender.STALLION)) {
            return C.cAqua + C.Bold + "S";
        }
        return C.cGreen + C.Bold + "G";
    }

    public void updateInventory() {
        if (!this.alive()) {
            return;
        }
        this.token.saddle = this.horse.getInventory().getItem(0);
        this.token.armor = this.horse.getInventory().getItem(1);
    }

    private void randomizeSymptoms() {
        ArrayList<Symptom> symptoms = new ArrayList<Symptom>();
        while (symptoms.size() < 3) {
            HappySymptom symptom = HappySymptom.random();
            if (symptoms.contains((Object)symptom)) continue;
            symptoms.add(symptom.symptom);
        }
        this.token.symptoms.clear();
        this.token.symptoms.addAll(symptoms);
    }

    public void addAppearance(int appearance) {
        this.token.appearance += appearance;
        if (this.token.appearance > 10) {
            this.token.appearance = 10;
        }
    }
}
