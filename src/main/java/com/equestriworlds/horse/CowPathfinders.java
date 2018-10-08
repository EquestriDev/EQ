/*
 * Decompiled with CFR 0_133.
 * 
 * Could not load the following classes:
 *  net.minecraft.server.v1_12_R1.Entity
 *  net.minecraft.server.v1_12_R1.EntityCreature
 *  net.minecraft.server.v1_12_R1.EntityHuman
 *  net.minecraft.server.v1_12_R1.PathfinderGoal
 *  net.minecraft.server.v1_12_R1.PathfinderGoalAvoidTarget
 *  org.bukkit.World
 *  org.bukkit.craftbukkit.v1_12_R1.entity.CraftEntity
 *  org.bukkit.entity.Entity
 *  org.bukkit.entity.EntityType
 *  org.bukkit.event.EventHandler
 */
package com.equestriworlds.horse;

import com.equestriworlds.MiniModule;
import com.equestriworlds.horse.HorseManager;
import com.equestriworlds.update.UpdateEvent;
import com.equestriworlds.update.UpdateType;
import com.equestriworlds.util.UtilEnt;
import com.equestriworlds.util.UtilServer;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.server.v1_12_R1.EntityCreature;
import net.minecraft.server.v1_12_R1.EntityHuman;
import net.minecraft.server.v1_12_R1.PathfinderGoal;
import net.minecraft.server.v1_12_R1.PathfinderGoalAvoidTarget;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftEntity;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;

public class CowPathfinders
extends MiniModule<HorseManager> {
    private List<Entity> list = new ArrayList<Entity>();

    CowPathfinders(HorseManager plugin) {
        super("Cow Pathfinders", plugin);
    }

    @EventHandler
    public void update(UpdateEvent e) {
        if (e.getType() != UpdateType.SEC) {
            return;
        }
        for (World world : UtilServer.getServer().getWorlds()) {
            for (Entity entity : world.getEntities()) {
                if (entity.getType() != EntityType.COW || this.list.contains((Object)entity)) continue;
                UtilEnt.addAI(entity, 0, (PathfinderGoal)new PathfinderGoalAvoidTarget((EntityCreature)((CraftEntity)entity).getHandle(), EntityHuman.class, 30.0f, 1.0, 3.7));
                this.list.add(entity);
            }
        }
    }
}
