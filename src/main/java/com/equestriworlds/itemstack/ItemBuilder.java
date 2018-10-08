/*
 * Decompiled with CFR 0_133.
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.ChatColor
 *  org.bukkit.Color
 *  org.bukkit.Material
 *  org.bukkit.enchantments.Enchantment
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.inventory.meta.ItemMeta
 *  org.bukkit.inventory.meta.LeatherArmorMeta
 *  org.bukkit.inventory.meta.SkullMeta
 *  org.bukkit.potion.Potion
 */
package com.equestriworlds.itemstack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.potion.Potion;

public class ItemBuilder {
    private int _amount;
    private Color _color;
    private short _data;
    private final HashMap<Enchantment, Integer> _enchants = new HashMap();
    private final List<String> _lore = new ArrayList<String>();
    private Material _mat;
    private String _title = null;
    private boolean _unbreakable;
    private String _playerHeadName = null;

    private static ArrayList<String> split(String string, int maxLength) {
        String[] split = string.split(" ");
        string = "";
        ArrayList<String> newString = new ArrayList<String>();
        for (String aSplit : split) {
            if (ChatColor.stripColor((String)(string = string + (string.length() == 0 ? "" : " ") + aSplit)).length() <= maxLength) continue;
            newString.add((newString.size() > 0 ? ChatColor.getLastColors((String)newString.get(newString.size() - 1)) : "") + string);
            string = "";
        }
        if (string.length() > 0) {
            newString.add((newString.size() > 0 ? ChatColor.getLastColors((String)newString.get(newString.size() - 1)) : "") + string);
        }
        return newString;
    }

    public ItemBuilder(ItemStack item) {
        this(item.getType(), item.getDurability());
        this._amount = item.getAmount();
        this._enchants.putAll(item.getEnchantments());
        if (item.getType() == Material.POTION) {
            // empty if block
        }
        if (item.hasItemMeta()) {
            ItemMeta meta = item.getItemMeta();
            if (meta.hasDisplayName()) {
                this._title = meta.getDisplayName();
            }
            if (meta.hasLore()) {
                this._lore.addAll(meta.getLore());
            }
            if (meta instanceof LeatherArmorMeta) {
                this.setColor(((LeatherArmorMeta)meta).getColor());
            }
            this._unbreakable = meta.isUnbreakable();
        }
    }

    private ItemBuilder(Material mat) {
        this(mat, 1);
    }

    private ItemBuilder(Material mat, int amount) {
        this(mat, amount, (short)0);
    }

    public ItemBuilder(Material mat, int amount, short data) {
        this._mat = mat;
        this._amount = amount;
        this._data = data;
    }

    private ItemBuilder(Material mat, short data) {
        this(mat, 1, data);
    }

    private void addEnchantment(Enchantment enchant, int level) {
        if (this._enchants.containsKey((Object)enchant)) {
            this._enchants.remove((Object)enchant);
        }
        this._enchants.put(enchant, level);
    }

    private /* varargs */ void addLore(String ... lores) {
        for (String lore : lores) {
            this._lore.add((Object)ChatColor.GRAY + lore);
        }
    }

    private void addLore(String lore, int maxLength) {
        this._lore.addAll(ItemBuilder.split(lore, maxLength));
    }

    public ItemBuilder addLores(List<String> lores) {
        this._lore.addAll(lores);
        return this;
    }

    private ItemBuilder addLores(List<String> lores, int maxLength) {
        for (String lore : lores) {
            this.addLore(lore, maxLength);
        }
        return this;
    }

    public ItemBuilder addLores(String[] description, int maxLength) {
        return this.addLores(Arrays.asList(description), maxLength);
    }

    public ItemStack build() {
        Material mat = this._mat;
        if (mat == null) {
            mat = Material.AIR;
            Bukkit.getLogger().warning("Null material!");
        } else if (mat == Material.AIR) {
            Bukkit.getLogger().warning("Air material!");
        }
        ItemStack item = new ItemStack(mat, this._amount, this._data);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            if (this._title != null) {
                meta.setDisplayName(this._title);
            }
            if (!this._lore.isEmpty()) {
                meta.setLore(this._lore);
            }
            if (meta instanceof LeatherArmorMeta) {
                ((LeatherArmorMeta)meta).setColor(this._color);
            } else if (meta instanceof SkullMeta && this._playerHeadName != null) {
                ((SkullMeta)meta).setOwner(this._playerHeadName);
            }
            meta.setUnbreakable(this.isUnbreakable());
            item.setItemMeta(meta);
        }
        item.addUnsafeEnchantments(this._enchants);
        return item;
    }

    public ItemBuilder clone() {
        ItemBuilder newBuilder = new ItemBuilder(this._mat);
        newBuilder.setTitle(this._title);
        for (String lore : this._lore) {
            newBuilder.addLore(lore);
        }
        for (Map.Entry entry : this._enchants.entrySet()) {
            newBuilder.addEnchantment((Enchantment)entry.getKey(), (Integer)entry.getValue());
        }
        newBuilder.setColor(this._color);
        return newBuilder;
    }

    public HashMap<Enchantment, Integer> getAllEnchantments() {
        return this._enchants;
    }

    public Color getColor() {
        return this._color;
    }

    public short getData() {
        return this._data;
    }

    public int getEnchantmentLevel(Enchantment enchant) {
        return this._enchants.get((Object)enchant);
    }

    public List<String> getLore() {
        return this._lore;
    }

    private String getTitle() {
        return this._title;
    }

    public Material getType() {
        return this._mat;
    }

    private boolean hasEnchantment(Enchantment enchant) {
        return this._enchants.containsKey((Object)enchant);
    }

    public boolean isItem(ItemStack item) {
        ItemMeta meta = item.getItemMeta();
        if (item.getType() != this.getType()) {
            return false;
        }
        if (!meta.hasDisplayName() && this.getTitle() != null) {
            return false;
        }
        if (!meta.getDisplayName().equals(this.getTitle())) {
            return false;
        }
        if (!meta.hasLore() && !this.getLore().isEmpty()) {
            return false;
        }
        if (meta.hasLore()) {
            for (String lore : meta.getLore()) {
                if (this.getLore().contains(lore)) continue;
                return false;
            }
        }
        for (Enchantment enchant : item.getEnchantments().keySet()) {
            if (this.hasEnchantment(enchant)) continue;
            return false;
        }
        return true;
    }

    private boolean isUnbreakable() {
        return this._unbreakable;
    }

    public ItemBuilder setAmount(int amount) {
        this._amount = amount;
        return this;
    }

    public ItemBuilder setColor(Color color) {
        if (!this._mat.name().contains("LEATHER_")) {
            throw new IllegalArgumentException("Can only dye leather armor!");
        }
        this._color = color;
        return this;
    }

    public void setData(short newData) {
        this._data = newData;
    }

    public ItemBuilder setPotion(Potion potion) {
        if (this._mat != Material.POTION) {
            this._mat = Material.POTION;
        }
        return this;
    }

    public ItemBuilder setRawTitle(String title) {
        this._title = title;
        return this;
    }

    public ItemBuilder setTitle(String title) {
        this._title = (title == null ? null : (title.length() > 2 && ChatColor.getLastColors((String)title.substring(0, 2)).length() == 0 ? ChatColor.WHITE : "")) + title;
        return this;
    }

    public ItemBuilder setTitle(String title, int maxLength) {
        if (title != null && ChatColor.stripColor((String)title).length() > maxLength) {
            ArrayList<String> lores = ItemBuilder.split(title, maxLength);
            for (int i = 1; i < lores.size(); ++i) {
                this._lore.add(lores.get(i));
            }
            title = lores.get(0);
        }
        this.setTitle(title);
        return this;
    }

    public ItemBuilder setType(Material mat) {
        this._mat = mat;
        return this;
    }

    public ItemBuilder setUnbreakable(boolean setUnbreakable) {
        this._unbreakable = setUnbreakable;
        return this;
    }

    public ItemBuilder setPlayerHead(String playerName) {
        this._playerHeadName = playerName;
        return this;
    }
}
