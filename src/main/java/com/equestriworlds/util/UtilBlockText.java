/*
 * Decompiled with CFR 0_133.
 * 
 * Could not load the following classes:
 *  org.bukkit.Location
 *  org.bukkit.World
 *  org.bukkit.block.Block
 *  org.bukkit.block.BlockFace
 */
package com.equestriworlds.util;

import com.equestriworlds.util.MapUtil;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;

public class UtilBlockText {
    public static HashMap<Character, int[][]> alphabet = new HashMap();

    public static ArrayList<Location> GetTextLocations(String string, Location loc, BlockFace face) {
        if (alphabet.isEmpty()) {
            UtilBlockText.PopulateAlphabet();
        }
        ArrayList<Location> locs = new ArrayList<Location>();
        Block block = loc.getBlock();
        int width = 0;
        for (char c : string.toLowerCase().toCharArray()) {
            int[][] letter = alphabet.get(Character.valueOf(c));
            if (letter == null) continue;
            width += (letter[0].length + 1) * 3;
        }
        block = block.getRelative(face, -1 * width / 2 + 1);
        World world = block.getWorld();
        int bX = block.getX();
        int bY = block.getY();
        int bZ = block.getZ();
        for (char c : string.toLowerCase().toCharArray()) {
            int[][] letter = alphabet.get(c);
            if (letter == null) continue;
            for (int x = 0; x < letter.length; ++x) {
                for (int y = 0; y < letter[x].length; ++y) {
                    if (letter[x][y] == 1) {
                        locs.add(new Location(world, (double)bX, (double)bY, (double)bZ));
                    }
                    bX += face.getModX() * 3;
                    bY += face.getModY() * 3;
                    bZ += face.getModZ() * 3;
                }
                bX += face.getModX() * -3 * letter[x].length;
                bY += face.getModY() * -3 * letter[x].length;
                bZ += face.getModZ() * -3 * letter[x].length;
                bY -= 3;
            }
            bY += 15;
            bX += face.getModX() * (letter[0].length + 1) * 3;
            bY += face.getModY() * (letter[0].length + 1) * 3;
            bZ += face.getModZ() * (letter[0].length + 1) * 3;
        }
        return locs;
    }

    public static Collection<Block> MakeText(String string, Location loc, BlockFace face, int id, byte data, TextAlign align) {
        return UtilBlockText.MakeText(string, loc, face, id, data, align, true);
    }

    public static Collection<Block> MakeText(String string, Location loc, BlockFace face, int id, byte data, TextAlign align, boolean setAir) {
        World world;
        int bZ;
        int bY;
        int bX;
        HashSet<Block> changes = new HashSet<Block>();
        if (alphabet.isEmpty()) {
            UtilBlockText.PopulateAlphabet();
        }
        Block block = loc.getBlock();
        int width = 0;
        for (char c : string.toLowerCase().toCharArray()) {
            int[][] letter = alphabet.get(Character.valueOf(c));
            if (letter == null) continue;
            width += letter[0].length + 1;
        }
        if (align == TextAlign.CENTER || align == TextAlign.RIGHT) {
            int divisor = 1;
            if (align == TextAlign.CENTER) {
                divisor = 2;
            }
            block = block.getRelative(face, -1 * width / divisor + 1);
        }
        if (setAir) {
            world = loc.getWorld();
            bX = loc.getBlockX();
            bY = loc.getBlockY();
            bZ = loc.getBlockZ();
            for (int y = 0; y < 5; ++y) {
                int i;
                if (align == TextAlign.CENTER) {
                    for (i = -64; i <= 64; ++i) {
                        if (world.getBlockAt(bX + i * face.getModX(), bY + i * face.getModY(), bZ + i * face.getModZ()).getTypeId() != id) continue;
                        MapUtil.QuickChangeBlockAt(world, bX + i * face.getModX(), bY + i * face.getModY(), bZ + i * face.getModZ(), 0, 0);
                    }
                }
                if (align == TextAlign.LEFT) {
                    for (i = 0; i <= 128; ++i) {
                        if (world.getBlockAt(bX + i * face.getModX(), bY + i * face.getModY(), bZ + i * face.getModZ()).getTypeId() != id) continue;
                        MapUtil.QuickChangeBlockAt(world, bX + i * face.getModX(), bY + i * face.getModY(), bZ + i * face.getModZ(), 0, 0);
                    }
                }
                if (align == TextAlign.RIGHT) {
                    for (i = -128; i <= 0; ++i) {
                        if (world.getBlockAt(bX + i * face.getModX(), bY + i * face.getModY(), bZ + i * face.getModZ()).getTypeId() != id) continue;
                        MapUtil.QuickChangeBlockAt(world, bX + i * face.getModX(), bY + i * face.getModY(), bZ + i * face.getModZ(), 0, 0);
                    }
                }
                --bY;
            }
        }
        world = block.getWorld();
        bX = block.getX();
        bY = block.getY();
        bZ = block.getZ();
        for (char c : string.toLowerCase().toCharArray()) {
            int[][] letter = alphabet.get(Character.valueOf(c));
            if (letter == null) continue;
            for (int x = 0; x < letter.length; ++x) {
                for (int y = 0; y < letter[x].length; ++y) {
                    if (letter[x][y] == 1) {
                        changes.add(world.getBlockAt(bX, bY, bZ));
                        MapUtil.QuickChangeBlockAt(world, bX, bY, bZ, id, (int)data);
                    }
                    bX += face.getModX();
                    bY += face.getModY();
                    bZ += face.getModZ();
                }
                bX += face.getModX() * -1 * letter[x].length;
                bY += face.getModY() * -1 * letter[x].length;
                bZ += face.getModZ() * -1 * letter[x].length;
                --bY;
            }
            bY += 5;
            bX += face.getModX() * (letter[0].length + 1);
            bY += face.getModY() * (letter[0].length + 1);
            bZ += face.getModZ() * (letter[0].length + 1);
        }
        return changes;
    }

    private static void PopulateAlphabet() {
        alphabet.put(Character.valueOf('0'), new int[][]{{1, 1, 1}, {1, 0, 1}, {1, 0, 1}, {1, 0, 1}, {1, 1, 1}});
        alphabet.put(Character.valueOf('1'), new int[][]{{1, 1, 0}, {0, 1, 0}, {0, 1, 0}, {0, 1, 0}, {1, 1, 1}});
        alphabet.put(Character.valueOf('2'), new int[][]{{1, 1, 1}, {0, 0, 1}, {1, 1, 1}, {1, 0, 0}, {1, 1, 1}});
        alphabet.put(Character.valueOf('3'), new int[][]{{1, 1, 1}, {0, 0, 1}, {1, 1, 1}, {0, 0, 1}, {1, 1, 1}});
        alphabet.put(Character.valueOf('4'), new int[][]{{1, 0, 1}, {1, 0, 1}, {1, 1, 1}, {0, 0, 1}, {0, 0, 1}});
        alphabet.put(Character.valueOf('5'), new int[][]{{1, 1, 1}, {1, 0, 0}, {1, 1, 1}, {0, 0, 1}, {1, 1, 1}});
        alphabet.put(Character.valueOf('6'), new int[][]{{1, 1, 1}, {1, 0, 0}, {1, 1, 1}, {1, 0, 1}, {1, 1, 1}});
        alphabet.put(Character.valueOf('7'), new int[][]{{1, 1, 1}, {0, 0, 1}, {0, 0, 1}, {0, 0, 1}, {0, 0, 1}});
        alphabet.put(Character.valueOf('8'), new int[][]{{1, 1, 1}, {1, 0, 1}, {1, 1, 1}, {1, 0, 1}, {1, 1, 1}});
        alphabet.put(Character.valueOf('9'), new int[][]{{1, 1, 1}, {1, 0, 1}, {1, 1, 1}, {0, 0, 1}, {1, 1, 1}});
        alphabet.put(Character.valueOf('.'), new int[][]{{0}, {0}, {0}, {0}, {1}});
        alphabet.put(Character.valueOf('!'), new int[][]{{1}, {1}, {1}, {0}, {1}});
        alphabet.put(Character.valueOf('_'), new int[][]{{0, 0, 0, 0}, {0, 0, 0, 0}, {0, 0, 0, 0}, {0, 0, 0, 0}, {1, 1, 1, 1}});
        alphabet.put(Character.valueOf('-'), new int[][]{{0, 0, 0}, {0, 0, 0}, {1, 1, 1}, {0, 0, 0}, {0, 0, 0}});
        alphabet.put(Character.valueOf('+'), new int[][]{{0, 0, 0}, {0, 1, 0}, {1, 1, 1}, {0, 1, 0}, {0, 0, 0}});
        alphabet.put(Character.valueOf('='), new int[][]{{0, 0, 0}, {1, 1, 1}, {0, 0, 0}, {1, 1, 1}, {0, 0, 0}});
        alphabet.put(Character.valueOf('^'), new int[][]{{0, 0, 1, 0, 0}, {0, 1, 0, 1, 0}, {1, 0, 0, 0, 1}, {0, 0, 0, 0, 0}, {0, 0, 0, 0, 0}});
        alphabet.put(Character.valueOf('('), new int[][]{{0, 1}, {1, 0}, {1, 0}, {1, 0}, {0, 1}});
        alphabet.put(Character.valueOf(')'), new int[][]{{1, 0}, {0, 1}, {0, 1}, {0, 1}, {1, 0}});
        alphabet.put(Character.valueOf(':'), new int[][]{{0}, {1}, {0}, {1}, {0}});
        alphabet.put(Character.valueOf('<'), new int[][]{{0, 0, 1}, {0, 1, 0}, {1, 0, 0}, {0, 1, 0}, {0, 0, 1}});
        alphabet.put(Character.valueOf('>'), new int[][]{{1, 0, 0}, {0, 1, 0}, {0, 0, 1}, {0, 1, 0}, {1, 0, 0}});
        alphabet.put(Character.valueOf('\"'), new int[][]{{1, 0, 1}, {1, 0, 1}, {0, 0, 0}, {0, 0, 0}, {0, 0, 0}});
        alphabet.put(Character.valueOf('\''), new int[][]{{1}, {1}, {0}, {0}, {0}});
        alphabet.put(Character.valueOf('`'), new int[][]{{1, 0}, {1, 0}, {0, 1}, {0, 0}, {0, 0}});
        alphabet.put(Character.valueOf('~'), new int[][]{{0, 1, 0, 1}, {1, 0, 1, 0}, {0, 0, 0, 0}, {0, 0, 0, 0}, {0, 0, 0, 0}});
        alphabet.put(Character.valueOf(' '), new int[][]{{0, 0}, {0, 0}, {0, 0}, {0, 0}, {0, 0}});
        alphabet.put(Character.valueOf('a'), new int[][]{{1, 1, 1, 1}, {1, 0, 0, 1}, {1, 1, 1, 1}, {1, 0, 0, 1}, {1, 0, 0, 1}});
        alphabet.put(Character.valueOf('b'), new int[][]{{1, 1, 1, 0}, {1, 0, 0, 1}, {1, 1, 1, 0}, {1, 0, 0, 1}, {1, 1, 1, 0}});
        alphabet.put(Character.valueOf('c'), new int[][]{{1, 1, 1, 1}, {1, 0, 0, 0}, {1, 0, 0, 0}, {1, 0, 0, 0}, {1, 1, 1, 1}});
        alphabet.put(Character.valueOf('d'), new int[][]{{1, 1, 1, 0}, {1, 0, 0, 1}, {1, 0, 0, 1}, {1, 0, 0, 1}, {1, 1, 1, 0}});
        alphabet.put(Character.valueOf('e'), new int[][]{{1, 1, 1, 1}, {1, 0, 0, 0}, {1, 1, 1, 0}, {1, 0, 0, 0}, {1, 1, 1, 1}});
        alphabet.put(Character.valueOf('f'), new int[][]{{1, 1, 1, 1}, {1, 0, 0, 0}, {1, 1, 1, 0}, {1, 0, 0, 0}, {1, 0, 0, 0}});
        alphabet.put(Character.valueOf('g'), new int[][]{{1, 1, 1, 1}, {1, 0, 0, 0}, {1, 0, 1, 1}, {1, 0, 0, 1}, {1, 1, 1, 1}});
        alphabet.put(Character.valueOf('h'), new int[][]{{1, 0, 0, 1}, {1, 0, 0, 1}, {1, 1, 1, 1}, {1, 0, 0, 1}, {1, 0, 0, 1}});
        alphabet.put(Character.valueOf('i'), new int[][]{{1, 1, 1}, {0, 1, 0}, {0, 1, 0}, {0, 1, 0}, {1, 1, 1}});
        alphabet.put(Character.valueOf('j'), new int[][]{{1, 1, 1, 1}, {0, 0, 1, 0}, {0, 0, 1, 0}, {1, 0, 1, 0}, {1, 1, 1, 0}});
        alphabet.put(Character.valueOf('k'), new int[][]{{1, 0, 0, 1}, {1, 0, 1, 0}, {1, 1, 0, 0}, {1, 0, 1, 0}, {1, 0, 0, 1}});
        alphabet.put(Character.valueOf('l'), new int[][]{{1, 0, 0, 0}, {1, 0, 0, 0}, {1, 0, 0, 0}, {1, 0, 0, 0}, {1, 1, 1, 1}});
        alphabet.put(Character.valueOf('m'), new int[][]{{1, 1, 1, 1, 1}, {1, 0, 1, 0, 1}, {1, 0, 1, 0, 1}, {1, 0, 0, 0, 1}, {1, 0, 0, 0, 1}});
        alphabet.put(Character.valueOf('n'), new int[][]{{1, 0, 0, 1}, {1, 1, 0, 1}, {1, 0, 1, 1}, {1, 0, 0, 1}, {1, 0, 0, 1}});
        alphabet.put(Character.valueOf('o'), new int[][]{{1, 1, 1, 1}, {1, 0, 0, 1}, {1, 0, 0, 1}, {1, 0, 0, 1}, {1, 1, 1, 1}});
        alphabet.put(Character.valueOf('p'), new int[][]{{1, 1, 1, 1}, {1, 0, 0, 1}, {1, 1, 1, 1}, {1, 0, 0, 0}, {1, 0, 0, 0}});
        alphabet.put(Character.valueOf('q'), new int[][]{{1, 1, 1, 1}, {1, 0, 0, 1}, {1, 0, 0, 1}, {1, 0, 1, 0}, {1, 1, 0, 1}});
        alphabet.put(Character.valueOf('r'), new int[][]{{1, 1, 1, 1}, {1, 0, 0, 1}, {1, 1, 1, 0}, {1, 0, 0, 1}, {1, 0, 0, 1}});
        alphabet.put(Character.valueOf('s'), new int[][]{{1, 1, 1, 1}, {1, 0, 0, 0}, {1, 1, 1, 1}, {0, 0, 0, 1}, {1, 1, 1, 1}});
        alphabet.put(Character.valueOf('t'), new int[][]{{1, 1, 1, 1, 1}, {0, 0, 1, 0, 0}, {0, 0, 1, 0, 0}, {0, 0, 1, 0, 0}, {0, 0, 1, 0, 0}});
        alphabet.put(Character.valueOf('u'), new int[][]{{1, 0, 0, 1}, {1, 0, 0, 1}, {1, 0, 0, 1}, {1, 0, 0, 1}, {1, 1, 1, 1}});
        alphabet.put(Character.valueOf('v'), new int[][]{{1, 0, 0, 1}, {1, 0, 0, 1}, {1, 0, 0, 1}, {1, 0, 0, 1}, {0, 1, 1, 0}});
        alphabet.put(Character.valueOf('w'), new int[][]{{1, 0, 0, 0, 1}, {1, 0, 0, 0, 1}, {1, 0, 1, 0, 1}, {1, 0, 1, 0, 1}, {1, 1, 1, 1, 1}});
        alphabet.put(Character.valueOf('x'), new int[][]{{1, 0, 0, 1}, {1, 0, 0, 1}, {0, 1, 1, 0}, {1, 0, 0, 1}, {1, 0, 0, 1}});
        alphabet.put(Character.valueOf('y'), new int[][]{{1, 0, 0, 1}, {1, 0, 0, 1}, {1, 1, 1, 1}, {0, 0, 0, 1}, {1, 1, 1, 1}});
        alphabet.put(Character.valueOf('z'), new int[][]{{1, 1, 1, 1}, {0, 0, 0, 1}, {0, 0, 1, 0}, {0, 1, 0, 0}, {1, 1, 1, 1}});
    }

    public static enum TextAlign {
        LEFT,
        RIGHT,
        CENTER;
        

        private TextAlign() {
        }
    }

}
