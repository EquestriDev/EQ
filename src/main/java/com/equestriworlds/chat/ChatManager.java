package com.equestriworlds.chat;

import com.equestriworlds.MiniPlugin;
import com.equestriworlds.chat.command.AdminChatCommand;
import com.equestriworlds.chat.command.AdminMessageCommand;
import com.equestriworlds.chat.command.AdminReplyCommand;
import com.equestriworlds.chat.command.StaffBroadcastCommand;
import com.equestriworlds.command.ICommand;
import com.equestriworlds.common.Rank;
import com.equestriworlds.util.C;
import com.equestriworlds.util.F;
import com.equestriworlds.util.FileUtil;
import com.equestriworlds.util.UtilPlayer;
import com.equestriworlds.util.UtilServer;
import java.util.ArrayList;
import java.util.HashMap;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Chat and command interceptor
 * Staff chat
 * /gamemode, /gm, /gms, /gmc (cancel with purchase message)
 * Messaging utility
 */
public class ChatManager
extends MiniPlugin {
    public HashMap<Player, Player> prevMes = new HashMap();
    public HashMap<Player, Player> prevStaff = new HashMap();
    public ArrayList<Player> staffChat = new ArrayList();

    public ChatManager(JavaPlugin plugin) {
        super("Chat Manager", plugin);
        this.addCommand(new AdminChatCommand(this));
        this.addCommand(new AdminMessageCommand(this));
        this.addCommand(new AdminReplyCommand(this));
        this.addCommand(new StaffBroadcastCommand(this));
    }

    @EventHandler
    public void chat(AsyncPlayerChatEvent e) {
        if (this.staffChat.contains((Object)e.getPlayer())) {
            e.setCancelled(true);
            Player player = e.getPlayer();
            String message = e.getMessage();
            boolean staffOnline = false;
            FileUtil.logToFile(message, player.getName());
            for (Player staff : UtilServer.getPlayers()) {
                if (!Rank.Has(staff, Rank.TJRMOD, false)) continue;
                UtilPlayer.message((Entity)staff, C.convert("&8[&dSC&8] &5" + Rank.getRank((Player)player).Name + " &d" + player.getName() + " &7\u00bb &5" + C.clear(message)));
                staffOnline = true;
                staff.playSound(staff.getLocation(), Sound.BLOCK_NOTE_PLING, 0.5f, 2.0f);
            }
            if (staffOnline) {
                if (!Rank.Has(player, Rank.TJRMOD, false)) {
                    UtilPlayer.message((Entity)player, C.convert("&8[&dSC&8] &5" + Rank.getRank((Player)player).Name + " &d" + player.getName() + " &7\u00bb &5" + C.clear(message)));
                }
            } else {
                UtilPlayer.message((Entity)player, F.main("Staff Chat", "There are no staff members online"));
            }
        }
    }

    @EventHandler
    public void onCommand(PlayerCommandPreprocessEvent event) {
        String[] string = event.getMessage().split(" ");
        if (string[0].equalsIgnoreCase("/gamemode") || string[0].equals("/gm") || string[0].equals("/gms") || string[0].equals("/gmc")) {
            Player player = event.getPlayer();
            if (Rank.Has(player, Rank.TJRMOD, false)) {
                return;
            }
            String colouredMessage = ChatColor.translateAlternateColorCodes((char)'&', (String)("&8[&bID Gamemode&8] &b" + player.getName() + ": &7" + event.getMessage()));
            for (Player staff : UtilServer.getPlayers()) {
                if (!Rank.Has(staff, Rank.TJRMOD, false)) continue;
                staff.sendMessage(colouredMessage);
            }
            if (!player.hasPermission("essentials.gamemode")) {
                player.sendMessage(ChatColor.translateAlternateColorCodes((char)'&', (String)"&8&l[&b&lEquiWorlds&8&l] &cPlease purchase VIP++ for this command!"));
                event.setCancelled(true);
            }
        }
    }

    public void message(Player sender, Player target, String message, boolean staff) {
        if (staff) {
            UtilPlayer.message((Entity)sender, C.cPurple + "-> " + F.rank(Rank.getRank(target)) + " " + target.getName() + " " + C.cPurple + message);
            for (Player pstaff : UtilServer.getPlayers()) {
                if (target.equals((Object)pstaff) || sender.equals((Object)pstaff) || !Rank.Has(pstaff, Rank.TJRMOD, false)) continue;
                UtilPlayer.message((Entity)pstaff, F.rank(Rank.getRank(sender)) + " " + sender.getName() + C.cPurple + " -> " + F.rank(Rank.getRank(target)) + " " + target.getName() + " " + C.cPurple + message);
            }
            this.prevStaff.put(sender, target);
            UtilPlayer.message((Entity)target, C.cPurple + "<- " + F.rank(Rank.getRank(sender)) + " " + sender.getName() + " " + C.cPurple + message);
            sender.playSound(target.getLocation(), Sound.BLOCK_NOTE_CHIME, 1.0f, 1.0f);
            target.playSound(target.getLocation(), Sound.BLOCK_NOTE_CHIME, 2.0f, 2.0f);
        }
    }

    public void broadcast(Player sender, String message) {
        for (Player player : UtilServer.getPlayers()) {
            player.playSound(player.getLocation(), Sound.BLOCK_NOTE_PLING, 10.0f, 0.6f);
            UtilPlayer.message((Entity)player, C.cAqua + C.Bold + "[" + sender.getName() + "] " + C.cDAqua + C.Bold + message);
        }
    }
}
