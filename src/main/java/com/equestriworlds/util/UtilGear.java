package com.equestriworlds.util;

import java.util.HashSet;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class UtilGear {
    private static HashSet<Material> _axeSet = new HashSet();
    private static HashSet<Material> _swordSet = new HashSet();
    private static HashSet<Material> _maulSet = new HashSet();
    private static HashSet<Material> pickSet = new HashSet();
    private static HashSet<Material> diamondSet = new HashSet();
    private static HashSet<Material> goldSet = new HashSet();
    public static HashSet<Material> scytheSet = new HashSet();

    public static boolean isAxe(ItemStack item) {
        if (item == null) {
            return false;
        }
        if (_axeSet.isEmpty()) {
            _axeSet.add(Material.WOOD_AXE);
            _axeSet.add(Material.STONE_AXE);
            _axeSet.add(Material.IRON_AXE);
            _axeSet.add(Material.GOLD_AXE);
            _axeSet.add(Material.DIAMOND_AXE);
        }
        return _axeSet.contains((Object)item.getType());
    }

    public static boolean isSword(ItemStack item) {
        if (item == null) {
            return false;
        }
        if (_swordSet.isEmpty()) {
            _swordSet.add(Material.WOOD_SWORD);
            _swordSet.add(Material.STONE_SWORD);
            _swordSet.add(Material.IRON_SWORD);
            _swordSet.add(Material.GOLD_SWORD);
            _swordSet.add(Material.DIAMOND_SWORD);
        }
        return _swordSet.contains((Object)item.getType());
    }

    public static boolean isShovel(ItemStack item) {
        if (item == null) {
            return false;
        }
        if (_maulSet.isEmpty()) {
            _maulSet.add(Material.WOOD_SPADE);
            _maulSet.add(Material.STONE_SPADE);
            _maulSet.add(Material.IRON_SPADE);
            _maulSet.add(Material.GOLD_SPADE);
            _maulSet.add(Material.DIAMOND_SPADE);
        }
        return _maulSet.contains((Object)item.getType());
    }

    public static boolean isHoe(ItemStack item) {
        if (item == null) {
            return false;
        }
        if (scytheSet.isEmpty()) {
            scytheSet.add(Material.WOOD_HOE);
            scytheSet.add(Material.STONE_HOE);
            scytheSet.add(Material.IRON_HOE);
            scytheSet.add(Material.GOLD_HOE);
            scytheSet.add(Material.DIAMOND_HOE);
        }
        return scytheSet.contains((Object)item.getType());
    }

    public static boolean isPickaxe(ItemStack item) {
        if (item == null) {
            return false;
        }
        if (pickSet.isEmpty()) {
            pickSet.add(Material.WOOD_PICKAXE);
            pickSet.add(Material.STONE_PICKAXE);
            pickSet.add(Material.IRON_PICKAXE);
            pickSet.add(Material.GOLD_PICKAXE);
            pickSet.add(Material.DIAMOND_PICKAXE);
        }
        return pickSet.contains((Object)item.getType());
    }

    public static boolean isDiamond(ItemStack item) {
        if (item == null) {
            return false;
        }
        if (diamondSet.isEmpty()) {
            diamondSet.add(Material.DIAMOND_SWORD);
            diamondSet.add(Material.DIAMOND_AXE);
            diamondSet.add(Material.DIAMOND_SPADE);
            diamondSet.add(Material.DIAMOND_HOE);
        }
        return diamondSet.contains((Object)item.getType());
    }

    public static boolean isGold(ItemStack item) {
        if (item == null) {
            return false;
        }
        if (goldSet.isEmpty()) {
            goldSet.add(Material.GOLD_SWORD);
            goldSet.add(Material.GOLD_AXE);
        }
        return goldSet.contains((Object)item.getType());
    }

    public static boolean isBow(ItemStack item) {
        if (item == null) {
            return false;
        }
        return item.getType() == Material.BOW;
    }

    public static boolean isWeapon(ItemStack item) {
        return UtilGear.isAxe(item) || UtilGear.isSword(item);
    }

    public static boolean isMat(ItemStack item, Material mat) {
        if (item == null) {
            return false;
        }
        return item.getType() == mat;
    }

    public static boolean isRepairable(ItemStack item) {
        return item.getType().getMaxDurability() > 0;
    }
}
