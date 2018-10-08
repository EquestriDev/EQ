/*
 * Decompiled with CFR 0_133.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Sets
 *  net.minecraft.server.v1_12_R1.ControllerMove
 *  net.minecraft.server.v1_12_R1.Entity
 *  net.minecraft.server.v1_12_R1.EntityCreature
 *  net.minecraft.server.v1_12_R1.EntityHuman
 *  net.minecraft.server.v1_12_R1.EntityInsentient
 *  net.minecraft.server.v1_12_R1.MethodProfiler
 *  net.minecraft.server.v1_12_R1.Navigation
 *  net.minecraft.server.v1_12_R1.NavigationAbstract
 *  net.minecraft.server.v1_12_R1.PathfinderGoal
 *  net.minecraft.server.v1_12_R1.PathfinderGoalLookAtPlayer
 *  net.minecraft.server.v1_12_R1.PathfinderGoalMoveTowardsRestriction
 *  net.minecraft.server.v1_12_R1.PathfinderGoalRandomLookaround
 *  net.minecraft.server.v1_12_R1.PathfinderGoalRandomStroll
 *  net.minecraft.server.v1_12_R1.PathfinderGoalSelector
 *  net.minecraft.server.v1_12_R1.WorldServer
 *  org.bukkit.Bukkit
 *  org.bukkit.Location
 *  org.bukkit.Material
 *  org.bukkit.Sound
 *  org.bukkit.World
 *  org.bukkit.block.Block
 *  org.bukkit.block.BlockFace
 *  org.bukkit.craftbukkit.v1_12_R1.CraftWorld
 *  org.bukkit.craftbukkit.v1_12_R1.entity.CraftCreature
 *  org.bukkit.craftbukkit.v1_12_R1.entity.CraftEntity
 *  org.bukkit.entity.Creature
 *  org.bukkit.entity.Entity
 *  org.bukkit.entity.EntityType
 *  org.bukkit.entity.Giant
 *  org.bukkit.entity.LivingEntity
 *  org.bukkit.entity.Player
 *  org.bukkit.util.Vector
 */
package com.equestriworlds.util;

import com.equestriworlds.util.C;
import com.equestriworlds.util.F;
import com.equestriworlds.util.UtilAlg;
import com.equestriworlds.util.UtilBlock;
import com.equestriworlds.util.UtilField;
import com.equestriworlds.util.UtilMath;
import com.equestriworlds.util.UtilPlayer;
import com.google.common.collect.Sets;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import net.minecraft.server.v1_12_R1.ControllerMove;
import net.minecraft.server.v1_12_R1.EntityCreature;
import net.minecraft.server.v1_12_R1.EntityHuman;
import net.minecraft.server.v1_12_R1.EntityInsentient;
import net.minecraft.server.v1_12_R1.MethodProfiler;
import net.minecraft.server.v1_12_R1.Navigation;
import net.minecraft.server.v1_12_R1.NavigationAbstract;
import net.minecraft.server.v1_12_R1.PathfinderGoal;
import net.minecraft.server.v1_12_R1.PathfinderGoalLookAtPlayer;
import net.minecraft.server.v1_12_R1.PathfinderGoalMoveTowardsRestriction;
import net.minecraft.server.v1_12_R1.PathfinderGoalRandomLookaround;
import net.minecraft.server.v1_12_R1.PathfinderGoalRandomStroll;
import net.minecraft.server.v1_12_R1.PathfinderGoalSelector;
import net.minecraft.server.v1_12_R1.WorldServer;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.craftbukkit.v1_12_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftCreature;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftEntity;
import org.bukkit.entity.Creature;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Giant;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class UtilEnt {
    private static HashMap<Entity, String> _nameMap = new HashMap();
    private static HashMap<String, EntityType> creatureMap = new HashMap();
    private static Field _goalSelector;
    private static Field _targetSelector;
    private static Field _bsRestrictionGoal;
    private static Field _pathfinderBList;
    private static Field _pathfinderCList;

    public static HashMap<Entity, String> GetEntityNames() {
        return _nameMap;
    }

    public static void silence(Entity entity, boolean silence) {
        ((CraftEntity)entity).getHandle().setSilent(silence);
    }

    public static void ghost(Entity entity, boolean ghost, boolean invisible) {
        if (entity instanceof LivingEntity) {
            // empty if block
        }
        ((CraftEntity)entity).getHandle().setInvisible(invisible);
    }

    public static void Leash(LivingEntity leashed, Entity holder, boolean pull, boolean breakable) {
        if (((CraftEntity)leashed).getHandle() instanceof EntityInsentient) {
            EntityInsentient entityInsentient = (EntityInsentient)((CraftEntity)leashed).getHandle();
        }
        leashed.setLeashHolder(holder);
    }

    public static void addLookAtPlayerAI(Entity entity, float dist) {
        if (((CraftEntity)entity).getHandle() instanceof EntityInsentient) {
            UtilEnt.addAI(entity, 7, (PathfinderGoal)new PathfinderGoalLookAtPlayer((EntityInsentient)((CraftEntity)entity).getHandle(), EntityHuman.class, dist));
            UtilEnt.addAI(entity, 8, (PathfinderGoal)new PathfinderGoalRandomLookaround((EntityInsentient)((CraftEntity)entity).getHandle()));
        }
    }

    public static void addAI(Entity entity, int value, PathfinderGoal ai) {
        if (((CraftEntity)entity).getHandle() instanceof EntityInsentient) {
            EntityInsentient ei = (EntityInsentient)((CraftEntity)entity).getHandle();
            if (_goalSelector == null) {
                try {
                    _goalSelector = EntityInsentient.class.getDeclaredField("goalSelector");
                }
                catch (NoSuchFieldException e) {
                    e.printStackTrace();
                    return;
                }
                _goalSelector.setAccessible(true);
            }
            try {
                ((PathfinderGoalSelector)_goalSelector.get((Object)ei)).a(value, ai);
            }
            catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    public static void Vegetate(Entity entity) {
        UtilEnt.Vegetate(entity, false);
    }

    public static void Vegetate(Entity entity, boolean mute) {
        try {
            if (_pathfinderBList == null) {
                _pathfinderBList = UtilField.getField(PathfinderGoalSelector.class, "b");
            }
            if (_pathfinderCList == null) {
                _pathfinderCList = UtilField.getField(PathfinderGoalSelector.class, "c");
            }
            entity.setSilent(mute);
            UtilField.setField((Object)((CraftCreature)entity).getHandle().goalSelector, _pathfinderBList, Sets.newLinkedHashSet());
            UtilField.setField((Object)((CraftCreature)entity).getHandle().targetSelector, _pathfinderBList, Sets.newLinkedHashSet());
            UtilField.setField((Object)((CraftCreature)entity).getHandle().goalSelector, _pathfinderCList, Sets.newLinkedHashSet());
            UtilField.setField((Object)((CraftCreature)entity).getHandle().goalSelector, _pathfinderCList, Sets.newLinkedHashSet());
            ((CraftCreature)entity).getHandle().goalSelector.a(5, (PathfinderGoal)new PathfinderGoalMoveTowardsRestriction(((CraftCreature)entity).getHandle(), 0.0));
            ((CraftCreature)entity).getHandle().goalSelector.a(7, (PathfinderGoal)new PathfinderGoalRandomStroll(((CraftCreature)entity).getHandle(), 0.0));
            ((CraftCreature)entity).getHandle().goalSelector.a(8, (PathfinderGoal)new PathfinderGoalLookAtPlayer((EntityInsentient)((CraftCreature)entity).getHandle(), EntityHuman.class, 0.0f));
            ((CraftCreature)entity).getHandle().goalSelector.a(8, (PathfinderGoal)new PathfinderGoalRandomLookaround((EntityInsentient)((CraftCreature)entity).getHandle()));
        }
        catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
        catch (SecurityException e) {
            e.printStackTrace();
        }
    }

    public static void removeGoalSelectors(Entity entity) {
        try {
            if (_goalSelector == null) {
                _goalSelector = EntityInsentient.class.getDeclaredField("goalSelector");
                _goalSelector.setAccessible(true);
            }
            if (((CraftEntity)entity).getHandle() instanceof EntityInsentient) {
                EntityInsentient creature = (EntityInsentient)((CraftEntity)entity).getHandle();
                PathfinderGoalSelector goalSelector = new PathfinderGoalSelector(((CraftWorld)entity.getWorld()).getHandle().methodProfiler);
                _goalSelector.set((Object)creature, (Object)goalSelector);
            }
        }
        catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
        catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
        catch (SecurityException e) {
            e.printStackTrace();
        }
    }

    public static void populate() {
        if (creatureMap.isEmpty()) {
            creatureMap.put("Bat", EntityType.BAT);
            creatureMap.put("Blaze", EntityType.BLAZE);
            creatureMap.put("Arrow", EntityType.ARROW);
            creatureMap.put("Cave Spider", EntityType.CAVE_SPIDER);
            creatureMap.put("Chicken", EntityType.CHICKEN);
            creatureMap.put("Cow", EntityType.COW);
            creatureMap.put("Creeper", EntityType.CREEPER);
            creatureMap.put("Ender Dragon", EntityType.ENDER_DRAGON);
            creatureMap.put("Enderman", EntityType.ENDERMAN);
            creatureMap.put("Ghast", EntityType.GHAST);
            creatureMap.put("Giant", EntityType.GIANT);
            creatureMap.put("Horse", EntityType.HORSE);
            creatureMap.put("Iron Golem", EntityType.IRON_GOLEM);
            creatureMap.put("Item", EntityType.DROPPED_ITEM);
            creatureMap.put("Magma Cube", EntityType.MAGMA_CUBE);
            creatureMap.put("Mooshroom", EntityType.MUSHROOM_COW);
            creatureMap.put("Ocelot", EntityType.OCELOT);
            creatureMap.put("Pig", EntityType.PIG);
            creatureMap.put("Pig Zombie", EntityType.PIG_ZOMBIE);
            creatureMap.put("Sheep", EntityType.SHEEP);
            creatureMap.put("Silverfish", EntityType.SILVERFISH);
            creatureMap.put("Skeleton", EntityType.SKELETON);
            creatureMap.put("Slime", EntityType.SLIME);
            creatureMap.put("Snowman", EntityType.SNOWMAN);
            creatureMap.put("Spider", EntityType.SPIDER);
            creatureMap.put("Squid", EntityType.SQUID);
            creatureMap.put("Villager", EntityType.VILLAGER);
            creatureMap.put("Witch", EntityType.WITCH);
            creatureMap.put("Wither", EntityType.WITHER);
            creatureMap.put("WitherSkull", EntityType.WITHER_SKULL);
            creatureMap.put("Wolf", EntityType.WOLF);
            creatureMap.put("Zombie", EntityType.ZOMBIE);
            creatureMap.put("Item", EntityType.DROPPED_ITEM);
        }
    }

    public static String getName(Entity ent) {
        LivingEntity le;
        if (ent == null) {
            return "Null";
        }
        if (ent.getType() == EntityType.PLAYER) {
            return ((Player)ent).getName();
        }
        if (UtilEnt.GetEntityNames().containsKey((Object)ent)) {
            return UtilEnt.GetEntityNames().get((Object)ent);
        }
        if (ent instanceof LivingEntity && (le = (LivingEntity)ent).getCustomName() != null) {
            return le.getCustomName();
        }
        return UtilEnt.getName(ent.getType());
    }

    public static String getName(EntityType type) {
        for (String cur : creatureMap.keySet()) {
            if (creatureMap.get(cur) != type) continue;
            return cur;
        }
        return type.getName();
    }

    public static String searchName(Player caller, String arg, boolean inform) {
        UtilEnt.populate();
        arg = arg.toLowerCase().replaceAll("_", " ");
        LinkedList<String> matchList = new LinkedList<String>();
        for (String cur : creatureMap.keySet()) {
            if (cur.equalsIgnoreCase(arg)) {
                return cur;
            }
            if (!cur.toLowerCase().contains(arg)) continue;
            matchList.add(cur);
        }
        if (matchList.size() != 1) {
            if (!inform) {
                return null;
            }
            UtilPlayer.message((Entity)caller, F.main("Creature Search", "" + C.mCount + matchList.size() + C.mBody + " matches for [" + C.mElem + arg + C.mBody + "]."));
            if (matchList.size() > 0) {
                String matchString = "";
                for (String cur : matchList) {
                    matchString = matchString + F.elem(cur) + ", ";
                }
                if (matchString.length() > 1) {
                    matchString = matchString.substring(0, matchString.length() - 2);
                }
                UtilPlayer.message((Entity)caller, F.main("Creature Search", "" + C.mBody + "Matches [" + C.mElem + matchString + C.mBody + "]."));
            }
            return null;
        }
        return (String)matchList.get(0);
    }

    public static EntityType searchEntity(Player caller, String arg, boolean inform) {
        UtilEnt.populate();
        arg = arg.toLowerCase();
        LinkedList<EntityType> matchList = new LinkedList<EntityType>();
        for (String cur : creatureMap.keySet()) {
            if (cur.equalsIgnoreCase(arg)) {
                return creatureMap.get(cur);
            }
            if (!cur.toLowerCase().contains(arg)) continue;
            matchList.add(creatureMap.get(cur));
        }
        if (matchList.size() != 1) {
            if (!inform) {
                return null;
            }
            UtilPlayer.message((Entity)caller, F.main("Creature Search", "" + C.mCount + matchList.size() + C.mBody + " matches for [" + C.mElem + arg + C.mBody + "]."));
            if (matchList.size() > 0) {
                String matchString = "";
                for (EntityType cur : matchList) {
                    matchString = matchString + F.elem(cur.getName()) + ", ";
                }
                if (matchString.length() > 1) {
                    matchString = matchString.substring(0, matchString.length() - 2);
                }
                UtilPlayer.message((Entity)caller, F.main("Creature Search", "" + C.mBody + "Matches [" + C.mElem + matchString + C.mBody + "]."));
            }
            return null;
        }
        return (EntityType)matchList.get(0);
    }

    public static HashMap<LivingEntity, Double> getInRadiusLiv(Location loc, double dR) {
        HashMap<LivingEntity, Double> ents = new HashMap<LivingEntity, Double>();
        for (Entity cur : loc.getWorld().getEntities()) {
            LivingEntity ent;
            double offset;
            if (!(cur instanceof LivingEntity) || (offset = UtilMath.offset(loc, (ent = (LivingEntity)cur).getLocation())) >= dR) continue;
            ents.put(ent, 1.0 - offset / dR);
        }
        return ents;
    }

    public static ArrayList<Entity> getInRadius(Location loc, double dR) {
        ArrayList<Entity> ents = new ArrayList<Entity>();
        for (Entity cur : loc.getWorld().getEntities()) {
            double offset = UtilMath.offset(loc, cur.getLocation());
            if (offset >= dR) continue;
            ents.add(cur);
        }
        return ents;
    }

    public static boolean hitBox(Location loc, LivingEntity ent, double mult, EntityType disguise) {
        if (disguise != null && disguise == EntityType.SQUID) {
            if (UtilMath.offset(loc, ent.getLocation().add(0.0, 0.4, 0.0)) < 0.6 * mult) {
                return true;
            }
            return false;
        }
        if (ent instanceof Player) {
            Player player = (Player)ent;
            if (UtilMath.offset(loc, player.getEyeLocation()) < 0.4 * mult) {
                return true;
            }
            if (UtilMath.offset2d(loc, player.getLocation()) < 0.6 * mult && loc.getY() > player.getLocation().getY() && loc.getY() < player.getEyeLocation().getY()) {
                return true;
            }
        } else if (ent instanceof Giant ? loc.getY() > ent.getLocation().getY() && loc.getY() < ent.getLocation().getY() + 12.0 && UtilMath.offset2d(loc, ent.getLocation()) < 4.0 : loc.getY() > ent.getLocation().getY() && loc.getY() < ent.getLocation().getY() + 2.0 && UtilMath.offset2d(loc, ent.getLocation()) < 0.5 * mult) {
            return true;
        }
        return false;
    }

    public static boolean isGrounded(Entity ent) {
        return UtilBlock.solid(ent.getLocation().getBlock().getRelative(BlockFace.DOWN));
    }

    public static void PlayDamageSound(LivingEntity damagee) {
        Sound sound = Sound.ENTITY_PLAYER_HURT;
        if (damagee.getType() == EntityType.ARMOR_STAND) {
            sound = Sound.ENTITY_ARMORSTAND_HIT;
        } else if (damagee.getType() == EntityType.BAT) {
            sound = Sound.ENTITY_BAT_HURT;
        } else if (damagee.getType() == EntityType.BLAZE) {
            sound = Sound.ENTITY_BLAZE_HURT;
        } else if (damagee.getType() == EntityType.CAVE_SPIDER) {
            sound = Sound.ENTITY_SPIDER_HURT;
        } else if (damagee.getType() == EntityType.CHICKEN) {
            sound = Sound.ENTITY_CHICKEN_HURT;
        } else if (damagee.getType() == EntityType.COW) {
            sound = Sound.ENTITY_COW_HURT;
        } else if (damagee.getType() == EntityType.CREEPER) {
            sound = Sound.ENTITY_CREEPER_HURT;
        } else if (damagee.getType() == EntityType.ENDER_DRAGON) {
            sound = Sound.ENTITY_ENDERDRAGON_HURT;
        } else if (damagee.getType() == EntityType.ENDERMAN) {
            sound = Sound.ENTITY_ENDERMEN_HURT;
        } else if (damagee.getType() == EntityType.GHAST) {
            sound = Sound.ENTITY_GHAST_HURT;
        } else if (damagee.getType() == EntityType.GIANT) {
            sound = Sound.ENTITY_ZOMBIE_HURT;
        } else if (damagee.getType() == EntityType.HORSE) {
            sound = Sound.ENTITY_HORSE_HURT;
        } else if (damagee.getType() == EntityType.IRON_GOLEM) {
            sound = Sound.ENTITY_IRONGOLEM_HURT;
        } else if (damagee.getType() == EntityType.MAGMA_CUBE) {
            sound = Sound.ENTITY_MAGMACUBE_HURT;
        } else if (damagee.getType() == EntityType.MUSHROOM_COW) {
            sound = Sound.ENTITY_COW_HURT;
        } else if (damagee.getType() == EntityType.OCELOT) {
            sound = Sound.ENTITY_CAT_HURT;
        } else if (damagee.getType() == EntityType.PIG) {
            sound = Sound.ENTITY_PIG_HURT;
        } else if (damagee.getType() == EntityType.PIG_ZOMBIE) {
            sound = Sound.ENTITY_ZOMBIE_HURT;
        } else if (damagee.getType() == EntityType.SHEEP) {
            sound = Sound.ENTITY_SHEEP_HURT;
        } else if (damagee.getType() == EntityType.SILVERFISH) {
            sound = Sound.ENTITY_SILVERFISH_HURT;
        } else if (damagee.getType() == EntityType.SKELETON) {
            sound = Sound.ENTITY_SKELETON_HURT;
        } else if (damagee.getType() == EntityType.SLIME) {
            sound = Sound.ENTITY_SLIME_HURT;
        } else if (damagee.getType() == EntityType.SNOWMAN) {
            sound = Sound.ENTITY_SNOWMAN_HURT;
        } else if (damagee.getType() == EntityType.SPIDER) {
            sound = Sound.ENTITY_SPIDER_HURT;
        } else if (damagee.getType() == EntityType.SQUID) {
            sound = Sound.ENTITY_SQUID_HURT;
        } else if (damagee.getType() == EntityType.VILLAGER) {
            sound = Sound.ENTITY_VILLAGER_HURT;
        } else if (damagee.getType() == EntityType.WITCH) {
            sound = Sound.ENTITY_WITCH_HURT;
        } else if (damagee.getType() == EntityType.WITHER) {
            sound = Sound.ENTITY_WITHER_HURT;
        } else if (damagee.getType() == EntityType.WOLF) {
            sound = Sound.ENTITY_WOLF_HURT;
        } else if (damagee.getType() == EntityType.ZOMBIE) {
            sound = Sound.ENTITY_ZOMBIE_HURT;
        }
        damagee.getWorld().playSound(damagee.getLocation(), sound, 1.5f + (float)(0.5 * Math.random()), 0.8f + (float)(0.4000000059604645 * Math.random()));
    }

    public static boolean onBlock(Player player) {
        double xMod = player.getLocation().getX() % 1.0;
        if (player.getLocation().getX() < 0.0) {
            xMod += 1.0;
        }
        double zMod = player.getLocation().getZ() % 1.0;
        if (player.getLocation().getZ() < 0.0) {
            zMod += 1.0;
        }
        int xMin = 0;
        int xMax = 0;
        int zMin = 0;
        int zMax = 0;
        if (xMod < 0.3) {
            xMin = -1;
        }
        if (xMod > 0.7) {
            xMax = 1;
        }
        if (zMod < 0.3) {
            zMin = -1;
        }
        if (zMod > 0.7) {
            zMax = 1;
        }
        for (int x = xMin; x <= xMax; ++x) {
            for (int z = zMin; z <= zMax; ++z) {
                if (player.getLocation().add((double)x, -0.5, (double)z).getBlock().getType() != Material.AIR && !player.getLocation().add((double)x, -0.5, (double)z).getBlock().isLiquid()) {
                    return true;
                }
                if (player.getLocation().add((double)x, 0.0, (double)z).getBlock().getType() == Material.WATER_LILY) {
                    return true;
                }
                Material beneath = player.getLocation().add((double)x, -1.5, (double)z).getBlock().getType();
                if (player.getLocation().getY() % 0.5 != 0.0 || beneath != Material.FENCE && beneath != Material.FENCE_GATE && beneath != Material.NETHER_FENCE && beneath != Material.COBBLE_WALL) continue;
                return true;
            }
        }
        return false;
    }

    public static void CreatureMove(Entity ent, Location target, float speed) {
        if (!(ent instanceof Creature)) {
            return;
        }
        if (UtilMath.offset(ent.getLocation(), target) < 0.1) {
            return;
        }
        EntityCreature ec = ((CraftCreature)ent).getHandle();
        Navigation nav = (Navigation)ec.getNavigation();
        if (UtilMath.offset(ent.getLocation(), target) > 16.0) {
            Location newTarget = ent.getLocation();
            newTarget.add(UtilAlg.getTrajectory(ent.getLocation(), target).multiply(16));
            nav.a(newTarget.getX(), newTarget.getY(), newTarget.getZ(), (double)speed);
        } else {
            nav.a(target.getX(), target.getY(), target.getZ(), (double)speed);
        }
    }

    public static boolean CreatureMoveFast(Entity ent, Location target, float speed) {
        return UtilEnt.CreatureMoveFast(ent, target, speed, true);
    }

    public static boolean CreatureMoveFast(Entity ent, Location target, float speed, boolean slow) {
        if (!(ent instanceof Creature)) {
            return false;
        }
        if (UtilMath.offset(ent.getLocation(), target) < 0.1) {
            return false;
        }
        if (UtilMath.offset(ent.getLocation(), target) < 2.0) {
            speed = Math.min(speed, 1.0f);
        }
        EntityCreature ec = ((CraftCreature)ent).getHandle();
        ec.getControllerMove().a(target.getX(), target.getY(), target.getZ(), (double)speed);
        return true;
    }

    public static int getNewEntityId() {
        return UtilEnt.getNewEntityId(true);
    }

    public static int getNewEntityId(boolean modifynumber) {
        try {
            Field field = net.minecraft.server.v1_12_R1.Entity.class.getDeclaredField("entityCount");
            field.setAccessible(true);
            int entityId = field.getInt(null);
            if (modifynumber) {
                field.set(null, entityId + 1);
            }
            return entityId;
        }
        catch (Exception ex) {
            ex.printStackTrace();
            return -1;
        }
    }

    public static Entity getEntityById(int entityId) {
        for (World world : Bukkit.getWorlds()) {
            for (Entity entity : world.getEntities()) {
                if (entity.getEntityId() != entityId) continue;
                return entity;
            }
        }
        return null;
    }

    public static boolean inWater(LivingEntity ent) {
        return ent.getLocation().getBlock().getTypeId() == 8 || ent.getLocation().getBlock().getTypeId() == 9;
    }
}
