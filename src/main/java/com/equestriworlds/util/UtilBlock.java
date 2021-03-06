package com.equestriworlds.util;

import com.equestriworlds.util.UtilMath;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import net.minecraft.server.v1_12_R1.MathHelper;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 * Block properties such as transparency.
 * Doubling what can already be done via Bukkit.
 */
public class UtilBlock {
    public static HashSet<Byte> blockUseSet = new HashSet();
    public static HashSet<Byte> fullSolid = new HashSet();
    public static HashSet<Byte> blockPassSet = new HashSet();
    public static HashSet<Byte> blockAirFoliageSet = new HashSet();

    public static void main(String[] args) {
        for (Material m : Material.values()) {
            boolean solid;
            boolean thisSolid = UtilBlock.fullSolid(m.getId());
            if (thisSolid == (solid = m.isSolid())) continue;
            StringBuilder sb = new StringBuilder();
            sb.append("Failed: ");
            sb.append(m.name());
            int amount = 40 - sb.length();
            for (int i = 0; i < amount; ++i) {
                sb.append(" ");
            }
            sb.append(thisSolid);
            System.out.println(sb);
        }
        System.out.println("done!");
    }

    public static boolean solid(Block block) {
        if (block == null) {
            return false;
        }
        return UtilBlock.solid(block.getTypeId());
    }

    public static boolean solid(int block) {
        return UtilBlock.solid((byte)block);
    }

    public static boolean solid(byte block) {
        return !blockPassSet.contains(block);
    }

    public static boolean airFoliage(Block block) {
        if (block == null) {
            return false;
        }
        return UtilBlock.airFoliage(block.getTypeId());
    }

    public static boolean airFoliage(int block) {
        return UtilBlock.airFoliage((byte)block);
    }

    public static boolean airFoliage(byte block) {
        return blockAirFoliageSet.contains(block);
    }

    public static boolean fullSolid(Block block) {
        if (block == null) {
            return false;
        }
        return UtilBlock.fullSolid(block.getTypeId());
    }

    public static boolean fullSolid(int block) {
        return UtilBlock.fullSolid((byte)block);
    }

    public static boolean fullSolid(byte block) {
        return fullSolid.contains(block);
    }

    public static boolean usable(Block block) {
        if (block == null) {
            return false;
        }
        return UtilBlock.usable(block.getTypeId());
    }

    public static boolean usable(int block) {
        return UtilBlock.usable((byte)block);
    }

    public static boolean usable(byte block) {
        return blockUseSet.contains(block);
    }

    public static HashMap<Block, Double> getInRadius(Location loc, double dR) {
        return UtilBlock.getInRadius(loc, dR, 9999.0);
    }

    public static HashMap<Block, Double> getInRadius(Location loc, double dR, double maxHeight) {
        HashMap<Block, Double> blockList = new HashMap<Block, Double>();
        int iR = (int)dR + 1;
        for (int x = - iR; x <= iR; ++x) {
            for (int z = - iR; z <= iR; ++z) {
                for (int y = - iR; y <= iR; ++y) {
                    Block curBlock;
                    double offset;
                    if ((double)Math.abs(y) > maxHeight || (offset = UtilMath.offset(loc, (curBlock = loc.getWorld().getBlockAt((int)(loc.getX() + (double)x), (int)(loc.getY() + (double)y), (int)(loc.getZ() + (double)z))).getLocation().add(0.5, 0.5, 0.5))) > dR) continue;
                    blockList.put(curBlock, 1.0 - offset / dR);
                }
            }
        }
        return blockList;
    }

    public static HashMap<Block, Double> getInRadius(Block block, double dR) {
        return UtilBlock.getInRadius(block, dR, false);
    }

    public static HashMap<Block, Double> getInRadius(Block block, double dR, boolean hollow) {
        HashMap<Block, Double> blockList = new HashMap<Block, Double>();
        int iR = (int)dR + 1;
        for (int x = - iR; x <= iR; ++x) {
            for (int z = - iR; z <= iR; ++z) {
                for (int y = - iR; y <= iR; ++y) {
                    Block curBlock = block.getRelative(x, y, z);
                    double offset = UtilMath.offset(block.getLocation(), curBlock.getLocation());
                    if (offset > dR || hollow && offset < dR - 1.0) continue;
                    blockList.put(curBlock, 1.0 - offset / dR);
                }
            }
        }
        return blockList;
    }

    public static ArrayList<Block> getInSquare(Block block, double dR) {
        ArrayList<Block> blockList = new ArrayList<Block>();
        int iR = (int)dR + 1;
        for (int x = - iR; x <= iR; ++x) {
            for (int z = - iR; z <= iR; ++z) {
                for (int y = - iR; y <= iR; ++y) {
                    blockList.add(block.getRelative(x, y, z));
                }
            }
        }
        return blockList;
    }

    public static boolean isBlock(ItemStack item) {
        if (item == null) {
            return false;
        }
        return item.getTypeId() > 0 && item.getTypeId() < 256;
    }

    public static Block getHighest(World world, int x, int z) {
        return UtilBlock.getHighest(world, x, z, null);
    }

    public static Block getHighest(World world, int x, int z, HashSet<Material> ignore) {
        Block block = world.getHighestBlockAt(x, z);
        while (block.getY() > 0 && (UtilBlock.airFoliage(block) || block.getType() == Material.LEAVES || ignore != null && ignore.contains((Object)block.getType()))) {
            block = block.getRelative(BlockFace.DOWN);
        }
        return block.getRelative(BlockFace.UP);
    }

    public static ArrayList<Block> getExplosionBlocks(Location location, float strength, boolean damageBlocksEqually) {
        ArrayList<Block> toExplode = new ArrayList<Block>();
        for (int i = 0; i < 16; ++i) {
            for (int j = 0; j < 16; ++j) {
                for (int k = 0; k < 16; ++k) {
                    if (i != 0 && i != 15 && j != 0 && j != 15 && k != 0 && k != 15) continue;
                    double d3 = (float)i / 15.0f * 2.0f - 1.0f;
                    double d4 = (float)j / 15.0f * 2.0f - 1.0f;
                    double d5 = (float)k / 15.0f * 2.0f - 1.0f;
                    double d6 = Math.sqrt(d3 * d3 + d4 * d4 + d5 * d5);
                    d3 /= d6;
                    d4 /= d6;
                    d5 /= d6;
                    double d0 = location.getX();
                    double d1 = location.getY();
                    double d2 = location.getZ();
                    float f2 = 0.3f;
                    for (float f1 = strength * (0.7f + UtilMath.random.nextFloat() * 0.6f); f1 > 0.0f; f1 -= f2 * 0.75f) {
                        int l = MathHelper.floor((double)d0);
                        int i1 = MathHelper.floor((double)d1);
                        int j1 = MathHelper.floor((double)d2);
                        Block block = location.getWorld().getBlockAt(l, i1, j1);
                        if (f1 > 0.0f && i1 < 256 && i1 >= 0) {
                            toExplode.add(block);
                        }
                        d0 += d3 * (double)f2;
                        d1 += d4 * (double)f2;
                        d2 += d5 * (double)f2;
                    }
                }
            }
        }
        return toExplode;
    }

    public static ArrayList<Block> getSurrounding(Block block, boolean diagonals) {
        ArrayList<Block> blocks = new ArrayList<Block>();
        if (diagonals) {
            for (int x = -1; x <= 1; ++x) {
                for (int y = -1; y <= 1; ++y) {
                    for (int z = -1; z <= 1; ++z) {
                        if (x == 0 && y == 0 && z == 0) continue;
                        blocks.add(block.getRelative(x, y, z));
                    }
                }
            }
        } else {
            blocks.add(block.getRelative(BlockFace.UP));
            blocks.add(block.getRelative(BlockFace.DOWN));
            blocks.add(block.getRelative(BlockFace.NORTH));
            blocks.add(block.getRelative(BlockFace.SOUTH));
            blocks.add(block.getRelative(BlockFace.EAST));
            blocks.add(block.getRelative(BlockFace.WEST));
        }
        return blocks;
    }

    public static boolean isVisible(Block block) {
        for (Block other : UtilBlock.getSurrounding(block, false)) {
            if (other.getType().isOccluding()) continue;
            return true;
        }
        return false;
    }

    public static ArrayList<Block> getInBoundingBox(Location a, Location b) {
        ArrayList<Block> blocks = new ArrayList<Block>();
        for (int x = Math.min((int)a.getBlockX(), (int)b.getBlockX()); x <= Math.max(a.getBlockX(), b.getBlockX()); ++x) {
            for (int y = Math.min((int)a.getBlockY(), (int)b.getBlockY()); y <= Math.max(a.getBlockY(), b.getBlockY()); ++y) {
                for (int z = Math.min((int)a.getBlockZ(), (int)b.getBlockZ()); z <= Math.max(a.getBlockZ(), b.getBlockZ()); ++z) {
                    Block block = a.getWorld().getBlockAt(x, y, z);
                    if (block.getType() == Material.AIR) continue;
                    blocks.add(block);
                }
            }
        }
        return blocks;
    }

    public static /* varargs */ void setFakeBlock(World world, int x, int y, int z, Material material, byte data, Player ... players) {
    }

    static {
        blockAirFoliageSet.add((byte)0);
        blockAirFoliageSet.add((byte)6);
        blockAirFoliageSet.add((byte)31);
        blockAirFoliageSet.add((byte)32);
        blockAirFoliageSet.add((byte)37);
        blockAirFoliageSet.add((byte)38);
        blockAirFoliageSet.add((byte)39);
        blockAirFoliageSet.add((byte)40);
        blockAirFoliageSet.add((byte)51);
        blockAirFoliageSet.add((byte)59);
        blockAirFoliageSet.add((byte)104);
        blockAirFoliageSet.add((byte)105);
        blockAirFoliageSet.add((byte)115);
        blockAirFoliageSet.add((byte)-115);
        blockAirFoliageSet.add((byte)-114);
        blockPassSet.add((byte)0);
        blockPassSet.add((byte)6);
        blockPassSet.add((byte)8);
        blockPassSet.add((byte)9);
        blockPassSet.add((byte)10);
        blockPassSet.add((byte)11);
        blockPassSet.add((byte)26);
        blockPassSet.add((byte)27);
        blockPassSet.add((byte)28);
        blockPassSet.add((byte)30);
        blockPassSet.add((byte)31);
        blockPassSet.add((byte)32);
        blockPassSet.add((byte)37);
        blockPassSet.add((byte)38);
        blockPassSet.add((byte)39);
        blockPassSet.add((byte)40);
        blockPassSet.add((byte)50);
        blockPassSet.add((byte)51);
        blockPassSet.add((byte)55);
        blockPassSet.add((byte)59);
        blockPassSet.add((byte)63);
        blockPassSet.add((byte)64);
        blockPassSet.add((byte)65);
        blockPassSet.add((byte)66);
        blockPassSet.add((byte)68);
        blockPassSet.add((byte)69);
        blockPassSet.add((byte)70);
        blockPassSet.add((byte)71);
        blockPassSet.add((byte)72);
        blockPassSet.add((byte)75);
        blockPassSet.add((byte)76);
        blockPassSet.add((byte)77);
        blockPassSet.add((byte)78);
        blockPassSet.add((byte)83);
        blockPassSet.add((byte)90);
        blockPassSet.add((byte)92);
        blockPassSet.add((byte)93);
        blockPassSet.add((byte)94);
        blockPassSet.add((byte)96);
        blockPassSet.add((byte)101);
        blockPassSet.add((byte)102);
        blockPassSet.add((byte)104);
        blockPassSet.add((byte)105);
        blockPassSet.add((byte)106);
        blockPassSet.add((byte)107);
        blockPassSet.add((byte)111);
        blockPassSet.add((byte)115);
        blockPassSet.add((byte)116);
        blockPassSet.add((byte)117);
        blockPassSet.add((byte)118);
        blockPassSet.add((byte)119);
        blockPassSet.add((byte)120);
        blockPassSet.add((byte)-85);
        fullSolid.add((byte)1);
        fullSolid.add((byte)2);
        fullSolid.add((byte)3);
        fullSolid.add((byte)4);
        fullSolid.add((byte)5);
        fullSolid.add((byte)7);
        fullSolid.add((byte)12);
        fullSolid.add((byte)13);
        fullSolid.add((byte)14);
        fullSolid.add((byte)15);
        fullSolid.add((byte)16);
        fullSolid.add((byte)17);
        fullSolid.add((byte)19);
        fullSolid.add((byte)20);
        fullSolid.add((byte)21);
        fullSolid.add((byte)22);
        fullSolid.add((byte)23);
        fullSolid.add((byte)24);
        fullSolid.add((byte)25);
        fullSolid.add((byte)29);
        fullSolid.add((byte)33);
        fullSolid.add((byte)35);
        fullSolid.add((byte)41);
        fullSolid.add((byte)42);
        fullSolid.add((byte)43);
        fullSolid.add((byte)44);
        fullSolid.add((byte)45);
        fullSolid.add((byte)46);
        fullSolid.add((byte)47);
        fullSolid.add((byte)48);
        fullSolid.add((byte)49);
        fullSolid.add((byte)56);
        fullSolid.add((byte)57);
        fullSolid.add((byte)58);
        fullSolid.add((byte)60);
        fullSolid.add((byte)61);
        fullSolid.add((byte)62);
        fullSolid.add((byte)73);
        fullSolid.add((byte)74);
        fullSolid.add((byte)79);
        fullSolid.add((byte)80);
        fullSolid.add((byte)82);
        fullSolid.add((byte)84);
        fullSolid.add((byte)86);
        fullSolid.add((byte)87);
        fullSolid.add((byte)88);
        fullSolid.add((byte)89);
        fullSolid.add((byte)91);
        fullSolid.add((byte)95);
        fullSolid.add((byte)97);
        fullSolid.add((byte)98);
        fullSolid.add((byte)99);
        fullSolid.add((byte)100);
        fullSolid.add((byte)103);
        fullSolid.add((byte)110);
        fullSolid.add((byte)112);
        fullSolid.add((byte)121);
        fullSolid.add((byte)123);
        fullSolid.add((byte)124);
        fullSolid.add((byte)125);
        fullSolid.add((byte)126);
        fullSolid.add((byte)-127);
        fullSolid.add((byte)-123);
        fullSolid.add((byte)-119);
        fullSolid.add((byte)-118);
        fullSolid.add((byte)-104);
        fullSolid.add((byte)-103);
        fullSolid.add((byte)-101);
        fullSolid.add((byte)-98);
        blockUseSet.add((byte)23);
        blockUseSet.add((byte)26);
        blockUseSet.add((byte)33);
        blockUseSet.add((byte)47);
        blockUseSet.add((byte)54);
        blockUseSet.add((byte)58);
        blockUseSet.add((byte)61);
        blockUseSet.add((byte)62);
        blockUseSet.add((byte)64);
        blockUseSet.add((byte)69);
        blockUseSet.add((byte)71);
        blockUseSet.add((byte)77);
        blockUseSet.add((byte)85);
        blockUseSet.add((byte)93);
        blockUseSet.add((byte)94);
        blockUseSet.add((byte)96);
        blockUseSet.add((byte)107);
        blockUseSet.add((byte)113);
        blockUseSet.add((byte)116);
        blockUseSet.add((byte)117);
        blockUseSet.add((byte)-126);
        blockUseSet.add((byte)-111);
        blockUseSet.add((byte)-110);
        blockUseSet.add((byte)-102);
        blockUseSet.add((byte)-98);
        blockUseSet.add((byte)-72);
        blockUseSet.add((byte)-71);
        blockUseSet.add((byte)-70);
        blockUseSet.add((byte)-69);
        blockUseSet.add((byte)-68);
        blockUseSet.add((byte)-67);
        blockUseSet.add((byte)-66);
        blockUseSet.add((byte)-65);
        blockUseSet.add((byte)-64);
        blockUseSet.add((byte)-63);
        blockUseSet.add((byte)-62);
        blockUseSet.add((byte)-61);
        blockUseSet.add((byte)-60);
        blockUseSet.add((byte)-59);
    }
}
