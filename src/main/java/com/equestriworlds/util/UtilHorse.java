package com.equestriworlds.util;

import java.util.Random;
import net.minecraft.server.v1_12_R1.Block;
import net.minecraft.server.v1_12_R1.Blocks;
import net.minecraft.server.v1_12_R1.Entity;
import net.minecraft.server.v1_12_R1.EntityHorseAbstract;
import net.minecraft.server.v1_12_R1.EntityHuman;
import net.minecraft.server.v1_12_R1.EntityInsentient;
import net.minecraft.server.v1_12_R1.EntityPlayer;
import net.minecraft.server.v1_12_R1.EnumHand;
import net.minecraft.server.v1_12_R1.EnumParticle;
import net.minecraft.server.v1_12_R1.Item;
import net.minecraft.server.v1_12_R1.ItemStack;
import net.minecraft.server.v1_12_R1.Items;
import net.minecraft.server.v1_12_R1.Packet;
import net.minecraft.server.v1_12_R1.PacketPlayOutAttachEntity;
import net.minecraft.server.v1_12_R1.PlayerAbilities;
import net.minecraft.server.v1_12_R1.PlayerConnection;
import net.minecraft.server.v1_12_R1.SoundCategory;
import net.minecraft.server.v1_12_R1.SoundEffect;
import net.minecraft.server.v1_12_R1.SoundEffects;
import net.minecraft.server.v1_12_R1.World;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_12_R1.event.CraftEventFactory;
import org.bukkit.event.entity.EntityRegainHealthEvent;

public class UtilHorse {
    public static boolean item(Material m) {
        return m.equals((Object)Material.WHEAT) || m.equals((Object)Material.SUGAR) || m.equals((Object)Material.HAY_BLOCK) || m.equals((Object)Material.APPLE) || m.equals((Object)Material.GOLDEN_CARROT) || m.equals((Object)Material.GOLDEN_APPLE) || m.equals((Object)Material.FISHING_ROD) || m.equals((Object)Material.LEASH) || m.equals((Object)Material.BOW);
    }

    public static boolean b(EntityHorseAbstract horse, EntityHuman entityhuman, ItemStack itemstack) {
        boolean flag = false;
        float f = 0.0f;
        int short0 = 0;
        int b0 = 0;
        Item item = itemstack.getItem();
        if (item == Items.WHEAT) {
            f = 2.0f;
            short0 = 20;
            b0 = 3;
        } else if (item == Items.SUGAR) {
            f = 1.0f;
            short0 = 30;
            b0 = 3;
        } else if (item == Item.getItemOf((Block)Blocks.HAY_BLOCK)) {
            f = 20.0f;
            short0 = 180;
        } else if (item == Items.APPLE) {
            f = 3.0f;
            short0 = 60;
            b0 = 3;
        } else if (item == Items.GOLDEN_CARROT) {
            f = 4.0f;
            short0 = 60;
            b0 = 5;
            if (horse.isTamed() && horse.getAge() == 0 && !horse.isInLove()) {
                flag = true;
                horse.f(entityhuman);
            }
        } else if (item == Items.GOLDEN_APPLE) {
            f = 10.0f;
            short0 = 240;
            b0 = 10;
            if (horse.isTamed() && horse.getAge() == 0 && !horse.isInLove()) {
                flag = true;
                horse.f(entityhuman);
            }
        }
        if (horse.getHealth() < horse.getMaxHealth() && f > 0.0f) {
            horse.heal(f, EntityRegainHealthEvent.RegainReason.EATING);
            flag = true;
        }
        if (horse.isBaby() && short0 > 0) {
            horse.world.addParticle(EnumParticle.VILLAGER_HAPPY, horse.locX + (double)(new Random().nextFloat() * horse.width * 2.0f) - (double)horse.width, horse.locY + 0.5 + (double)(new Random().nextFloat() * horse.length), horse.locZ + (double)(new Random().nextFloat() * horse.width * 2.0f) - (double)horse.width, 0.0, 0.0, 0.0, new int[0]);
            if (!horse.world.isClientSide) {
                horse.setAge(short0);
            }
            flag = true;
        }
        if (b0 > 0 && (flag || !horse.isTamed()) && horse.getTemper() < horse.getMaxDomestication()) {
            flag = true;
            if (!horse.world.isClientSide) {
                horse.n(b0);
            }
        }
        if (flag && !horse.isSilent()) {
            horse.world.a((EntityHuman)null, horse.locX, horse.locY, horse.locZ, SoundEffects.cK, horse.bK(), 1.0f, 1.0f + (new Random().nextFloat() - new Random().nextFloat()) * 0.2f);
        }
        return flag;
    }

    public static boolean b(EntityInsentient horse, EntityHuman entityhuman, EnumHand enumhand) {
        if (horse.isLeashed() && horse.getLeashHolder() == entityhuman) {
            if (CraftEventFactory.callPlayerUnleashEntityEvent((EntityInsentient)horse, (EntityHuman)entityhuman).isCancelled()) {
                ((EntityPlayer)entityhuman).playerConnection.sendPacket((Packet)new PacketPlayOutAttachEntity((Entity)horse, horse.getLeashHolder()));
                return false;
            }
            horse.unleash(true, !entityhuman.abilities.canInstantlyBuild);
            return true;
        }
        ItemStack itemstack = entityhuman.b(enumhand);
        if (itemstack.getItem() == Items.LEAD && !horse.isLeashed()) {
            if (CraftEventFactory.callPlayerLeashEntityEvent((EntityInsentient)horse, (Entity)entityhuman, (EntityHuman)entityhuman).isCancelled()) {
                ((EntityPlayer)entityhuman).playerConnection.sendPacket((Packet)new PacketPlayOutAttachEntity((Entity)horse, horse.getLeashHolder()));
                return false;
            }
            horse.setLeashHolder((Entity)entityhuman, true);
            itemstack.subtract(1);
            return true;
        }
        return false;
    }
}
