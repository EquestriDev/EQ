/*
 * Decompiled with CFR 0_133.
 * 
 * Could not load the following classes:
 *  net.minecraft.server.v1_12_R1.EntityPlayer
 *  net.minecraft.server.v1_12_R1.IChatBaseComponent
 *  net.minecraft.server.v1_12_R1.IChatBaseComponent$ChatSerializer
 *  net.minecraft.server.v1_12_R1.Packet
 *  net.minecraft.server.v1_12_R1.PacketPlayOutPlayerListHeaderFooter
 *  net.minecraft.server.v1_12_R1.PacketPlayOutScoreboardTeam
 *  net.minecraft.server.v1_12_R1.PlayerConnection
 *  org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer
 *  org.bukkit.entity.Entity
 *  org.bukkit.entity.Player
 */
package com.equestriworlds.util;

import com.equestriworlds.common.Rank;
import com.equestriworlds.util.C;
import com.equestriworlds.util.UtilField;
import com.equestriworlds.util.UtilServer;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.UUID;
import net.minecraft.server.v1_12_R1.EntityPlayer;
import net.minecraft.server.v1_12_R1.IChatBaseComponent;
import net.minecraft.server.v1_12_R1.Packet;
import net.minecraft.server.v1_12_R1.PacketPlayOutPlayerListHeaderFooter;
import net.minecraft.server.v1_12_R1.PacketPlayOutScoreboardTeam;
import net.minecraft.server.v1_12_R1.PlayerConnection;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

public class UtilTabList {
    public static void setPlayerTag(Entity entity, String prefix, String suffix) {
        String name = UUID.randomUUID().toString().substring(0, 16);
        PacketPlayOutScoreboardTeam packet = new PacketPlayOutScoreboardTeam();
        Class<?> clas = packet.getClass();
        Field team_name = UtilField.getField(clas, "a");
        Field display_name = UtilField.getField(clas, "b");
        Field prefix1 = UtilField.getField(clas, "c");
        Field suffix1 = UtilField.getField(clas, "d");
        Field members = UtilField.getField(clas, "h");
        Field param_int = UtilField.getField(clas, "i");
        Field pack_option = UtilField.getField(clas, "j");
        UtilField.setField((Object)packet, team_name, name);
        UtilField.setField((Object)packet, display_name, entity.getName());
        UtilField.setField((Object)packet, prefix1, prefix);
        UtilField.setField((Object)packet, suffix1, suffix);
        UtilField.setField((Object)packet, members, Arrays.asList(entity.getName()));
        UtilField.setField((Object)packet, param_int, 0);
        UtilField.setField((Object)packet, pack_option, 1);
        for (Player p : UtilServer.getPlayers()) {
            CraftPlayer cp = (CraftPlayer)p;
            cp.getHandle().playerConnection.sendPacket((Packet)packet);
        }
    }

    public static void setPlayerRank(Rank rank) {
        String name = rank.toString();
        PacketPlayOutScoreboardTeam packet = new PacketPlayOutScoreboardTeam();
        Class<?> clas = packet.getClass();
        Field team_name = UtilField.getField(clas, "a");
        Field display_name = UtilField.getField(clas, "b");
        Field prefix1 = UtilField.getField(clas, "c");
        Field suffix1 = UtilField.getField(clas, "d");
        Field members = UtilField.getField(clas, "h");
        Field param_int = UtilField.getField(clas, "i");
        Field pack_option = UtilField.getField(clas, "j");
        UtilField.setField((Object)packet, team_name, rank.Index + name);
        UtilField.setField((Object)packet, display_name, rank.Name);
        UtilField.setField((Object)packet, prefix1, rank.equals((Object)Rank.PLAYER) ? C.Reset : rank.GetTag(true, true) + C.Reset + " ");
        UtilField.setField((Object)packet, suffix1, "");
        UtilField.setField((Object)packet, members, Rank.getPlayerNamesOfRank(rank));
        UtilField.setField((Object)packet, param_int, 0);
        UtilField.setField((Object)packet, pack_option, 1);
        for (Player p : UtilServer.getPlayers()) {
            CraftPlayer cp = (CraftPlayer)p;
            cp.getHandle().playerConnection.sendPacket((Packet)packet);
        }
    }

    public static void setHeaderAndFooter(String header, String footer) {
        for (Player player : UtilServer.getPlayers()) {
            CraftPlayer cp = (CraftPlayer)player;
            PacketPlayOutPlayerListHeaderFooter packet = new PacketPlayOutPlayerListHeaderFooter();
            Class<?> clas = packet.getClass();
            IChatBaseComponent header1 = IChatBaseComponent.ChatSerializer.a((String)("{\"text\": \"" + C.convert(header) + "\"}"));
            IChatBaseComponent footer1 = IChatBaseComponent.ChatSerializer.a((String)("{\"text\": \"" + C.convert(footer) + "\"}"));
            Field head = UtilField.getField(clas, "a");
            Field foot = UtilField.getField(clas, "b");
            UtilField.setField((Object)packet, head, (Object)header1);
            UtilField.setField((Object)packet, foot, (Object)footer1);
            cp.getHandle().playerConnection.sendPacket((Packet)packet);
        }
    }
}
