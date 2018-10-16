package com.equestriworlds.command;

import com.equestriworlds.command.ICommand;
import com.equestriworlds.common.Rank;
import com.equestriworlds.servermanager.ServerStopEvent;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Pattern;
import org.bukkit.Server;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.server.TabCompleteEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Command and complete interceptor
 */
public class CommandCenter
implements Listener {
    public static CommandCenter Instance;
    protected JavaPlugin Plugin;
    private HashMap<String, ICommand> Commands;

    public static void Initialize(JavaPlugin plugin) {
        if (Instance == null) {
            Instance = new CommandCenter(plugin);
        }
    }

    private CommandCenter(JavaPlugin instance) {
        this.Plugin = instance;
        this.Commands = new HashMap();
        this.Plugin.getServer().getPluginManager().registerEvents((Listener)this, (Plugin)this.Plugin);
    }

    @EventHandler
    public void disable(ServerStopEvent e) {
        HandlerList.unregisterAll((Listener)this);
    }

    @EventHandler
    public void OnPlayerCommandPreprocess(PlayerCommandPreprocessEvent event) {
        ICommand command;
        String commandName = event.getMessage().substring(1);
        String[] args = null;
        if (commandName.contains(" ")) {
            commandName = commandName.split(" ")[0];
            args = event.getMessage().substring(event.getMessage().indexOf(32) + 1).split(" ");
        }
        if ((command = this.Commands.get(commandName.toLowerCase())) != null) {
            event.setCancelled(true);
            if (Rank.Has(event.getPlayer(), command.GetRequiredRank(), command.GetSpecificRanks(), true)) {
                command.SetAliasUsed(commandName.toLowerCase());
                command.Execute(event.getPlayer(), args);
            }
        }
    }

    @EventHandler
    public void onTabComplete(TabCompleteEvent event) {
        List<String> suggestions;
        if (!(event.getSender() instanceof Player)) {
            return;
        }
        Player player = (Player)event.getSender();
        if (!Rank.Has(player, Rank.TJRMOD, false)) {
            return;
        }
        if (!event.getBuffer().startsWith("/")) {
            return;
        }
        String cmdLine = event.getBuffer().substring(1);
        int spaceIndex = cmdLine.indexOf(32);
        if (spaceIndex == -1) {
            ArrayList<String> suggestions2 = new ArrayList<String>();
            for (String sug : event.getCompletions()) {
                if (sug.contains(":")) continue;
                suggestions2.add(sug);
            }
            for (ICommand command : this.Commands.values()) {
                if (suggestions2.contains("/" + command.Aliases().toArray()[0]) || !((String)command.Aliases().toArray()[0]).startsWith(cmdLine) || !Rank.Has((Player)event.getSender(), command.GetRequiredRank(), command.GetSpecificRanks(), false)) continue;
                suggestions2.add("/" + command.Aliases().toArray()[0]);
            }
            event.setCompletions(suggestions2);
            return;
        }
        if (spaceIndex == -1) {
            return;
        }
        String argLine = cmdLine.substring(spaceIndex + 1, cmdLine.length());
        String[] args = Pattern.compile(" ", 16).split(argLine, -1);
        String commandName = cmdLine.substring(0, spaceIndex);
        ICommand command = this.Commands.get(commandName.toLowerCase());
        if (command != null && (suggestions = command.onTabComplete(event.getSender(), commandName, args)) != null) {
            event.setCompletions(suggestions);
        }
    }

    public void AddCommand(ICommand command) {
        for (String commandRoot : command.Aliases()) {
            this.Commands.put(commandRoot.toLowerCase(), command);
            command.SetCommandCenter(this);
        }
    }

    public void RemoveCommand(ICommand command) {
        for (String commandRoot : command.Aliases()) {
            this.Commands.remove(commandRoot.toLowerCase());
            command.SetCommandCenter(null);
        }
    }
}
