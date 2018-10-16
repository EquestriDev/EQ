package com.equestriworlds.horse.config;

import com.equestriworlds.horse.branding.Brand;
import com.equestriworlds.horse.config.CustomHorse;
import com.equestriworlds.horse.config.CustomHorseToken;
import com.equestriworlds.horse.config.Gender;
import com.equestriworlds.util.C;
import com.equestriworlds.util.F;
import com.equestriworlds.util.UtilGson;
import com.equestriworlds.util.UtilServer;
import com.google.gson.Gson;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.logging.Level;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.MemoryConfigurationOptions;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.configuration.file.YamlConfigurationOptions;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftAbstractHorse;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftEntity;
import org.bukkit.entity.Player;

public class HorseConfig
extends YamlConfiguration {
    public HashMap<String, CustomHorse> horses = new HashMap();
    private Gson gson = UtilGson.getPrettyGson();
    private File file = new File("./plugins/EQ/horse/horses.yml");

    public HorseConfig() {
        if (!this.file.exists()) {
            try {
                this.save(this.file);
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            this.load(this.file);
        }
        catch (FileNotFoundException ex) {
            try {
                this.file.createNewFile();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
        catch (IOException | InvalidConfigurationException ex) {
            Bukkit.getLogger().log(Level.SEVERE, "Cannot load " + this.file, (Throwable)ex);
        }
        this.load();
    }

    public void save() {
        for (CustomHorse horse : this.horses.values()) {
            this.set("horses." + horse.id, (Object)this.gson.toJson((Object)horse.token));
        }
        try {
            this.save(this.file);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void load() {
        ConfigurationSection section = this.getConfigurationSection("horses");
        if (section == null) {
            return;
        }
        Map<String, Object> ids = section.getValues(false);
        for (String str : ids.keySet()) {
            try {
                this.horses.put(str, new CustomHorse(str, (CustomHorseToken)this.gson.fromJson(this.getString("horses." + str), CustomHorseToken.class)));
            }
            catch (Exception e) {
                System.out.println(F.main("Horse", "Failed loading: " + F.elem(str)));
            }
        }
    }

    public CustomHorse getHorseByHorse(CraftEntity entity) {
        for (CustomHorse horse : this.horses.values()) {
            if (horse.horse != entity) continue;
            return horse;
        }
        return null;
    }

    public CustomHorse getHorseById(String id) {
        for (CustomHorse horse : this.horses.values()) {
            if (!horse.id.toLowerCase().equals(id.toLowerCase())) continue;
            return horse;
        }
        return null;
    }

    public List<CustomHorse> getPlayerHorses(Player player) {
        return this.getPlayerHorses(player.getName());
    }

    public List<CustomHorse> getPlayerHorses(String name) {
        ArrayList<CustomHorse> playerHorses = new ArrayList<CustomHorse>();
        OfflinePlayer player = UtilServer.getServer().getOfflinePlayer(name);
        for (CustomHorse horse : this.horses.values()) {
            if (horse.token.owner == null || !horse.token.owner.equals(player.getUniqueId())) continue;
            playerHorses.add(horse);
        }
        return playerHorses;
    }

    public List<CustomHorse> getPlayerTrusted(String name) {
        ArrayList<CustomHorse> playerHorses = new ArrayList<CustomHorse>();
        OfflinePlayer player = UtilServer.getServer().getOfflinePlayer(name);
        for (CustomHorse horse : this.horses.values()) {
            if (horse.token.owner == null || !horse.token.trusted.contains(player.getUniqueId())) continue;
            playerHorses.add(horse);
        }
        return playerHorses;
    }

    public List<CustomHorse> getPlayerHorseGender(String name, Gender gender) {
        ArrayList<CustomHorse> playerHorses = new ArrayList<CustomHorse>();
        OfflinePlayer player = UtilServer.getServer().getOfflinePlayer(name);
        for (CustomHorse horse : this.horses.values()) {
            if (horse.token.owner == null || horse.token.gender != gender || !horse.token.owner.equals(player.getUniqueId())) continue;
            playerHorses.add(horse);
        }
        return playerHorses;
    }

    public int getHorseAmountByBrand(UUID id) {
        int amount = 0;
        for (CustomHorse horses : this.horses.values()) {
            if (horses.token.brand == null || !horses.token.brand.id.equals(id)) continue;
            ++amount;
        }
        return amount;
    }

    public List<CustomHorse> getHorsesByBrand(UUID id) {
        ArrayList<CustomHorse> playerHorses = new ArrayList<CustomHorse>();
        for (CustomHorse horse : this.horses.values()) {
            if (horse.token.brand == null || !horse.token.brand.id.equals(id)) continue;
            playerHorses.add(horse);
        }
        return playerHorses;
    }

    public void delete(String id) {
        CustomHorse horse = this.getHorseById(id);
        horse.remove(false);
        this.horses.remove(horse.id);
        this.set("horses." + horse.id, null);
    }

    public void changeId(String id, String newId) {
        CustomHorse horse = this.horses.get(id);
        this.set("horses." + id, null);
        this.horses.remove(id);
        horse.id = newId;
        this.set("horses." + newId, (Object)this.gson.toJson((Object)horse.token));
        this.horses.put(newId, horse);
    }
}
