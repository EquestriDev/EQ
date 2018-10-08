/*
 * Decompiled with CFR 0_133.
 * 
 * Could not load the following classes:
 *  org.bukkit.Material
 */
package com.equestriworlds.common;

import org.bukkit.Material;

public enum CurrencyType {
    CRYSTAL("Crystal", Material.PRISMARINE_CRYSTALS);
    
    private String _name;
    private Material _material;

    private CurrencyType(String name, Material material) {
        this._name = name;
        this._material = material;
    }

    public String getName() {
        return this._name;
    }

    public Material getMat() {
        return this._material;
    }
}
