package com.equestriworlds.misc.commands;

import com.equestriworlds.MiniPlugin;
import com.equestriworlds.command.MultiCommandBase;
import com.equestriworlds.common.Rank;
import com.equestriworlds.horse.HorseManager;
import com.equestriworlds.util.F;
import com.equestriworlds.util.UtilPlayer;
import com.equestriworlds.util.UtilTime;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Server;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

public class PlayerCountdownCommand
extends MultiCommandBase<HorseManager> {
    private Long cooldown = 0L;

    public PlayerCountdownCommand(HorseManager plugin) {
        super(plugin, Rank.VIPP, "countdown");
    }

    @Override
    protected void Help(Player caller, String[] args) {
        if (args != null) {
            UtilPlayer.message((Entity)caller, F.main("Misc Manager", "Invalid arguments! Usage: /countdown"));
            return;
        }
        if (this.cooldown > System.currentTimeMillis()) {
            UtilPlayer.message((Entity)caller, F.main("Misc Manager", "Cooldown remaining: " + F.name(UtilTime.formatDateDiff(this.cooldown, true)) + "!"));
            return;
        }
        this.cooldown = System.currentTimeMillis() + 15000L;
        new BukkitRunnable(){
            Server server = Bukkit.getServer();
            String prefix = "&8&l[&b&lCountdown&8&l]";
            int i = 3;

            public void run() {
                this.server.broadcastMessage(ChatColor.translateAlternateColorCodes((char)'&', (String)(this.prefix + " &3&l" + PlayerCountdownCommand.this.getWord(this.i))));
                --this.i;
                if (this.i < 0) {
                    this.cancel();
                }
            }
        }.runTaskTimerAsynchronously((Plugin)((HorseManager)this.Plugin).getPlugin(), 0L, 20L);
    }

    private String getWord(int number) {
        switch (number) {
            case 0: {
                return "&6&l* &b&lGO! &6&l*";
            }
            case 1: {
                return "One...";
            }
            case 2: {
                return "Two...";
            }
            case 3: {
                return "Three...";
            }
        }
        return "";
    }

}
