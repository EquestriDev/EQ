/*
 * Decompiled with CFR 0_133.
 * 
 * Could not load the following classes:
 *  org.apache.commons.io.FileUtils
 *  org.bukkit.configuration.file.YamlConfiguration
 */
package com.equestriworlds.vet;

import com.equestriworlds.Main;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import org.apache.commons.io.FileUtils;
import org.bukkit.configuration.file.YamlConfiguration;

public class config {
    public static YamlConfiguration config;

    public config() {
        File langFile = new File(((Main)Main.getPlugin(Main.class)).getDataFolder(), "config.yml");
        if (!langFile.exists()) {
            try {
                langFile.createNewFile();
                FileUtils.copyInputStreamToFile((InputStream)((Main)Main.getPlugin(Main.class)).getResource("config.yml"), (File)langFile);
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
        config = YamlConfiguration.loadConfiguration((File)langFile);
    }
}
