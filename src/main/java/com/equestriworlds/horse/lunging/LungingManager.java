package com.equestriworlds.horse.lunging;

import com.equestriworlds.MiniModule;
import com.equestriworlds.MiniPlugin;
import com.equestriworlds.horse.HorseManager;
import com.equestriworlds.horse.config.CustomHorse;
import com.equestriworlds.horse.config.HorseConfig;
import com.equestriworlds.util.C;
import com.equestriworlds.util.F;
import com.equestriworlds.util.UtilPlayer;
import com.equestriworlds.util.UtilTextMiddle;
import java.util.HashMap;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftEntity;
import org.bukkit.entity.AbstractHorse;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;

public class LungingManager
extends MiniModule<HorseManager> {
    private HashMap<Player, Integer> gaits = new HashMap();
    private HashMap<Player, Long> lastRightClick = new HashMap();
    private HashMap<Player, AbstractHorse> lungedHorse = new HashMap();
    private static HashMap<Player, Integer> lastTime = new HashMap();
    private static HashMap<Player, BukkitTask> tasks = new HashMap();

    public LungingManager(HorseManager plugin) {
        super("Lunging Manager", plugin);
    }

    @EventHandler
    public void onLunge(PlayerFishEvent event) {
        if (!(event.getCaught() instanceof AbstractHorse)) {
            return;
        }
        CustomHorse horse = ((HorseManager)this.Plugin).config.getHorseByHorse((CraftEntity)event.getCaught());
        Player player = event.getPlayer();
        if (horse == null) {
            UtilPlayer.message((Entity)player, F.main(this.getName(), "You can only lunge claimed horses"));
            return;
        }
        if (player.getInventory().getItemInMainHand().getType() == Material.FISHING_ROD) {
            event.setCancelled(true);
            this.lastRightClick.put(player, System.currentTimeMillis());
            this.lungedHorse.put(player, (AbstractHorse)event.getCaught());
            boolean firstTime = false;
            if (this.gaits.get((Object)player) != null) {
                if (this.gaits.get((Object)player) >= 4) {
                    return;
                }
                int level = this.gaits.get((Object)player);
                this.gaits.put(player, ++level);
            } else {
                this.gaits.put(player, 1);
                firstTime = true;
            }
            if (this.gaits.get((Object)player) == 0) {
                this.sendTitle(player);
                return;
            }
            if (firstTime || !firstTime && this.gaits.get((Object)player) == 1) {
                this.lunge((AbstractHorse)event.getCaught(), player);
            }
        }
    }

    @EventHandler
    public void onLeftClick(PlayerInteractEvent event) {
        if (event.getItem() == null) {
            return;
        }
        if (event.getItem().getType() != Material.FISHING_ROD) {
            return;
        }
        Player player = event.getPlayer();
        if (this.gaits.get((Object)player) == null) {
            return;
        }
        if (event.getAction() == Action.RIGHT_CLICK_AIR) {
            this.lastRightClick.put(player, System.currentTimeMillis());
        } else if (event.getAction() == Action.LEFT_CLICK_AIR) {
            if (this.lastRightClick.get((Object)player) + 100L > System.currentTimeMillis()) {
                return;
            }
            boolean firstTime = false;
            if (this.gaits.get((Object)player) != null) {
                if (this.gaits.get((Object)player) <= 0) {
                    return;
                }
                int level = this.gaits.get((Object)player);
                this.gaits.put(player, --level);
            } else {
                this.gaits.put(player, 1);
                firstTime = true;
            }
            if (firstTime) {
                this.lunge(this.lungedHorse.get((Object)player), player);
            }
        } else if (event.getAction() == Action.LEFT_CLICK_BLOCK) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void playerChangeItem(PlayerItemHeldEvent event) {
        Player player = event.getPlayer();
        if (player.getInventory().getItem(event.getPreviousSlot()) == null) {
            return;
        }
        if (player.getInventory().getItem(event.getPreviousSlot()).getType() == Material.FISHING_ROD && this.gaits.get((Object)player) != null) {
            this.gaits.remove((Object)player);
            this.lungedHorse.get((Object)player).setAI(true);
            this.lungedHorse.remove((Object)player);
            lastTime.remove((Object)player);
            tasks.remove((Object)player);
        }
    }

    private void lunge(final AbstractHorse horse, final Player player) {
        this.sendTitle(player);
        if (horse.hasAI()) {
            horse.setAI(false);
        }
        BukkitTask bukkitTask = new BukkitRunnable() {
            int level = (Integer)LungingManager.this.gaits.get((Object)player);
            final float radius = 6.0f;
            final float radPerSec = 2.0f;
            final float radPerTick = 0.1f;
            int time = LungingManager.this.getTime(player);

            public void run() {
                if (LungingManager.this.gaits.get((Object)player) == null) {
                    this.cancel();
                    return;
                }
                if ((Integer)LungingManager.this.gaits.get((Object)player) != this.level) {
                    this.cancel();
                    lastTime.put(player, this.time);
                    LungingManager.this.lunge(horse, player);
                    return;
                }
                Location loc = LungingManager.rotation(player.getLocation(), 6.0, 0.1f * (float)this.time, horse.getLocation());
                ++this.time;
                horse.teleport(loc);
            }
        }.runTaskTimer((Plugin)((HorseManager)this.Plugin).getPlugin(), 0L, (long)this.calculateSpeed(player));
        tasks.put(player, bukkitTask);
    }

    private String gaitName(int level) {
        if (level == 0) {
            return "Halted";
        }
        if (level == 1) {
            return "Walking";
        }
        if (level == 2) {
            return "Trotting";
        }
        if (level == 3) {
            return "Cantering";
        }
        if (level == 4) {
            return "Galloping";
        }
        return "";
    }

    private int calculateSpeed(Player player) {
        int level = this.gaits.get((Object)player);
        if (level == 0) {
            return 1000;
        }
        if (level == 1) {
            return 6;
        }
        if (level == 2) {
            return 4;
        }
        if (level == 3) {
            return 2;
        }
        if (level == 4) {
            return 1;
        }
        return 1000;
    }

    private void sendTitle(Player player) {
        int level = this.gaits.get((Object)player);
        UtilTextMiddle.display(" ", C.cYellow + this.gaitName(level), 5, 40, 1, player);
    }

    private static Location rotation(Location center, double radius, double angleInRadian, Location horseLocation) {
        double x = center.getX() + radius * Math.cos(angleInRadian);
        double z = center.getZ() + radius * Math.sin(angleInRadian);
        double y = center.getY();
        Location loc = new Location(center.getWorld(), x, y, z);
        Vector difference = loc.toVector().clone().subtract(horseLocation.toVector());
        loc.setDirection(difference);
        return loc;
    }

    private int getTime(Player player) {
        if (lastTime.get((Object)player) != null) {
            return lastTime.get((Object)player);
        }
        return 0;
    }

}
