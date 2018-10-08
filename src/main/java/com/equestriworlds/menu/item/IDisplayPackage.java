/*
 * Decompiled with CFR 0_133.
 * 
 * Could not load the following classes:
 *  org.bukkit.Material
 */
package com.equestriworlds.menu.item;

import org.bukkit.Material;

public interface IDisplayPackage {
    public String GetName();

    public String[] GetDescription();

    public Material GetDisplayMaterial();

    public byte GetDisplayData();
}
