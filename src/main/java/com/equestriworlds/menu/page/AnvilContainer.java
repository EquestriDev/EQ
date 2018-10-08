/*
 * Decompiled with CFR 0_133.
 * 
 * Could not load the following classes:
 *  net.minecraft.server.v1_12_R1.Container
 *  net.minecraft.server.v1_12_R1.EntityHuman
 *  net.minecraft.server.v1_12_R1.IInventory
 *  net.minecraft.server.v1_12_R1.InventoryLargeChest
 *  net.minecraft.server.v1_12_R1.PlayerInventory
 *  net.minecraft.server.v1_12_R1.Slot
 *  org.bukkit.craftbukkit.v1_12_R1.entity.CraftHumanEntity
 *  org.bukkit.craftbukkit.v1_12_R1.inventory.CraftInventory
 *  org.bukkit.craftbukkit.v1_12_R1.inventory.CraftInventoryDoubleChest
 *  org.bukkit.craftbukkit.v1_12_R1.inventory.CraftInventoryPlayer
 *  org.bukkit.craftbukkit.v1_12_R1.inventory.CraftInventoryView
 *  org.bukkit.entity.HumanEntity
 *  org.bukkit.inventory.Inventory
 *  org.bukkit.inventory.InventoryView
 */
package com.equestriworlds.menu.page;

import net.minecraft.server.v1_12_R1.Container;
import net.minecraft.server.v1_12_R1.EntityHuman;
import net.minecraft.server.v1_12_R1.IInventory;
import net.minecraft.server.v1_12_R1.InventoryLargeChest;
import net.minecraft.server.v1_12_R1.PlayerInventory;
import net.minecraft.server.v1_12_R1.Slot;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftHumanEntity;
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftInventory;
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftInventoryDoubleChest;
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftInventoryPlayer;
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftInventoryView;
import org.bukkit.entity.HumanEntity;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;

public class AnvilContainer
extends Container {
    public IInventory _container;
    private CraftInventoryView _bukkitEntity = null;
    private PlayerInventory _playerInventory;

    public AnvilContainer(PlayerInventory playerInventory, IInventory anvilInventory) {
        int l;
        this._playerInventory = playerInventory;
        this._container = anvilInventory;
        this.a(new Slot(anvilInventory, 0, 27, 47));
        this.a(new Slot(anvilInventory, 1, 76, 47));
        this.a(new Slot(anvilInventory, 2, 134, 47));
        for (l = 0; l < 3; ++l) {
            for (int i1 = 0; i1 < 9; ++i1) {
                this.a(new Slot((IInventory)playerInventory, i1 + l * 9 + 9, 8 + i1 * 18, 84 + l * 18));
            }
        }
        for (l = 0; l < 9; ++l) {
            this.a(new Slot((IInventory)playerInventory, l, 8 + l * 18, 142));
        }
    }

    public CraftInventoryView getBukkitView() {
        if (this._bukkitEntity != null) {
            return this._bukkitEntity;
        }
        Object inventory = this._container instanceof PlayerInventory ? new CraftInventoryPlayer((PlayerInventory)this._container) : (this._container instanceof InventoryLargeChest ? new CraftInventoryDoubleChest((InventoryLargeChest)this._container) : new CraftInventory(this._container));
        this._bukkitEntity = new CraftInventoryView((HumanEntity)this._playerInventory.player.getBukkitEntity(), (Inventory)inventory, (Container)this);
        return this._bukkitEntity;
    }

    public boolean a(EntityHuman arg0) {
        return true;
    }
}
