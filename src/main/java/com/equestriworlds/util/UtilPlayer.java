package com.equestriworlds.util;

import com.equestriworlds.common.Rank;
import com.equestriworlds.util.C;
import com.equestriworlds.util.F;
import com.equestriworlds.util.UtilAlg;
import com.equestriworlds.util.UtilBlock;
import com.equestriworlds.util.UtilMath;
import com.equestriworlds.util.UtilServer;
import java.io.PrintStream;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import net.minecraft.server.v1_12_R1.EntityPlayer;
import net.minecraft.server.v1_12_R1.Packet;
import net.minecraft.server.v1_12_R1.PlayerConnection;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class UtilPlayer {
    private static boolean hasIntersection(Vector3D p1, Vector3D p2, Vector3D min, Vector3D max) {
        double epsilon = 9.999999747378752E-5;
        Vector3D d = p2.subtract(p1).multiply(0.5);
        Vector3D e = max.subtract(min).multiply(0.5);
        Vector3D c = p1.add(d).subtract(min.add(max).multiply(0.5));
        Vector3D ad = d.abs();
        if (Math.abs(c.x) > e.x + ad.x) {
            return false;
        }
        if (Math.abs(c.y) > e.y + ad.y) {
            return false;
        }
        if (Math.abs(c.z) > e.z + ad.z) {
            return false;
        }
        if (Math.abs(d.y * c.z - d.z * c.y) > e.y * ad.z + e.z * ad.y + 9.999999747378752E-5) {
            return false;
        }
        if (Math.abs(d.z * c.x - d.x * c.z) > e.z * ad.x + e.x * ad.z + 9.999999747378752E-5) {
            return false;
        }
        if (Math.abs(d.x * c.y - d.y * c.x) > e.x * ad.y + e.y * ad.x + 9.999999747378752E-5) {
            return false;
        }
        return true;
    }

    public static Player getPlayerInSight(Player p, int range, boolean lineOfSight) {
        Location observerPos = p.getEyeLocation();
        Vector3D observerDir = new Vector3D(observerPos.getDirection());
        Vector3D observerStart = new Vector3D(observerPos);
        Vector3D observerEnd = observerStart.add(observerDir.multiply(range));
        Player hit = null;
        for (Entity entity : p.getNearbyEntities((double)range, (double)range, (double)range)) {
            Vector3D maximum;
            Vector3D minimum;
            Vector3D targetPos;
            if (entity == p) continue;
            double theirDist = p.getEyeLocation().distance(entity.getLocation());
            if (lineOfSight && ((Block)p.getLastTwoTargetBlocks(UtilBlock.blockAirFoliageSet, (int)Math.ceil(theirDist)).get(0)).getLocation().distance(p.getEyeLocation()) + 1.0 < theirDist || !UtilPlayer.hasIntersection(observerStart, observerEnd, minimum = (targetPos = new Vector3D(entity.getLocation())).add(-0.5, 0.0, -0.5), maximum = targetPos.add(0.5, 1.67, 0.5)) || hit != null && hit.getLocation().distanceSquared(observerPos) <= entity.getLocation().distanceSquared(observerPos)) continue;
            hit = (Player)entity;
        }
        return hit;
    }

    public static Entity getEntityInSight(Player player, int rangeToScan, boolean avoidAllies, boolean avoidNonLiving, boolean lineOfSight, float expandBoxesPercentage) {
        Location observerPos = player.getEyeLocation();
        Vector3D observerDir = new Vector3D(observerPos.getDirection());
        Vector3D observerStart = new Vector3D(observerPos);
        Vector3D observerEnd = observerStart.add(observerDir.multiply(rangeToScan));
        Entity hit = null;
        for (Entity entity : player.getNearbyEntities((double)rangeToScan, (double)rangeToScan, (double)rangeToScan)) {
            Vector3D maximum;
            Vector3D targetPos;
            Vector3D minimum;
            float width;
            if (entity == player || avoidNonLiving && !(entity instanceof LivingEntity)) continue;
            double theirDist = player.getEyeLocation().distance(entity.getLocation());
            if (lineOfSight && ((Block)player.getLastTwoTargetBlocks(UtilBlock.blockAirFoliageSet, (int)Math.ceil(theirDist)).get(0)).getLocation().distance(player.getEyeLocation()) + 1.0 < theirDist || !UtilPlayer.hasIntersection(observerStart, observerEnd, minimum = (targetPos = new Vector3D(entity.getLocation())).add(- (width = ((CraftEntity)entity).getHandle().width / 1.8f * expandBoxesPercentage), -0.1 / (double)expandBoxesPercentage, - width), maximum = targetPos.add(width, ((CraftEntity)entity).getHandle().length * expandBoxesPercentage, width)) || hit != null && hit.getLocation().distanceSquared(observerPos) <= entity.getLocation().distanceSquared(observerPos)) continue;
            hit = entity;
        }
        return hit;
    }

    public static void message(Entity client, TextComponent textComponent) {
        if (client == null) {
            return;
        }
        if (!(client instanceof Player)) {
            return;
        }
        ((Player)client).spigot().sendMessage((BaseComponent)textComponent);
    }

    public static void message(Entity client, LinkedList<String> messageList) {
        UtilPlayer.message(client, messageList, false);
    }

    public static void message(Entity client, String message) {
        UtilPlayer.message(client, message, false);
    }

    public static void message(Entity client, LinkedList<String> messageList, boolean wiki) {
        for (String curMessage : messageList) {
            UtilPlayer.message(client, curMessage, wiki);
        }
    }

    public static void message(Entity client, String message, boolean wiki) {
        if (client == null) {
            return;
        }
        if (!(client instanceof Player)) {
            return;
        }
        ((Player)client).sendMessage(message);
    }

    public static Player searchExact(String name) {
        for (Player cur : UtilServer.getPlayers()) {
            if (!cur.getName().equalsIgnoreCase(name)) continue;
            return cur;
        }
        return null;
    }

    public static Player searchExact(UUID uuid) {
        return UtilServer.getServer().getPlayer(uuid);
    }

    public static String searchCollection(Player caller, String player, Collection<String> coll, String collName, boolean inform) {
        LinkedList<String> matchList = new LinkedList<String>();
        for (String cur : coll) {
            if (cur.equalsIgnoreCase(player)) {
                return cur;
            }
            if (!cur.toLowerCase().contains(player.toLowerCase())) continue;
            matchList.add(cur);
        }
        if (matchList.size() != 1) {
            if (!inform) {
                return null;
            }
            UtilPlayer.message((Entity)caller, F.main(collName + " Search", "" + C.mCount + matchList.size() + C.mBody + " matches for [" + C.mElem + player + C.mBody + "]."));
            if (matchList.size() > 0) {
                String matchString = "";
                for (String cur : matchList) {
                    matchString = matchString + cur + " ";
                }
                UtilPlayer.message((Entity)caller, F.main(collName + " Search", "" + C.mBody + " Matches [" + C.mElem + matchString + C.mBody + "]."));
            }
            return null;
        }
        return (String)matchList.get(0);
    }

    public static Player searchOnline(Player caller, String player, boolean inform) {
        LinkedList<Player> matchList = new LinkedList<Player>();
        for (Player cur : UtilServer.getPlayers()) {
            if (cur.getName().equalsIgnoreCase(player)) {
                return cur;
            }
            if (!cur.getName().toLowerCase().contains(player.toLowerCase())) continue;
            matchList.add(cur);
        }
        if (matchList.size() != 1) {
            if (!inform) {
                return null;
            }
            UtilPlayer.message((Entity)caller, F.main("Online Player Search", "" + C.mCount + matchList.size() + C.mBody + " matches for [" + C.mElem + player + C.mBody + "]."));
            if (matchList.size() > 0) {
                String matchString = "";
                for (Player cur : matchList) {
                    matchString = matchString + F.elem(cur.getName()) + ", ";
                }
                if (matchString.length() > 1) {
                    matchString = matchString.substring(0, matchString.length() - 2);
                }
                UtilPlayer.message((Entity)caller, F.main("Online Player Search", "" + C.mBody + "Matches [" + C.mElem + matchString + C.mBody + "]."));
            }
            return null;
        }
        return (Player)matchList.get(0);
    }

    public static OfflinePlayer searchOffline(Player caller, String player, boolean inform) {
        LinkedList<OfflinePlayer> matchList = new LinkedList<OfflinePlayer>();
        for (OfflinePlayer cur : UtilServer.getOfflinePlayers()) {
            if (cur.getName().equalsIgnoreCase(player)) {
                return cur;
            }
            if (!cur.getName().toLowerCase().contains(player.toLowerCase())) continue;
            matchList.add(cur);
        }
        if (matchList.size() != 1) {
            if (!inform) {
                return null;
            }
            UtilPlayer.message((Entity)caller, F.main("Player Search", "" + C.mCount + matchList.size() + C.mBody + " matches for [" + C.mElem + player + C.mBody + "]."));
            if (matchList.size() > 0) {
                String matchString = "";
                for (OfflinePlayer cur : matchList) {
                    matchString = matchString + F.elem(cur.getName()) + ", ";
                }
                if (matchString.length() > 1) {
                    matchString = matchString.substring(0, matchString.length() - 2);
                }
                UtilPlayer.message((Entity)caller, F.main("Player Search", "" + C.mBody + "Matches [" + C.mElem + matchString + C.mBody + "]."));
            }
            return null;
        }
        return (OfflinePlayer)matchList.get(0);
    }

    public static LinkedList<Player> matchOnline(Player caller, String players, boolean inform) {
        LinkedList<Player> matchList = new LinkedList<Player>();
        String failList = "";
        for (String cur : players.split(",")) {
            Player match = UtilPlayer.searchOnline(caller, cur, inform);
            if (match != null) {
                matchList.add(match);
                continue;
            }
            failList = failList + cur + " ";
        }
        if (inform && failList.length() > 0) {
            failList = failList.substring(0, failList.length() - 1);
            UtilPlayer.message((Entity)caller, F.main("Online Player(s) Search", "" + C.mBody + "Invalid [" + C.mElem + failList + C.mBody + "]."));
        }
        return matchList;
    }

    public static LinkedList<Player> getNearby(Location loc, double maxDist) {
        LinkedList<Player> nearbyMap = new LinkedList<Player>();
        for (Player cur : loc.getWorld().getPlayers()) {
            double dist;
            if (cur.isDead() || (dist = loc.toVector().subtract(cur.getLocation().toVector()).length()) > maxDist) continue;
            for (int i = 0; i < nearbyMap.size(); ++i) {
                if (dist >= loc.toVector().subtract(nearbyMap.get(i).getLocation().toVector()).length()) continue;
                nearbyMap.add(i, cur);
                break;
            }
            if (nearbyMap.contains((Object)cur)) continue;
            nearbyMap.addLast(cur);
        }
        return nearbyMap;
    }

    public static Player getClosest(Location loc, Collection<Player> ignore) {
        Player best = null;
        double bestDist = 0.0;
        for (Player cur : loc.getWorld().getPlayers()) {
            if (cur.isDead() || ignore != null && ignore.contains((Object)cur)) continue;
            double dist = UtilMath.offset(cur.getLocation(), loc);
            if (best != null && dist >= bestDist) continue;
            best = cur;
            bestDist = dist;
        }
        return best;
    }

    public static Player getClosest(Location loc, Entity ignore) {
        Player best = null;
        double bestDist = 0.0;
        for (Player cur : loc.getWorld().getPlayers()) {
            if (cur.isDead() || ignore != null && ignore.equals((Object)cur)) continue;
            double dist = UtilMath.offset(cur.getLocation(), loc);
            if (best != null && dist >= bestDist) continue;
            best = cur;
            bestDist = dist;
        }
        return best;
    }

    public static void kick(Player player, String module, String message) {
        UtilPlayer.kick(player, module, message, true);
    }

    public static void kick(Player player, String module, String message, boolean log) {
        if (player == null) {
            return;
        }
        String out = (Object)ChatColor.RED + module + (Object)ChatColor.WHITE + " - " + (Object)ChatColor.YELLOW + message;
        player.kickPlayer(out);
        if (log) {
            System.out.println("Kicked Client [" + player.getName() + "] for [" + module + " - " + message + "]");
        }
    }

    public static HashMap<Player, Double> getInRadius(Location loc, double dR) {
        HashMap<Player, Double> players = new HashMap<Player, Double>();
        for (Player cur : loc.getWorld().getPlayers()) {
            double offset = UtilMath.offset(loc, cur.getLocation());
            if (offset >= dR) continue;
            players.put(cur, 1.0 - offset / dR);
        }
        return players;
    }

    public static HashMap<Player, Double> getPlayersInPyramid(Player player, double angleLimit, double distance) {
        HashMap<Player, Double> players = new HashMap<Player, Double>();
        for (Player cur : player.getWorld().getPlayers()) {
            double offset = Math.min(UtilMath.offset(player.getEyeLocation(), cur.getEyeLocation()), UtilMath.offset(player.getEyeLocation(), cur.getLocation()));
            if (offset >= distance || !UtilAlg.isTargetInPlayerPyramid(player, cur, angleLimit)) continue;
            players.put(cur, 1.0 - offset / distance);
        }
        return players;
    }

    public static void health(Player player, double mod) {
        if (player.isDead()) {
            return;
        }
        double health = player.getHealth() + mod;
        if (health < 0.0) {
            health = 0.0;
        }
        if (health > player.getMaxHealth()) {
            health = player.getMaxHealth();
        }
        player.setHealth(health);
    }

    public static void hunger(Player player, int mod) {
        if (player.isDead()) {
            return;
        }
        int hunger = player.getFoodLevel() + mod;
        if (hunger < 0) {
            hunger = 0;
        }
        if (hunger > 20) {
            hunger = 20;
        }
        player.setFoodLevel(hunger);
    }

    public static boolean isOnline(String name) {
        return UtilPlayer.searchExact(name) != null;
    }

    public static String safeNameLength(String name) {
        if (name.length() > 16) {
            name = name.substring(0, 16);
        }
        return name;
    }

    public static /* varargs */ void sendPacket(Player player, Packet ... packets) {
        PlayerConnection connection = ((CraftPlayer)player).getHandle().playerConnection;
        for (Packet packet : packets) {
            connection.sendPacket(packet);
        }
    }

    public static double getMaxJump(Player player) {
        if (player.isOp()) {
            return 2.0;
        }
        Rank rank = Rank.getRank(player);
        switch (rank) {
            case OWNER: {
                return 2.0;
            }
            case COOWNER: {
                return 2.0;
            }
            case DEV: {
                return 2.0;
            }
            case HEADADMIN: {
                return 1.38;
            }
            case ADMIN: {
                return 1.38;
            }
            case HEADMOD: {
                return 1.38;
            }
            case MOD: {
                return 1.38;
            }
            case JRMOD: {
                return 1.38;
            }
            case TJRMOD: {
                return 1.38;
            }
            case VIPPP: {
                return 1.38;
            }
            case VIPP: {
                return 1.38;
            }
            case VIP: {
                return 1.38;
            }
            case Level9: {
                return 1.37;
            }
            case Level8: {
                return 1.37;
            }
            case Level7: {
                return 1.37;
            }
            case Level6: {
                return 1.37;
            }
            case Level5: {
                return 1.24;
            }
            case Level4: {
                return 1.13;
            }
            case Level3: {
                return 1.03;
            }
            case Level2: {
                return 0.91;
            }
            case Level1: {
                return 0.79;
            }
            case PLAYER: {
                return 0.79;
            }
        }
        return 0.79;
    }

    private static class Vector3D {
        private final double x;
        private final double y;
        private final double z;

        private Vector3D(double x, double y, double z) {
            this.x = x;
            this.y = y;
            this.z = z;
        }

        private Vector3D(Location location) {
            this(location.toVector());
        }

        private Vector3D(Vector vector) {
            if (vector == null) {
                throw new IllegalArgumentException("Vector cannot be NULL.");
            }
            this.x = vector.getX();
            this.y = vector.getY();
            this.z = vector.getZ();
        }

        private Vector3D abs() {
            return new Vector3D(Math.abs(this.x), Math.abs(this.y), Math.abs(this.z));
        }

        private Vector3D add(double x, double y, double z) {
            return new Vector3D(this.x + x, this.y + y, this.z + z);
        }

        private Vector3D add(Vector3D other) {
            if (other == null) {
                throw new IllegalArgumentException("other cannot be NULL");
            }
            return new Vector3D(this.x + other.x, this.y + other.y, this.z + other.z);
        }

        private Vector3D multiply(double factor) {
            return new Vector3D(this.x * factor, this.y * factor, this.z * factor);
        }

        private Vector3D multiply(int factor) {
            return new Vector3D(this.x * (double)factor, this.y * (double)factor, this.z * (double)factor);
        }

        private Vector3D subtract(Vector3D other) {
            if (other == null) {
                throw new IllegalArgumentException("other cannot be NULL");
            }
            return new Vector3D(this.x - other.x, this.y - other.y, this.z - other.z);
        }
    }

}
