package com.equestriworlds.recharge;

import com.equestriworlds.MiniPlugin;
import com.equestriworlds.recharge.RechargeData;
import com.equestriworlds.recharge.RechargeEvent;
import com.equestriworlds.recharge.RechargedEvent;
import com.equestriworlds.update.UpdateEvent;
import com.equestriworlds.update.UpdateType;
import com.equestriworlds.util.F;
import com.equestriworlds.util.UtilPlayer;
import com.equestriworlds.util.UtilServer;
import com.equestriworlds.util.UtilTime;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;
import java.util.function.Predicate;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class Recharge
extends MiniPlugin {
    public static Recharge Instance;
    private HashSet<String> informSet = new HashSet();
    private HashMap<String, HashMap<String, RechargeData>> _recharge = new HashMap();

    private Recharge(JavaPlugin plugin) {
        super("Recharge", plugin);
    }

    public static void Initialize(JavaPlugin plugin) {
        Instance = new Recharge(plugin);
    }

    @EventHandler
    public void PlayerDeath(PlayerDeathEvent event) {
        this.Get(event.getEntity().getName()).clear();
    }

    public HashMap<String, RechargeData> Get(String name) {
        if (!this._recharge.containsKey(name)) {
            this._recharge.put(name, new HashMap());
        }
        return this._recharge.get(name);
    }

    public HashMap<String, RechargeData> Get(Player player) {
        return this.Get(player.getName());
    }

    @EventHandler
    public void update(UpdateEvent event) {
        if (event.getType() != UpdateType.TICK) {
            return;
        }
        this.recharge();
    }

    private void recharge() {
        for (Player cur : UtilServer.getPlayers()) {
            LinkedList<String> rechargeList = new LinkedList<String>();
            for (String ability : this.Get(cur).keySet()) {
                if (!this.Get(cur).get(ability).Update()) continue;
                rechargeList.add(ability);
            }
            for (String ability : rechargeList) {
                this.Get(cur).remove(ability);
                RechargedEvent rechargedEvent = new RechargedEvent(cur, ability);
                UtilServer.getServer().getPluginManager().callEvent((Event)rechargedEvent);
                if (!this.informSet.contains(ability)) continue;
                UtilPlayer.message((Entity)cur, F.main("Recharge", "You can use " + F.skill(ability) + "."));
            }
        }
    }

    public boolean use(Player player, String ability, long recharge, boolean inform, boolean attachItem) {
        return this.use(player, ability, ability, recharge, inform, attachItem);
    }

    public boolean use(Player player, String ability, String abilityFull, long recharge, boolean inform, boolean attachItem) {
        return this.use(player, ability, abilityFull, recharge, inform, attachItem, false);
    }

    public boolean use(Player player, String ability, long recharge, boolean inform, boolean attachItem, boolean attachDurability) {
        return this.use(player, ability, ability, recharge, inform, attachItem, attachDurability);
    }

    public boolean use(Player player, String ability, String abilityFull, long recharge, boolean inform, boolean attachItem, boolean attachDurability) {
        if (recharge == 0L) {
            return true;
        }
        this.recharge();
        if (inform && recharge > 1000L) {
            this.informSet.add(ability);
        }
        if (this.Get(player).containsKey(ability)) {
            if (inform) {
                UtilPlayer.message((Entity)player, F.main("Recharge", "You cannot use " + F.skill(abilityFull) + " for " + F.time(UtilTime.convertString(this.Get(player).get(ability).GetRemaining(), 1, UtilTime.TimeUnit.FIT)) + "."));
            }
            return false;
        }
        this.UseRecharge(player, ability, recharge, attachItem, attachDurability);
        return true;
    }

    public void useForce(Player player, String ability, long recharge) {
        this.useForce(player, ability, recharge, false);
    }

    private void useForce(Player player, String ability, long recharge, boolean attachItem) {
        this.UseRecharge(player, ability, recharge, attachItem, false);
    }

    public boolean usable(Player player, String ability) {
        return this.usable(player, ability, false);
    }

    private boolean usable(Player player, String ability, boolean inform) {
        if (!this.Get(player).containsKey(ability)) {
            return true;
        }
        if (this.Get(player).get(ability).GetRemaining() <= 0L) {
            return true;
        }
        if (inform) {
            UtilPlayer.message((Entity)player, F.main("Recharge", "You cannot use " + F.skill(ability) + " for " + F.time(UtilTime.convertString(this.Get(player).get(ability).GetRemaining(), 1, UtilTime.TimeUnit.FIT)) + "."));
        }
        return false;
    }

    private void UseRecharge(Player player, String ability, long recharge, boolean attachItem, boolean attachDurability) {
        RechargeEvent rechargeEvent = new RechargeEvent(player, ability, recharge);
        UtilServer.getServer().getPluginManager().callEvent((Event)rechargeEvent);
        this.Get(player).put(ability, new RechargeData(this, player, ability, player.getItemInHand(), rechargeEvent.GetRecharge(), attachItem, attachDurability));
    }

    public void recharge(Player player, String ability) {
        this.Get(player).remove(ability);
    }

    @EventHandler
    public void clearPlayer(PlayerQuitEvent event) {
        this._recharge.remove(event.getPlayer().getName());
    }

    public void setDisplayForce(Player player, String ability, boolean displayForce) {
        if (!this._recharge.containsKey(player.getName())) {
            return;
        }
        if (!this._recharge.get(player.getName()).containsKey(ability)) {
            return;
        }
        this._recharge.get((Object)player.getName()).get((Object)ability).DisplayForce = displayForce;
    }

    public void setCountdown(Player player, String ability, boolean countdown) {
        if (!this._recharge.containsKey(player.getName())) {
            return;
        }
        if (!this._recharge.get(player.getName()).containsKey(ability)) {
            return;
        }
        this._recharge.get((Object)player.getName()).get((Object)ability).Countdown = countdown;
    }

    public void Reset(Player player) {
        this._recharge.put(player.getName(), new HashMap());
    }

    public void Reset(Player player, String stringContains) {
        HashMap<String, RechargeData> data = this._recharge.get(player.getName());
        if (data == null) {
            return;
        }
        data.keySet().removeIf(key -> key.toLowerCase().contains(stringContains.toLowerCase()));
    }

    public void debug(Player player, String ability) {
        if (!this._recharge.containsKey(player.getName())) {
            player.sendMessage("No Recharge Map.");
            return;
        }
        if (!this._recharge.get(player.getName()).containsKey(ability)) {
            player.sendMessage("Ability Not Found.");
            return;
        }
        this._recharge.get(player.getName()).get(ability).debug(player);
    }
}
