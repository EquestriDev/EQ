package com.equestriworlds.horse.branding;

import com.equestriworlds.horse.branding.Brand;
import com.equestriworlds.horse.branding.BrandToken;
import com.equestriworlds.util.C;
import com.equestriworlds.util.F;
import com.equestriworlds.util.UtilGson;
import com.google.gson.Gson;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.logging.Level;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.MemoryConfigurationOptions;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.configuration.file.YamlConfigurationOptions;

public class BrandConfig
extends YamlConfiguration {
    public HashMap<UUID, Brand> brands = new HashMap();
    private Gson gson = UtilGson.getPrettyGson();
    private File file = new File("./plugins/EQ/brands/brands.yml");
    private final Pattern STRIP_COLOR_PATTERN = Pattern.compile("(?i)" + String.valueOf('&') + "[0-9A-FK-OR]");

    public BrandConfig() {
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
            Bukkit.getLogger().log(Level.SEVERE, "Cannot load " + this.file, ex);
        }
        this.load();
    }

    public void save() {
        for (Brand brand : this.brands.values()) {
            this.set("brands." + String.valueOf(brand.id), (Object)this.gson.toJson((Object)brand.token));
        }
        try {
            this.save(this.file);
        }
        catch (IOException e) {
            System.out.print("ERROR IN SAVING");
        }
    }

    private void load() {
        ConfigurationSection section = this.getConfigurationSection("brands");
        if (section == null) {
            return;
        }
        Map<String, Object> ids = section.getValues(false);
        for (String str : ids.keySet()) {
            try {
                this.brands.put(UUID.fromString(str), new Brand(UUID.fromString(str), (BrandToken)this.gson.fromJson(this.getString("brands." + str), BrandToken.class)));
            }
            catch (Exception e) {
                System.out.println(F.main("Brand", "Failed loading: " + F.elem(str)));
            }
        }
    }

    public Brand getBrandByID(UUID id) {
        for (Brand brand : this.brands.values()) {
            if (!brand.id.equals(id) && !brand.token.coowners.contains(id)) continue;
            return brand;
        }
        return null;
    }

    public Brand getBrandByFormat(String id) {
        for (Brand brand : this.brands.values()) {
            if (!this.stripColor(brand.token.format).equals(id)) continue;
            return brand;
        }
        return null;
    }

    public void delete(UUID id) {
        this.brands.remove(id);
        this.set("brands." + id, null);
    }

    private String stripColor(String input) {
        return input == null ? null : this.STRIP_COLOR_PATTERN.matcher(input).replaceAll("");
    }
}
