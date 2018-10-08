/*
 * Decompiled with CFR 0_133.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.BiMap
 *  net.minecraft.server.v1_12_R1.EnumProtocol
 *  net.minecraft.server.v1_12_R1.Packet
 *  net.minecraft.server.v1_12_R1.PacketDataSerializer
 *  net.minecraft.server.v1_12_R1.PacketListener
 */
package com.equestriworlds.util;

import com.google.common.collect.BiMap;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Map;
import net.minecraft.server.v1_12_R1.EnumProtocol;
import net.minecraft.server.v1_12_R1.Packet;
import net.minecraft.server.v1_12_R1.PacketDataSerializer;
import net.minecraft.server.v1_12_R1.PacketListener;

public class ProtocolInjector {
    public static void addPacket(EnumProtocol protocol, boolean clientbound, int id, Class<? extends Packet> packet) throws NoSuchFieldException, IllegalAccessException {
        Field packets = !clientbound ? EnumProtocol.class.getDeclaredField("h") : EnumProtocol.class.getDeclaredField("i");
        packets.setAccessible(true);
        BiMap pMap = (BiMap)packets.get((Object)protocol);
        pMap.put((Object)id, packet);
        Field map = EnumProtocol.class.getDeclaredField("f");
        map.setAccessible(true);
        Map protocolMap = (Map)map.get(null);
        protocolMap.put(packet, protocol);
    }

    public static class PacketLoginCompression
    implements Packet {
        private int threshold;

        public PacketLoginCompression(int threshold) {
            this.threshold = threshold;
        }

        public void a(PacketDataSerializer packetdataserializer) throws IOException {
        }

        public void b(PacketDataSerializer packetdataserializer) throws IOException {
            packetdataserializer.b(this.threshold);
        }

        public void handle(PacketListener packetlistener) {
        }

        public void a(PacketListener arg0) {
        }
    }

    public static class PacketPlayResourcePackSend
    implements Packet {
        private String url;
        private String hash;

        public PacketPlayResourcePackSend(String url, String hash) {
            this.url = url;
            this.hash = hash;
        }

        public void a(PacketDataSerializer packetdataserializer) throws IOException {
        }

        public void b(PacketDataSerializer packetdataserializer) throws IOException {
            packetdataserializer.a(this.url);
            packetdataserializer.a(this.hash);
        }

        public void handle(PacketListener packetlistener) {
        }

        public void a(PacketListener arg0) {
        }
    }

    public static class PacketPlayResourcePackStatus
    implements Packet {
        public void a(PacketDataSerializer packetdataserializer) throws IOException {
            packetdataserializer.c(255);
            packetdataserializer.a();
        }

        public void b(PacketDataSerializer packetdataserializer) throws IOException {
        }

        public void handle(PacketListener packetlistener) {
        }

        public void a(PacketListener arg0) {
        }
    }

}
