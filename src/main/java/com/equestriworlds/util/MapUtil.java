package com.equestriworlds.util;

import com.equestriworlds.util.UtilField;
import java.io.File;
import java.io.PrintStream;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import net.minecraft.server.v1_12_R1.BlockPosition;
import net.minecraft.server.v1_12_R1.Chunk;
import net.minecraft.server.v1_12_R1.ChunkProviderServer;
import net.minecraft.server.v1_12_R1.EntityPlayer;
import net.minecraft.server.v1_12_R1.ExceptionWorldConflict;
import net.minecraft.server.v1_12_R1.IBlockData;
import net.minecraft.server.v1_12_R1.IProgressUpdate;
import net.minecraft.server.v1_12_R1.MinecraftServer;
import net.minecraft.server.v1_12_R1.Packet;
import net.minecraft.server.v1_12_R1.PacketPlayOutMultiBlockChange;
import net.minecraft.server.v1_12_R1.PlayerConnection;
import net.minecraft.server.v1_12_R1.RegionFile;
import net.minecraft.server.v1_12_R1.RegionFileCache;
import net.minecraft.server.v1_12_R1.WorldServer;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.libs.it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import org.bukkit.craftbukkit.libs.it.unimi.dsi.fastutil.objects.ObjectCollection;
import org.bukkit.craftbukkit.libs.it.unimi.dsi.fastutil.objects.ObjectIterator;
import org.bukkit.craftbukkit.v1_12_R1.CraftServer;
import org.bukkit.craftbukkit.v1_12_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.world.WorldUnloadEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class MapUtil {
    public static void QuickChangeBlockAt(Location location, Material setTo) {
        MapUtil.QuickChangeBlockAt(location.getWorld(), location.getBlockX(), location.getBlockY(), location.getBlockZ(), setTo);
    }

    public static void QuickChangeBlockAt(Location location, int id, byte data) {
        MapUtil.QuickChangeBlockAt(location.getWorld(), location.getBlockX(), location.getBlockY(), location.getBlockZ(), id, (int)data);
    }

    public static void QuickChangeBlockAt(World world, int x, int y, int z, Material setTo) {
        MapUtil.QuickChangeBlockAt(world, x, y, z, setTo, 0);
    }

    public static void QuickChangeBlockAt(World world, int x, int y, int z, Material setTo, int data) {
        MapUtil.QuickChangeBlockAt(world, x, y, z, setTo.getId(), data);
    }

    public static void QuickChangeBlockAt(World world, int x, int y, int z, int id, int data) {
        WorldServer w = ((CraftWorld)world).getHandle();
        Chunk chunk = w.getChunkAt(x >> 4, z >> 4);
        BlockPosition bp = new BlockPosition(x, y, z);
        int combined = id + (data << 12);
        IBlockData ibd = net.minecraft.server.v1_12_R1.Block.getByCombinedId((int)combined);
        w.setTypeAndData(bp, ibd, 2);
        chunk.a(bp, ibd);
    }

    public static int GetHighestBlockInCircleAt(World world, int bx, int bz, int radius) {
        int count = 0;
        int totalHeight = 0;
        double invRadiusX = 1 / radius;
        double invRadiusZ = 1 / radius;
        int ceilRadiusX = (int)Math.ceil(radius);
        int ceilRadiusZ = (int)Math.ceil(radius);
        double nextXn = 0.0;
        block0 : for (int x = 0; x <= ceilRadiusX; ++x) {
            double xn = nextXn;
            nextXn = (double)(x + 1) * invRadiusX;
            double nextZn = 0.0;
            for (int z = 0; z <= ceilRadiusZ; ++z) {
                double zn = nextZn;
                nextZn = (double)(z + 1) * invRadiusZ;
                double distanceSq = xn * xn + zn * zn;
                if (distanceSq > 1.0) {
                    if (z != 0) continue block0;
                    break block0;
                }
                totalHeight += world.getHighestBlockAt(bx + x, bz + z).getY();
                ++count;
            }
        }
        return totalHeight / count;
    }

    public static void ChunkBlockSet(World world, int x, int y, int z, int id, byte data, boolean notifyPlayers) {
        world.getBlockAt(x, y, z).setTypeIdAndData(id, data, notifyPlayers);
    }

    public static void SendMultiBlockForPlayer(int x, int z, short[] dirtyBlocks, int dirtyCount, World world, Player player) {
        ((CraftPlayer)player).getHandle().playerConnection.sendPacket((Packet)new PacketPlayOutMultiBlockChange(dirtyCount, dirtyBlocks, ((CraftWorld)world).getHandle().getChunkAt(x, z)));
    }

    public static void UnloadWorld(JavaPlugin plugin, World world) {
        MapUtil.UnloadWorld(plugin, world, false);
    }

    public static void UnloadWorld(JavaPlugin plugin, World world, boolean save) {
        if (save) {
            try {
                ((CraftWorld)world).getHandle().save(true, (IProgressUpdate)null);
            }
            catch (ExceptionWorldConflict e) {
                e.printStackTrace();
            }
            ((CraftWorld)world).getHandle().saveLevel();
        }
        world.setAutoSave(save);
        CraftServer server = (CraftServer)plugin.getServer();
        CraftWorld craftWorld = (CraftWorld)world;
        Bukkit.getPluginManager().callEvent((Event)new WorldUnloadEvent((World)((CraftWorld)world).getHandle().getWorld()));
        ObjectIterator chunkIterator = ((CraftWorld)world).getHandle().getChunkProviderServer().chunks.values().iterator();
        for (Entity entity : world.getEntities()) {
            entity.remove();
        }
        while (chunkIterator.hasNext()) {
            Chunk chunk = (Chunk)chunkIterator.next();
            chunk.removeEntities();
        }
        ((CraftWorld)world).getHandle().getChunkProviderServer().chunks.clear();
        ((CraftWorld)world).getHandle().getChunkProviderServer().unloadQueue.clear();
        try {
            Field f = server.getClass().getDeclaredField("worlds");
            f.setAccessible(true);
            Map worlds = (Map)f.get((Object)server);
            worlds.remove(world.getName().toLowerCase());
            f.setAccessible(false);
        }
        catch (IllegalAccessException ex) {
            System.out.println("Error removing world from bukkit master list: " + ex.getMessage());
        }
        catch (NoSuchFieldException ex) {
            System.out.println("Error removing world from bukkit master list: " + ex.getMessage());
        }
        MinecraftServer ms = null;
        try {
            Field f = server.getClass().getDeclaredField("console");
            f.setAccessible(true);
            ms = (MinecraftServer)f.get((Object)server);
            f.setAccessible(false);
        }
        catch (IllegalAccessException ex) {
            System.out.println("Error getting minecraftserver variable: " + ex.getMessage());
        }
        catch (NoSuchFieldException ex) {
            System.out.println("Error getting minecraftserver variable: " + ex.getMessage());
        }
        ms.worlds.remove(ms.worlds.indexOf((Object)craftWorld.getHandle()));
    }

    public static boolean ClearWorldReferences(String worldName) {
        Field field = UtilField.getField(RegionFileCache.class, "a");
        Map<File, RegionFile> regionfiles = null;
        try {
            regionfiles = (Map<File, RegionFile>)field.get(RegionFileCache.class);
        }
        catch (IllegalAccessException | IllegalArgumentException e1) {
            System.out.println("ERROR IN CLEARING WORLD REFERENCES! MAPUTIL!");
            e1.printStackTrace();
            return false;
        }
        try {
            Iterator<Map.Entry<File, RegionFile>> iterator = regionfiles.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry e = iterator.next();
                RegionFile file = (RegionFile)e.getValue();
                try {
                    file.c();
                    iterator.remove();
                }
                catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }
        catch (Exception ex) {
            System.out.println("Exception while removing world reference for '" + worldName + "'!");
            ex.printStackTrace();
        }
        return true;
    }
}
