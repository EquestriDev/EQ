package com.equestriworlds.util;

import com.equestriworlds.util.UtilMath;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Player;

public class UtilParticle {
    public static /* varargs */ void PlayParticle(Particle particle, Location location, float offsetX, float offsetY, float offsetZ, float speed, int count, ViewDist dist, Player ... players) {
        for (Player player : players) {
            if (UtilMath.offset(player.getLocation(), location) > (double)dist.getDist()) continue;
            player.spawnParticle(particle, location, count, (double)offsetX, (double)offsetY, (double)offsetZ);
        }
    }

    public static enum ViewDist {
        SHORT(8),
        NORMAL(24),
        LONG(48),
        LONGER(96),
        MAX(256);
        
        private int _dist;

        private ViewDist(int dist) {
            this._dist = dist;
        }

        public int getDist() {
            return this._dist;
        }
    }

}
