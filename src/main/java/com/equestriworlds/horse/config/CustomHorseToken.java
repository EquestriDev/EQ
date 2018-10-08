/*
 * Decompiled with CFR 0_133.
 * 
 * Could not load the following classes:
 *  org.bukkit.Location
 *  org.bukkit.entity.Horse
 *  org.bukkit.entity.Horse$Color
 *  org.bukkit.entity.Horse$Style
 *  org.bukkit.entity.Horse$Variant
 *  org.bukkit.entity.Llama
 *  org.bukkit.entity.Llama$Color
 *  org.bukkit.inventory.ItemStack
 */
package com.equestriworlds.horse.config;

import com.equestriworlds.horse.branding.Brand;
import com.equestriworlds.horse.breeding.breedingStages;
import com.equestriworlds.horse.config.Gender;
import com.equestriworlds.horse.config.HorseAccess;
import com.equestriworlds.vet.enums.Disease;
import com.equestriworlds.vet.enums.Symptom;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import org.bukkit.Location;
import org.bukkit.entity.Horse;
import org.bukkit.entity.Llama;
import org.bukkit.inventory.ItemStack;

public class CustomHorseToken {
    public String name;
    public UUID owner;
    public Gender gender;
    public Brand brand;
    public Long age;
    public Horse.Variant variant;
    public Horse.Color color;
    public Horse.Style style;
    public Llama.Color llamaColor;
    public boolean adult;
    public double speed;
    public double jump;
    public HorseAccess access = HorseAccess.FRIENDS;
    public List<UUID> trusted = new ArrayList<UUID>();
    public List<UUID> friends = new ArrayList<UUID>();
    public HashMap<String, Location> homes = new HashMap();
    public boolean free = false;
    public boolean sale = false;
    public Location lastKnown;
    ItemStack saddle;
    ItemStack armor;
    public breedingStages stage;
    public long breedingTime;
    public String partnerID;
    public List<Symptom> symptoms = new ArrayList<Symptom>();
    public Disease disease;
    public boolean immortal;
    public boolean vaccinated;
    public int appearance;
    public int temperature;
    public int pulse;
    public int eyes;
    public int hydration;
    public int body;
    public long due;
}
