package com.equestriworlds.menu.item;

import com.equestriworlds.util.C;
import com.equestriworlds.util.UtilInv;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class ShopItem
extends ItemStack {
    protected String _name;
    private String _itemName;
    protected String[] _lore;
    private int _amount;
    private boolean _locked;
    private boolean _displayItem;

    public ShopItem(ItemStack itemStack, String name, String deliveryName, int deliveryAmount, boolean locked, boolean displayItem) {
        super(itemStack);
        this._name = name;
        this._itemName = deliveryName;
        this._displayItem = displayItem;
        this._amount = deliveryAmount;
        this._lore = itemStack.getItemMeta().hasLore() ? itemStack.getItemMeta().getLore().toArray(new String[0]) : new String[0];
        this.UpdateVisual(true);
    }

    public net.minecraft.server.v1_12_R1.ItemStack getHandle() {
        return CraftItemStack.asNMSCopy((ItemStack)this);
    }

    public ShopItem(Material type, String name, int deliveryAmount, boolean locked) {
        this(type, name, null, deliveryAmount, locked);
    }

    public ShopItem(int type, String name, int deliveryAmount, boolean locked) {
        this(type, name, null, deliveryAmount, locked);
    }

    public ShopItem(Material type, String name, String[] lore, int deliveryAmount, boolean locked) {
        this(type, name, lore, deliveryAmount, locked, false);
    }

    public ShopItem(int type, String name, String[] lore, int deliveryAmount, boolean locked) {
        this(type, name, lore, deliveryAmount, locked, false);
    }

    public ShopItem(Material type, String name, String[] lore, int deliveryAmount, boolean locked, boolean displayItem) {
        this(type, (byte)0, name, null, lore, deliveryAmount, locked, displayItem);
    }

    public ShopItem(int type, String name, String[] lore, int deliveryAmount, boolean locked, boolean displayItem) {
        this(type, (byte)0, name, null, lore, deliveryAmount, locked, displayItem);
    }

    public ShopItem(Material type, byte data, String name, String[] lore, int deliveryAmount, boolean locked, boolean displayItem) {
        this(type, data, name, null, lore, deliveryAmount, locked, displayItem);
    }

    public ShopItem(int type, byte data, String name, String[] lore, int deliveryAmount, boolean locked, boolean displayItem) {
        this(type, data, name, null, lore, deliveryAmount, locked, displayItem);
    }

    public ShopItem(Material type, byte data, String name, String deliveryName, String[] lore, int deliveryAmount, boolean locked, boolean displayItem) {
        this(type.getId(), data, name, deliveryName, lore, deliveryAmount, locked, displayItem);
    }

    public ShopItem(int type, byte data, String name, String deliveryName, String[] lore, int deliveryAmount, boolean locked, boolean displayItem) {
        super(type, Math.max(deliveryAmount, 1), (short)data, null);
        this._name = name;
        this._itemName = deliveryName;
        this._lore = lore;
        this._displayItem = displayItem;
        this._amount = deliveryAmount;
        this._locked = locked;
        this.UpdateVisual(false);
        this.setAmount(Math.max(deliveryAmount, 1));
    }

    public boolean IsLocked() {
        return this._locked;
    }

    public void SetDeliverySettings() {
        this.setAmount(this._amount);
        if (this._itemName != null) {
            ItemMeta meta = this.getItemMeta();
            meta.setDisplayName(this._itemName);
            this.setItemMeta(meta);
        }
    }

    public ShopItem clone() {
        return new ShopItem(super.clone(), this._name, this._itemName, this._amount, this._locked, this._displayItem);
    }

    public boolean equals(Object obj) {
        return super.equals(obj);
    }

    protected void UpdateVisual(boolean clone) {
        ItemMeta meta = this.getItemMeta();
        if (!clone) {
            meta.setDisplayName((this._locked && !this._displayItem ? C.cRed : C.cGreen) + C.Bold + this._name);
        }
        ArrayList<String> lore = new ArrayList<String>();
        if (this._lore != null) {
            for (String line : this._lore) {
                if (line == null || line.isEmpty()) continue;
                lore.add(line);
            }
        }
        meta.setLore(lore);
        this.setItemMeta(meta);
    }

    public boolean IsDisplay() {
        return this._displayItem;
    }

    public void addGlow() {
        UtilInv.addGlow(this);
    }

    public void SetLocked(boolean owns) {
        this._locked = owns;
        this.UpdateVisual(false);
    }

    public String GetName() {
        return this._name;
    }

    public void SetName(String name) {
        this._name = name;
    }

    public void SetLore(String[] string) {
        this._lore = string;
        ArrayList<String> lore = new ArrayList<String>();
        if (this._lore != null) {
            for (String line : this._lore) {
                if (line == null || line.isEmpty()) continue;
                lore.add(line);
            }
        }
        ItemMeta meta = this.getItemMeta();
        meta.setLore(lore);
        this.setItemMeta(meta);
    }
}
