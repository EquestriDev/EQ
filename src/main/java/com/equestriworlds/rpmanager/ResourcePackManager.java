package com.equestriworlds.rpmanager;

import com.equestriworlds.MiniPlugin;
import com.equestriworlds.command.ICommand;
import com.equestriworlds.rpmanager.ResourcePack;
import com.equestriworlds.rpmanager.commands.ResourcePackCommand;
import com.equestriworlds.util.C;
import com.equestriworlds.util.F;
import com.equestriworlds.util.UtilPlayer;
import org.bukkit.Location;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerResourcePackStatusEvent;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Notify player about resource pack download.
 */
public class ResourcePackManager
extends MiniPlugin {
    public ResourcePackManager(JavaPlugin plugin) {
        super("Resource Pack Manager", plugin);
        this.addCommand(new ResourcePackCommand(this));
    }

    public void load(Player player, ResourcePack pack) {
        player.setResourcePack(pack.link);
    }

    @EventHandler
    public void resourcePackLoadStatus(PlayerResourcePackStatusEvent e) {
        switch (e.getStatus()) {
            case ACCEPTED: {
                UtilPlayer.message((Entity)e.getPlayer(), F.main(this.getName(), C.cGreen + "Resource Pack accepted!"));
                break;
            }
            case DECLINED: {
                UtilPlayer.message((Entity)e.getPlayer(), F.main(this.getName(), C.cRed + "Resource Pack declined!"));
                break;
            }
            case FAILED_DOWNLOAD: {
                UtilPlayer.message((Entity)e.getPlayer(), F.main(this.getName(), C.cRed + "Resource Pack failed to download!"));
                break;
            }
            case SUCCESSFULLY_LOADED: {
                UtilPlayer.message((Entity)e.getPlayer(), F.main(this.getName(), C.cGreen + "Resource Pack successfully loaded!"));
            }
        }
    }
}
