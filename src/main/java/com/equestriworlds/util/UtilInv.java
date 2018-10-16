package com.equestriworlds.util;

import com.equestriworlds.util.F;
import com.equestriworlds.util.UtilPlayer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.material.MaterialData;

public class UtilInv {
    public static void addGlow(ItemStack stack) {
        ItemMeta meta = stack.getItemMeta();
        meta.addEnchant(Enchantment.LURE, 1, false);
        meta.addItemFlags(new ItemFlag[]{ItemFlag.HIDE_ENCHANTS});
        stack.setItemMeta(meta);
    }

    public static boolean insert(Player player, ItemStack stack) {
        player.getInventory().addItem(new ItemStack[]{stack});
        player.updateInventory();
        return true;
    }

    public static boolean contains(Player player, Material item, byte data, int required) {
        return UtilInv.contains(player, null, item, data, required);
    }

    public static boolean contains(Player player, String itemNameContains, Material item, byte data, int required) {
        return UtilInv.contains(player, itemNameContains, item, data, required, true, true);
    }

    public static boolean contains(Player player, String itemNameContains, Material item, byte data, int required, boolean checkArmor, boolean checkCursor) {
        for (ItemStack stack : UtilInv.getItems(player, checkArmor, checkCursor)) {
            if (required <= 0) {
                return true;
            }
            if (stack == null || stack.getType() != item || stack.getAmount() <= 0 || data >= 0 && stack.getData() != null && stack.getData().getData() != data || itemNameContains != null && (stack.getItemMeta().getDisplayName() == null || !stack.getItemMeta().getDisplayName().contains(itemNameContains))) continue;
            required -= stack.getAmount();
        }
        if (required <= 0) {
            return true;
        }
        return false;
    }

    public static boolean remove(Player player, Material item, byte data, int toRemove) {
        if (!UtilInv.contains(player, item, data, toRemove)) {
            return false;
        }
        Iterator iterator = player.getInventory().all(item).keySet().iterator();
        while (iterator.hasNext()) {
            ItemStack stack;
            int i = (Integer)iterator.next();
            if (toRemove <= 0 || (stack = player.getInventory().getItem(i)).getData() != null && stack.getData().getData() != data) continue;
            int foundAmount = stack.getAmount();
            if (toRemove >= foundAmount) {
                toRemove -= foundAmount;
                player.getInventory().setItem(i, null);
                continue;
            }
            stack.setAmount(foundAmount - toRemove);
            player.getInventory().setItem(i, stack);
            toRemove = 0;
        }
        player.updateInventory();
        return true;
    }

    public static void Clear(Player player) {
        PlayerInventory inv = player.getInventory();
        inv.clear();
        inv.setArmorContents(new ItemStack[4]);
        player.setItemOnCursor(new ItemStack(Material.AIR));
        player.saveData();
    }

    public static ArrayList<ItemStack> getItems(Player player) {
        return UtilInv.getItems(player, true, true);
    }

    public static ArrayList<ItemStack> getItems(Player player, boolean getArmor, boolean getCursor) {
        ItemStack cursorItem;
        ArrayList<ItemStack> items = new ArrayList<ItemStack>();
        PlayerInventory inv = player.getInventory();
        for (ItemStack item : inv.getContents()) {
            if (item == null || item.getType() == Material.AIR) continue;
            items.add(item.clone());
        }
        if (getArmor) {
            for (ItemStack item : inv.getArmorContents()) {
                if (item == null || item.getType() == Material.AIR) continue;
                items.add(item.clone());
            }
        }
        if (getCursor && (cursorItem = player.getItemOnCursor()) != null && cursorItem.getType() != Material.AIR) {
            items.add(cursorItem.clone());
        }
        return items;
    }

    public static void drop(Player player, boolean clear) {
        for (ItemStack cur : UtilInv.getItems(player)) {
            player.getWorld().dropItemNaturally(player.getLocation(), cur);
        }
        if (clear) {
            UtilInv.Clear(player);
        }
    }

    public static void Update(Entity player) {
        if (!(player instanceof Player)) {
            return;
        }
        ((Player)player).updateInventory();
    }

    public static int removeAll(Player player, Material type, byte data) {
        HashSet<ItemStack> remove = new HashSet<ItemStack>();
        int count = 0;
        for (ItemStack item : player.getInventory().getContents()) {
            if (item == null || item.getType() != type || data != -1 && item.getData() != null && (item.getData() == null || item.getData().getData() != data)) continue;
            count += item.getAmount();
            remove.add(item);
        }
        for (ItemStack item : remove) {
            player.getInventory().remove(item);
        }
        return count;
    }

    public static byte GetData(ItemStack stack) {
        if (stack == null) {
            return 0;
        }
        if (stack.getData() == null) {
            return 0;
        }
        return stack.getData().getData();
    }

    public static boolean IsItem(ItemStack item, Material type, byte data) {
        return UtilInv.IsItem(item, null, type.getId(), data);
    }

    public static boolean IsItem(ItemStack item, String name, Material type, byte data) {
        return UtilInv.IsItem(item, name, type.getId(), data);
    }

    public static boolean IsItem(ItemStack item, String name, int id, byte data) {
        if (item == null) {
            return false;
        }
        if (item.getTypeId() != id) {
            return false;
        }
        if (data != -1 && UtilInv.GetData(item) != data) {
            return false;
        }
        if (!(name == null || item.getItemMeta().getDisplayName() != null && item.getItemMeta().getDisplayName().contains(name))) {
            return false;
        }
        return true;
    }

    public static void DisallowMovementOf(InventoryClickEvent event, String name, Material type, byte data, boolean inform) {
        UtilInv.DisallowMovementOf(event, name, type, data, inform, false);
    }

    public static void DisallowMovementOf(InventoryClickEvent event, String name, Material type, byte data, boolean inform, boolean allInventorties) {
        if (!allInventorties && event.getInventory().getType() == InventoryType.CRAFTING) {
            return;
        }
        if (event.getAction() == InventoryAction.HOTBAR_SWAP || event.getAction() == InventoryAction.HOTBAR_MOVE_AND_READD) {
            boolean match = false;
            if (UtilInv.IsItem(event.getCurrentItem(), name, type, data)) {
                match = true;
            }
            if (UtilInv.IsItem(event.getWhoClicked().getInventory().getItem(event.getHotbarButton()), name, type, data)) {
                match = true;
            }
            if (!match) {
                return;
            }
            UtilPlayer.message((Entity)event.getWhoClicked(), F.main("Inventory", "You cannot hotbar swap " + F.item(name) + "."));
            event.setCancelled(true);
        } else {
            if (event.getCurrentItem() == null) {
                return;
            }
            UtilInv.IsItem(event.getCurrentItem(), name, type, data);
            if (!UtilInv.IsItem(event.getCurrentItem(), name, type, data)) {
                return;
            }
            UtilPlayer.message((Entity)event.getWhoClicked(), F.main("Inventory", "You cannot move " + F.item(name) + "."));
            event.setCancelled(true);
        }
    }

    public static void UseItemInHand(Player player) {
        if (player.getItemInHand().getAmount() > 1) {
            player.getItemInHand().setAmount(player.getItemInHand().getAmount() - 1);
        } else {
            player.setItemInHand(null);
        }
        UtilInv.Update((Entity)player);
    }
}
