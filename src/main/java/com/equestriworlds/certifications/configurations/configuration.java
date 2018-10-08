/*
 * Decompiled with CFR 0_133.
 * 
 * Could not load the following classes:
 *  org.apache.commons.io.FileUtils
 *  org.bukkit.configuration.file.YamlConfiguration
 */
package com.equestriworlds.certifications.configurations;

import com.equestriworlds.Main;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import org.apache.commons.io.FileUtils;
import org.bukkit.configuration.file.YamlConfiguration;

public class configuration {
    public static YamlConfiguration config;

    public configuration() {
        this.loadConfig();
    }

    private void loadConfig() {
        File langFile;
        File directory = new File(((Main)Main.getPlugin(Main.class)).getDataFolder() + File.separator + "certifications");
        if (!directory.exists()) {
            directory.mkdir();
        }
        if (!(langFile = new File(directory, "certifications.yml")).exists()) {
            try {
                langFile.createNewFile();
                FileUtils.copyInputStreamToFile((InputStream)((Main)Main.getPlugin(Main.class)).getResource("certifications.yml"), (File)langFile);
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
        config = YamlConfiguration.loadConfiguration((File)langFile);
    }
}
