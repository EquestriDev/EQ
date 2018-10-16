package com.equestriworlds.horse.pathfinders;

import java.util.List;
import java.util.Random;
import net.minecraft.server.v1_12_R1.AxisAlignedBB;
import net.minecraft.server.v1_12_R1.ControllerLook;
import net.minecraft.server.v1_12_R1.CriterionTriggerBredAnimals;
import net.minecraft.server.v1_12_R1.CriterionTriggers;
import net.minecraft.server.v1_12_R1.Entity;
import net.minecraft.server.v1_12_R1.EntityAgeable;
import net.minecraft.server.v1_12_R1.EntityAnimal;
import net.minecraft.server.v1_12_R1.EntityExperienceOrb;
import net.minecraft.server.v1_12_R1.EntityLiving;
import net.minecraft.server.v1_12_R1.EntityPlayer;
import net.minecraft.server.v1_12_R1.EntityTameableAnimal;
import net.minecraft.server.v1_12_R1.EnumParticle;
import net.minecraft.server.v1_12_R1.GameRules;
import net.minecraft.server.v1_12_R1.ItemStack;
import net.minecraft.server.v1_12_R1.NavigationAbstract;
import net.minecraft.server.v1_12_R1.PathfinderGoal;
import net.minecraft.server.v1_12_R1.Statistic;
import net.minecraft.server.v1_12_R1.StatisticList;
import net.minecraft.server.v1_12_R1.World;
import org.bukkit.craftbukkit.v1_12_R1.event.CraftEventFactory;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityBreedEvent;

/**
 * NBT pathfinder for horses
 */
public class HorseBreedPathfinder
extends PathfinderGoal {
    private final EntityAnimal animal;
    private final Class<? extends EntityAnimal> e;
    World a;
    private EntityAnimal partner;
    int b;
    double c;

    public HorseBreedPathfinder(EntityAnimal entityanimal, double d0) {
        this(entityanimal, d0, entityanimal.getClass());
    }

    public HorseBreedPathfinder(EntityAnimal entityanimal, double d0, Class<? extends EntityAnimal> oclass) {
        this.animal = entityanimal;
        this.a = entityanimal.world;
        this.e = oclass;
        this.c = d0;
        this.a(3);
    }

    public boolean a() {
        if (!this.animal.isInLove()) {
            return false;
        }
        this.partner = this.f();
        return this.partner != null;
    }

    public boolean b() {
        return this.partner.isAlive() && this.partner.isInLove() && this.b < 60;
    }

    public void d() {
        this.partner = null;
        this.b = 0;
    }

    public void e() {
        this.animal.getControllerLook().a((Entity)this.partner, 10.0f, (float)this.animal.N());
        this.animal.getNavigation().a((Entity)this.partner, this.c);
        ++this.b;
        if (this.b >= 60 && this.animal.h((Entity)this.partner) < 9.0) {
            this.i();
        }
    }

    private EntityAnimal f() {
        List list = this.a.a(this.e, this.animal.getBoundingBox().g(8.0));
        double d0 = Double.MAX_VALUE;
        EntityAnimal entityanimal = null;
        for (Object aList : list) {
            EntityAnimal entityanimal1 = (EntityAnimal)aList;
            if (!this.animal.mate(entityanimal1) || this.animal.h((Entity)entityanimal1) >= d0) continue;
            entityanimal = entityanimal1;
            d0 = this.animal.h((Entity)entityanimal1);
        }
        return entityanimal;
    }

    private void i() {
        EntityAgeable entityageable = this.animal.createChild((EntityAgeable)this.partner);
        if (entityageable != null) {
            EntityBreedEvent entityBreedEvent;
            int experience;
            EntityPlayer entityplayer;
            if (entityageable instanceof EntityTameableAnimal && ((EntityTameableAnimal)entityageable).isTamed()) {
                entityageable.persistent = true;
            }
            if ((entityplayer = this.animal.getBreedCause()) == null && this.partner.getBreedCause() != null) {
                entityplayer = this.partner.getBreedCause();
            }
            if ((entityBreedEvent = CraftEventFactory.callEntityBreedEvent((EntityLiving)entityageable, (EntityLiving)this.animal, (EntityLiving)this.partner, (EntityLiving)entityplayer, (ItemStack)this.animal.breedItem, (int)(experience = this.animal.getRandom().nextInt(7) + 1))).isCancelled()) {
                return;
            }
            experience = entityBreedEvent.getExperience();
            if (entityplayer != null) {
                entityplayer.b(StatisticList.C);
                CriterionTriggers.n.a(entityplayer, this.animal, this.partner, entityageable);
            }
            this.animal.setAgeRaw(6000);
            this.partner.setAgeRaw(6000);
            this.animal.resetLove();
            this.partner.resetLove();
            entityageable.setAgeRaw(-24000);
            entityageable.setPositionRotation(this.animal.locX, this.animal.locY, this.animal.locZ, 0.0f, 0.0f);
            this.a.addEntity((Entity)entityageable, CreatureSpawnEvent.SpawnReason.BREEDING);
            Random random = this.animal.getRandom();
            for (int i = 0; i < 7; ++i) {
                double d0 = random.nextGaussian() * 0.02;
                double d1 = random.nextGaussian() * 0.02;
                double d2 = random.nextGaussian() * 0.02;
                double d3 = random.nextDouble() * (double)this.animal.width * 2.0 - (double)this.animal.width;
                double d4 = 0.5 + random.nextDouble() * (double)this.animal.length;
                double d5 = random.nextDouble() * (double)this.animal.width * 2.0 - (double)this.animal.width;
                this.a.addParticle(EnumParticle.HEART, this.animal.locX + d3, this.animal.locY + d4, this.animal.locZ + d5, d0, d1, d2, new int[0]);
            }
            if (this.a.getGameRules().getBoolean("doMobLoot") && experience > 0) {
                this.a.addEntity((Entity)new EntityExperienceOrb(this.a, this.animal.locX, this.animal.locY, this.animal.locZ, experience));
            }
        }
    }
}
