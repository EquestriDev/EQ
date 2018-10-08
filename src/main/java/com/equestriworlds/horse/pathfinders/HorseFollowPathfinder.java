/*
 * Decompiled with CFR 0_133.
 * 
 * Could not load the following classes:
 *  net.minecraft.server.v1_12_R1.AxisAlignedBB
 *  net.minecraft.server.v1_12_R1.BlockPosition
 *  net.minecraft.server.v1_12_R1.ControllerLook
 *  net.minecraft.server.v1_12_R1.Entity
 *  net.minecraft.server.v1_12_R1.EntityHorseAbstract
 *  net.minecraft.server.v1_12_R1.EntityHuman
 *  net.minecraft.server.v1_12_R1.EntityLiving
 *  net.minecraft.server.v1_12_R1.EntityPlayer
 *  net.minecraft.server.v1_12_R1.EnumBlockFaceShape
 *  net.minecraft.server.v1_12_R1.EnumDirection
 *  net.minecraft.server.v1_12_R1.IBlockAccess
 *  net.minecraft.server.v1_12_R1.IBlockData
 *  net.minecraft.server.v1_12_R1.MathHelper
 *  net.minecraft.server.v1_12_R1.Navigation
 *  net.minecraft.server.v1_12_R1.NavigationAbstract
 *  net.minecraft.server.v1_12_R1.NavigationFlying
 *  net.minecraft.server.v1_12_R1.PathType
 *  net.minecraft.server.v1_12_R1.PathfinderGoal
 *  net.minecraft.server.v1_12_R1.World
 *  org.bukkit.Location
 *  org.bukkit.World
 *  org.bukkit.craftbukkit.v1_12_R1.CraftServer
 *  org.bukkit.craftbukkit.v1_12_R1.entity.CraftAbstractHorse
 *  org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer
 *  org.bukkit.entity.Entity
 *  org.bukkit.entity.Player
 *  org.bukkit.event.Event
 *  org.bukkit.event.entity.EntityTeleportEvent
 *  org.bukkit.plugin.PluginManager
 */
package com.equestriworlds.horse.pathfinders;

import com.equestriworlds.horse.config.CustomHorse;
import com.equestriworlds.horse.config.CustomHorseToken;
import com.equestriworlds.util.UtilServer;
import java.util.UUID;
import net.minecraft.server.v1_12_R1.AxisAlignedBB;
import net.minecraft.server.v1_12_R1.BlockPosition;
import net.minecraft.server.v1_12_R1.ControllerLook;
import net.minecraft.server.v1_12_R1.EntityHorseAbstract;
import net.minecraft.server.v1_12_R1.EntityHuman;
import net.minecraft.server.v1_12_R1.EntityLiving;
import net.minecraft.server.v1_12_R1.EntityPlayer;
import net.minecraft.server.v1_12_R1.EnumBlockFaceShape;
import net.minecraft.server.v1_12_R1.EnumDirection;
import net.minecraft.server.v1_12_R1.IBlockAccess;
import net.minecraft.server.v1_12_R1.IBlockData;
import net.minecraft.server.v1_12_R1.MathHelper;
import net.minecraft.server.v1_12_R1.Navigation;
import net.minecraft.server.v1_12_R1.NavigationAbstract;
import net.minecraft.server.v1_12_R1.NavigationFlying;
import net.minecraft.server.v1_12_R1.PathType;
import net.minecraft.server.v1_12_R1.PathfinderGoal;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_12_R1.CraftServer;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftAbstractHorse;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityTeleportEvent;
import org.bukkit.plugin.PluginManager;

public class HorseFollowPathfinder
extends PathfinderGoal {
    private final CustomHorse customhorse;
    private EntityLiving owner;
    net.minecraft.server.v1_12_R1.World a;
    private final double f;
    private final NavigationAbstract g;
    private int h;
    float b;
    float c;
    private float i;

    public HorseFollowPathfinder(CustomHorse customhorse, double d0, float f, float f1) {
        this.customhorse = customhorse;
        this.a = customhorse.horse.getHandle().world;
        this.f = d0;
        this.g = customhorse.horse.getHandle().getNavigation();
        this.c = f;
        this.b = f1;
        this.a(3);
        if (!(customhorse.horse.getHandle().getNavigation() instanceof Navigation) && !(customhorse.horse.getHandle().getNavigation() instanceof NavigationFlying)) {
            throw new IllegalArgumentException("Unsupported mob type for FollowOwnerGoal");
        }
    }

    public boolean a() {
        if (this.customhorse.token.owner == null) {
            return false;
        }
        if (!this.customhorse.ownerOnline()) {
            return false;
        }
        EntityPlayer entityliving = ((CraftPlayer)UtilServer.getServer().getPlayer(this.customhorse.token.owner)).getHandle();
        if (entityliving == null) {
            return false;
        }
        if (entityliving instanceof EntityHuman && ((EntityHuman)entityliving).isSpectator()) {
            return false;
        }
        if (!this.customhorse.follow) {
            return false;
        }
        if (this.customhorse.horse.getHandle().h((net.minecraft.server.v1_12_R1.Entity)entityliving) < (double)(this.c * this.c)) {
            return false;
        }
        this.owner = entityliving;
        return true;
    }

    public boolean b() {
        return !this.g.o() && this.customhorse.horse.getHandle().h((net.minecraft.server.v1_12_R1.Entity)this.owner) > (double)(this.b * this.b) && this.customhorse.follow;
    }

    public void c() {
        this.h = 0;
        this.i = this.customhorse.horse.getHandle().a(PathType.WATER);
        this.customhorse.horse.getHandle().a(PathType.WATER, 0.0f);
    }

    public void d() {
        this.owner = null;
        this.g.p();
        this.customhorse.horse.getHandle().a(PathType.WATER, this.i);
    }

    public void e() {
        this.customhorse.horse.getHandle().getControllerLook().a((net.minecraft.server.v1_12_R1.Entity)this.owner, 10.0f, (float)this.customhorse.horse.getHandle().N());
        if (this.customhorse.follow && --this.h <= 0) {
            this.h = 10;
            if (!(this.g.a((net.minecraft.server.v1_12_R1.Entity)this.owner, this.f) || this.customhorse.horse.getHandle().isLeashed() || this.customhorse.horse.getHandle().isPassenger() || this.customhorse.horse.getHandle().h((net.minecraft.server.v1_12_R1.Entity)this.owner) < 144.0)) {
                int i = MathHelper.floor((double)this.owner.locX) - 2;
                int j = MathHelper.floor((double)this.owner.locZ) - 2;
                int k = MathHelper.floor((double)this.owner.getBoundingBox().b);
                for (int l = 0; l <= 4; ++l) {
                    for (int i1 = 0; i1 <= 4; ++i1) {
                        if (l >= 1 && i1 >= 1 && l <= 3 && i1 <= 3 || !this.a(i, j, k, l, i1)) continue;
                        CraftAbstractHorse entity = this.customhorse.horse;
                        Location to = new Location(entity.getWorld(), (double)((float)(i + l) + 0.5f), (double)k, (double)((float)(j + i1) + 0.5f), this.customhorse.horse.getHandle().yaw, this.customhorse.horse.getHandle().pitch);
                        EntityTeleportEvent event = new EntityTeleportEvent((Entity)entity, entity.getLocation(), to);
                        this.customhorse.horse.getHandle().world.getServer().getPluginManager().callEvent((Event)event);
                        if (event.isCancelled()) {
                            return;
                        }
                        to = event.getTo();
                        this.customhorse.horse.getHandle().setPositionRotation(to.getX(), to.getY(), to.getZ(), to.getYaw(), to.getPitch());
                        this.g.p();
                        return;
                    }
                }
            }
        }
    }

    protected boolean a(int i, int j, int k, int l, int i1) {
        BlockPosition blockposition = new BlockPosition(i + l, k - 1, j + i1);
        IBlockData iblockdata = this.a.getType(blockposition);
        return iblockdata.d((IBlockAccess)this.a, blockposition, EnumDirection.DOWN) == EnumBlockFaceShape.SOLID && iblockdata.a((net.minecraft.server.v1_12_R1.Entity)this.customhorse.horse.getHandle()) && this.a.isEmpty(blockposition.up()) && this.a.isEmpty(blockposition.up(2));
    }
}
