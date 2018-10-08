/*
 * Decompiled with CFR 0_133.
 * 
 * Could not load the following classes:
 *  net.md_5.bungee.api.ChatColor
 *  net.md_5.bungee.api.chat.BaseComponent
 *  net.md_5.bungee.api.chat.ClickEvent
 *  net.md_5.bungee.api.chat.ClickEvent$Action
 *  net.md_5.bungee.api.chat.ComponentBuilder
 *  net.md_5.bungee.api.chat.HoverEvent
 *  net.md_5.bungee.api.chat.HoverEvent$Action
 *  net.md_5.bungee.api.chat.TextComponent
 *  org.bukkit.Bukkit
 *  org.bukkit.Server
 *  org.bukkit.Server$Spigot
 *  org.bukkit.command.CommandSender
 *  org.bukkit.configuration.file.YamlConfiguration
 *  org.bukkit.entity.Player
 *  org.bukkit.entity.Player$Spigot
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.player.AsyncPlayerChatEvent
 *  org.bukkit.plugin.java.JavaPlugin
 */
package com.equestriworlds.certifications;

import com.equestriworlds.MiniPlugin;
import com.equestriworlds.certifications.commands.CertificationCommand;
import com.equestriworlds.certifications.configurations.configuration;
import com.equestriworlds.certifications.enums.Discipline;
import com.equestriworlds.certifications.enums.TestingPlayer;
import com.equestriworlds.certifications.enums.TestingPlayerToken;
import com.equestriworlds.command.ICommand;
import com.equestriworlds.util.C;
import com.equestriworlds.util.F;
import java.util.HashMap;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class CertificationsManager
extends MiniPlugin {
    public HashMap<Player, TestingPlayer> test = new HashMap();

    public CertificationsManager(JavaPlugin plugin) {
        super("Certifications", plugin);
        new configuration();
        this.addCommand(new CertificationCommand(this));
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {
        if (!this.test.containsKey((Object)event.getPlayer())) {
            return;
        }
        event.setCancelled(true);
        TestingPlayer player = this.test.get((Object)event.getPlayer());
        String message = C.clear(event.getMessage());
        if (Discipline.getAnswer(player).equalsIgnoreCase(message)) {
            ++player.token.score;
        }
        ++player.token.answer;
        this.sendQuestion(player);
    }

    public void sendQuestion(TestingPlayer testingPlayer) {
        if (testingPlayer.token.answer == 11) {
            this.score(testingPlayer);
            return;
        }
        Player player = testingPlayer.player;
        Discipline discipline = testingPlayer.token.discipline;
        String num = this.num(testingPlayer.token.answer);
        this.clearChat(player);
        if (this.booleanQuestion(discipline, testingPlayer.token.answer)) {
            CertificationsManager.createMessage(configuration.config.getString(discipline.file + "." + num + ".Question"), null, "&bClick your answer!", player);
            player.sendMessage(C.convert("&7&m*----------------------------------------------------------"));
            CertificationsManager.createMessage(configuration.config.getString(discipline.file + "." + num + ".AnswerOne"), "A", " &bA. ", player);
            CertificationsManager.createMessage(configuration.config.getString(discipline.file + "." + num + ".AnswerTwo"), "B", " &bB. ", player);
            return;
        }
        CertificationsManager.createMessage(configuration.config.getString(discipline.file + "." + num + ".Question"), null, "&bClick your answer!", player);
        player.sendMessage(C.convert("&7&m*----------------------------------------------------------"));
        CertificationsManager.createMessage(configuration.config.getString(discipline.file + "." + num + ".AnswerOne"), "A", " &bA. ", player);
        CertificationsManager.createMessage(configuration.config.getString(discipline.file + "." + num + ".AnswerTwo"), "B", " &bB. ", player);
        CertificationsManager.createMessage(configuration.config.getString(discipline.file + "." + num + ".AnswerThree"), "C", " &bC. ", player);
        CertificationsManager.createMessage(configuration.config.getString(discipline.file + "." + num + ".AnswerFour"), "D", " &bD. ", player);
    }

    private static void createMessage(String display, String command, String letter, Player player) {
        TextComponent message = new TextComponent(TextComponent.fromLegacyText((String)ChatColor.translateAlternateColorCodes((char)'&', (String)display)));
        message.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, command));
        message.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(C.convert(letter)).create()));
        player.spigot().sendMessage((BaseComponent)message);
    }

    private void clearChat(Player player) {
        for (int i = 0; i <= 100; ++i) {
            player.sendMessage("");
        }
    }

    private String num(int num) {
        switch (num) {
            case 1: {
                return "One";
            }
            case 2: {
                return "Two";
            }
            case 3: {
                return "Three";
            }
            case 4: {
                return "Four";
            }
            case 5: {
                return "Five";
            }
            case 6: {
                return "Six";
            }
            case 7: {
                return "Seven";
            }
            case 8: {
                return "Eight";
            }
            case 9: {
                return "Nine";
            }
            case 10: {
                return "Ten";
            }
        }
        return "";
    }

    private boolean booleanQuestion(Discipline discipline, int answer) {
        return discipline == Discipline.DRESSAGE && answer == 5;
    }

    private void score(TestingPlayer player) {
        this.clearChat(player.player);
        if (player.token.score >= 8) {
            Bukkit.getServer().spigot().broadcast((BaseComponent)new TextComponent(""));
            Bukkit.getServer().spigot().broadcast((BaseComponent)new TextComponent(new TextComponent(TextComponent.fromLegacyText((String)ChatColor.translateAlternateColorCodes((char)'&', (String)F.main(this.getName(), "&3Congratulations to &b&l" + player.player.getName() + " &3for passing their &b&l" + player.token.discipline.name + " Certification &3with a score of &b&l" + this.getPercentage(player.token.score) + "&3%"))))));
            Bukkit.getServer().spigot().broadcast((BaseComponent)new TextComponent(""));
            Bukkit.getServer().dispatchCommand((CommandSender)Bukkit.getConsoleSender(), "pex user " + player.player.getName() + " add " + player.token.discipline.perm);
        } else {
            player.player.sendMessage(F.main(this.getName(), C.convert("&cUnfortunately you only scored &4&l" + this.getPercentage(player.token.score) + "&c% on your &4&l" + player.token.discipline.name + " Certification")));
        }
        this.test.remove((Object)player.player);
    }

    private int getPercentage(double currentValue) {
        float percent = (float)(currentValue / 10.0 * 100.0);
        return (int)percent;
    }
}
