package com.equestriworlds.misc;

import com.equestriworlds.horse.HorseManager;
import com.equestriworlds.horse.config.CustomHorse;
import com.equestriworlds.horse.config.HorseConfig;
import java.util.List;
import me.clip.placeholderapi.external.EZPlaceholderHook;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public class EquestriWorldsHooks
extends EZPlaceholderHook {
    private HorseManager horseManager;

    public EquestriWorldsHooks(HorseManager horseManager) {
        super((Plugin)horseManager.getPlugin(), "equestriworlds");
        this.horseManager = horseManager;
    }

    public String onPlaceholderRequest(Player p, String identifier) {
        if (p == null) {
            return "";
        }
        if (identifier.equals("horseamount")) {
            return String.valueOf(this.horseManager.config.getPlayerHorses(p).size());
        }
        return null;
    }
}
