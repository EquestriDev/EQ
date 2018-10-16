package com.equestriworlds.recharge;

import com.equestriworlds.recharge.Recharge;
import com.equestriworlds.util.C;
import com.equestriworlds.util.UtilGear;
import com.equestriworlds.util.UtilTextBottom;
import com.equestriworlds.util.UtilTime;
import java.io.PrintStream;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 * Cooldowns
 */
public class RechargeData {
    public Recharge Host;
    public long Time;
    public long Recharge;
    public Player Player;
    public String Name;
    public ItemStack Item;
    public boolean DisplayForce = false;
    public boolean Countdown = false;
    public boolean AttachItem;
    public boolean AttachDurability;

    public RechargeData(Recharge host, Player player, String name, ItemStack stack, long rechargeTime, boolean attachitem, boolean attachDurability) {
        this.Host = host;
        this.Player = player;
        this.Name = name;
        this.Item = player.getItemInHand();
        this.Time = System.currentTimeMillis();
        this.Recharge = rechargeTime;
        this.AttachItem = attachitem;
        this.AttachDurability = attachDurability;
    }

    public boolean Update() {
        if ((this.DisplayForce || this.Item != null) && this.Name != null && this.Player != null) {
            double percent = (double)(System.currentTimeMillis() - this.Time) / (double)this.Recharge;
            if (this.DisplayForce || this.AttachItem) {
                try {
                    if (this.DisplayForce || this.Item != null && UtilGear.isMat(this.Player.getItemInHand(), this.Item.getType())) {
                        if (!UtilTime.elapsed(this.Time, this.Recharge)) {
                            UtilTextBottom.displayProgress(C.Bold + this.Name, percent, UtilTime.MakeStr(this.Recharge - (System.currentTimeMillis() - this.Time)), this.Countdown, this.Player);
                        } else {
                            if (!this.Countdown) {
                                UtilTextBottom.display(C.cGreen + C.Bold + this.Name + " Recharged", this.Player);
                            } else {
                                UtilTextBottom.display(C.cRed + C.Bold + this.Name + " Ended", this.Player);
                            }
                            if (this.Recharge > 4000L) {
                                this.Player.playSound(this.Player.getLocation(), Sound.BLOCK_NOTE_PLING, 0.4f, 3.0f);
                            }
                        }
                    }
                }
                catch (Exception e) {
                    System.out.println("Recharge Indicator Error!");
                    e.printStackTrace();
                }
            }
            if (this.AttachDurability && this.Item != null) {
                this.Item.setDurability((short)((double)this.Item.getType().getMaxDurability() - (double)this.Item.getType().getMaxDurability() * percent));
            }
        }
        return UtilTime.elapsed(this.Time, this.Recharge);
    }

    public long GetRemaining() {
        return this.Recharge - (System.currentTimeMillis() - this.Time);
    }

    public void debug(Player player) {
        player.sendMessage("Recharge: " + this.Recharge);
        player.sendMessage("Time: " + this.Time);
        player.sendMessage("Elapsed: " + (System.currentTimeMillis() - this.Time));
        player.sendMessage("Remaining: " + this.GetRemaining());
    }
}
