package com.equestriworlds.util;

import java.io.PrintStream;
import net.minecraft.server.v1_12_R1.IProgressUpdate;
import net.minecraft.server.v1_12_R1.MinecraftServer;

public class ConvertProgressUpdater
implements IProgressUpdate {
    private long b = MinecraftServer.aw();

    public ConvertProgressUpdater(MinecraftServer paramMinecraftServer) {
    }

    public void a(String paramString) {
    }

    public void a(int paramInt) {
        if (MinecraftServer.aw() - this.b >= 1000L) {
            this.b = MinecraftServer.aw();
            System.out.println("Converting... " + paramInt + "%");
        }
    }

    public void c(String paramString) {
    }
}
