/*
 * Decompiled with CFR 0_133.
 * 
 * Could not load the following classes:
 *  net.minecraft.server.v1_12_R1.ChatMessageType
 *  net.minecraft.server.v1_12_R1.EntityPlayer
 *  net.minecraft.server.v1_12_R1.IChatBaseComponent
 *  net.minecraft.server.v1_12_R1.IChatBaseComponent$ChatSerializer
 *  net.minecraft.server.v1_12_R1.Packet
 *  net.minecraft.server.v1_12_R1.PacketPlayOutChat
 *  net.minecraft.server.v1_12_R1.PlayerConnection
 *  org.bukkit.command.CommandSender
 *  org.bukkit.command.ConsoleCommandSender
 *  org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer
 *  org.bukkit.entity.Player
 */
package com.equestriworlds.json;

import com.equestriworlds.json.ChildJsonMessage;
import com.equestriworlds.json.ClickEvent;
import com.equestriworlds.json.Color;
import com.equestriworlds.json.HoverEvent;
import com.equestriworlds.util.UtilServer;
import net.minecraft.server.v1_12_R1.ChatMessageType;
import net.minecraft.server.v1_12_R1.EntityPlayer;
import net.minecraft.server.v1_12_R1.IChatBaseComponent;
import net.minecraft.server.v1_12_R1.Packet;
import net.minecraft.server.v1_12_R1.PacketPlayOutChat;
import net.minecraft.server.v1_12_R1.PlayerConnection;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

public class JsonMessage {
    StringBuilder Builder;

    public JsonMessage(String text) {
        this(new StringBuilder(), text);
    }

    JsonMessage(StringBuilder builder, String text) {
        this.Builder = builder;
        this.Builder.append("{\"text\":\"" + text + "\"");
    }

    public JsonMessage color(String color) {
        this.Builder.append(", color:" + color);
        return this;
    }

    public JsonMessage bold() {
        this.Builder.append(", bold:true");
        return this;
    }

    public JsonMessage italic() {
        this.Builder.append(", italic:true");
        return this;
    }

    public JsonMessage underlined() {
        this.Builder.append(", underlined:true");
        return this;
    }

    public JsonMessage strikethrough() {
        this.Builder.append(", strikethrough:true");
        return this;
    }

    public JsonMessage obfuscated() {
        this.Builder.append(", obfuscated:true");
        return this;
    }

    public ChildJsonMessage extra(String text) {
        this.Builder.append(", \"extra\":[");
        return new ChildJsonMessage(this, this.Builder, text);
    }

    public JsonMessage click(String action, String value) {
        this.Builder.append(", \"clickEvent\":{\"action\":\"" + action + "\",\"value\":\"" + value + "\"}");
        return this;
    }

    public JsonMessage hover(String action, String value) {
        this.Builder.append(", \"hoverEvent\":{\"action\":\"" + action + "\",\"value\":\"" + value + "\"}");
        return this;
    }

    public JsonMessage click(ClickEvent event, String value) {
        return this.click(event.toString(), value);
    }

    public JsonMessage hover(HoverEvent event, String value) {
        return this.hover(event.toString(), value);
    }

    public JsonMessage color(Color color) {
        return this.color(color.toString());
    }

    public String toString() {
        this.Builder.append("}");
        return this.Builder.toString();
    }

    public void sendToPlayer(Player player) {
        UtilServer.getServer().dispatchCommand((CommandSender)UtilServer.getServer().getConsoleSender(), "tellraw " + player.getName() + " " + this.toString());
    }

    public /* varargs */ void send(MessageType messageType, Player ... players) {
        this.send(messageType, false, players);
    }

    public /* varargs */ void send(MessageType messageType, boolean defaultToChat, Player ... players) {
        PacketPlayOutChat chatPacket = new PacketPlayOutChat(IChatBaseComponent.ChatSerializer.a((String)this.toString()), ChatMessageType.GAME_INFO);
        for (Player player : players) {
            if (!defaultToChat && messageType == MessageType.ABOVE_HOTBAR) continue;
            ((CraftPlayer)player).getHandle().playerConnection.sendPacket((Packet)chatPacket);
        }
    }

    public static enum MessageType {
        CHAT_BOX(0),
        SYSTEM_MESSAGE(1),
        ABOVE_HOTBAR(2);
        
        private byte _id;

        private MessageType(int id) {
            this._id = (byte)id;
        }

        public byte getId() {
            return this._id;
        }
    }

}
