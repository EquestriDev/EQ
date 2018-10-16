package com.equestriworlds.barn.gui.barnshop;

import com.equestriworlds.account.CoreClientManager;
import com.equestriworlds.barn.BarnManager;
import com.equestriworlds.barn.gui.barnshop.BarnShopMenu;
import com.equestriworlds.itemstack.ItemLayout;
import com.equestriworlds.itemstack.ItemStackFactory;
import com.equestriworlds.menu.MenuBase;
import com.equestriworlds.menu.item.IButton;
import com.equestriworlds.menu.page.MenuPageBase;
import com.equestriworlds.util.C;
import com.equestriworlds.util.F;
import com.equestriworlds.util.UtilPlayer;
import com.intellectualcrafters.plot.api.PlotAPI;
import com.intellectualcrafters.plot.object.Plot;
import java.util.ArrayList;
import java.util.Objects;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

public class BarnShopArenasPage
extends MenuPageBase<BarnManager, BarnShopMenu> {
    private MenuPageBase<BarnManager, BarnShopMenu> m;
    private Plot plot;
    private int amount;
    private int l;
    private int w;
    private Material material;
    private byte data;
    private String name;
    private int cost;

    BarnShopArenasPage(BarnManager plugin, BarnShopMenu shop, MenuPageBase<BarnManager, BarnShopMenu> m, CoreClientManager clientManager, String name, Player player, Material material, byte data, int cost) {
        super(plugin, shop, clientManager, "Arena: " + name, player);
        this.plot = ((BarnManager)this.getPlugin()).plot.getPlot(this.getPlayer());
        this.amount = 1;
        this.l = 1;
        this.w = 1;
        this.m = m;
        this.material = material;
        this.data = data;
        this.name = name;
        this.cost = cost;
        this.buildPage();
    }

    @Override
    protected void buildPage() {
        this.addButton(0, ItemStackFactory.Instance.CreateStack(Material.BED, (byte)14, 1, C.cRed + C.Bold + "<- Go Back"), (player, clickType) -> ((BarnShopMenu)this.getShop()).openPageForPlayer(this.getPlayer(), this.m));
        this.buildA();
        this.buildL();
        this.buildW();
        this.addConfirmation(((BarnManager)this.getPlugin()).eco, 8, this.material, this.data, this.amount, (this.amount == 1 ? "" : new StringBuilder().append("x").append(this.amount).append(" ").toString()) + this.l + "x" + this.w + " " + this.n(), new String[]{"Create a " + this.n() + " to ride in!"}, this.l * this.w * this.cost * this.amount, () -> {
            ((BarnManager)this.getPlugin()).addToConfig(this.plot.temp, (this.amount == 1 ? "" : new StringBuilder().append("x").append(this.amount).append(" ").toString()) + this.l + "x" + this.w + " " + this.n());
            UtilPlayer.message((Entity)this.getPlayer(), F.main("Shop", new StringBuilder().append("You have successfully purchased: ").append(F.elem(new StringBuilder().append(this.amount == 1 ? "" : new StringBuilder().append("x").append(this.amount).append(" ").toString()).append(this.l).append("x").append(this.w).append(" ").append(this.n()).toString())).toString()) + "!");
        }, this.m);
    }

    private String n() {
        return Objects.equals(this.name, "Pasture") ? this.name : this.name + " Arena";
    }

    private void buildA() {
        ArrayList<Integer> slots = new ItemLayout("xxxoooxxx").getItemSlots();
        this.a(slots.get(0), -1);
        this.addItem(slots.get(1), ItemStackFactory.Instance.CreateStack(Material.REDSTONE_TORCH_ON, (byte)0, this.amount, C.cYellow + "Amount: " + C.cWhite + this.amount));
        this.a(slots.get(2), 1);
    }

    private void buildL() {
        ArrayList<Integer> slots = new ItemLayout("xxxxxxxxx", "xxoxxxxxx", "xoxoxxxxx", "xoxoxxxxx", "xoxoxxxxx", "xoxoxxxxx").getItemSlots();
        this.addItem(slots.get(0), ItemStackFactory.Instance.CreateStack(Material.INK_SACK, (byte)13, this.l, C.cYellow + "Length: " + C.cWhite + this.l));
        this.l(slots.get(1), -1);
        this.l(slots.get(2), 1);
        this.l(slots.get(3), -2);
        this.l(slots.get(4), 2);
        this.l(slots.get(5), -5);
        this.l(slots.get(6), 5);
        this.l(slots.get(7), -10);
        this.l(slots.get(8), 10);
    }

    private void buildW() {
        ArrayList<Integer> slots = new ItemLayout("xxxxxxxxx", "xxxxxxoxx", "xxxxxoxox", "xxxxxoxox", "xxxxxoxox", "xxxxxoxox").getItemSlots();
        this.addItem(slots.get(0), ItemStackFactory.Instance.CreateStack(Material.INK_SACK, (byte)12, this.w, C.cYellow + "Width: " + C.cWhite + this.w));
        this.w(slots.get(1), -1);
        this.w(slots.get(2), 1);
        this.w(slots.get(3), -2);
        this.w(slots.get(4), 2);
        this.w(slots.get(5), -5);
        this.w(slots.get(6), 5);
        this.w(slots.get(7), -10);
        this.w(slots.get(8), 10);
    }

    private void a(int slot, final int amount) {
        String[] arrstring = new String[3];
        arrstring[0] = "";
        arrstring[1] = C.cYellow + "Current Value: " + C.cWhite + this.amount;
        arrstring[2] = C.cYellow + "Post-Change Value: " + C.cWhite + (this.amount + amount < 1 ? 1 : this.amount + amount);
        this.addButton(slot, ItemStackFactory.Instance.CreateStack(Material.INK_SACK, (byte)(amount > 0 ? 10 : 1), Math.abs(amount), (amount > 0 ? new StringBuilder().append(C.cGreen).append(C.Bold).append("Add ").toString() : new StringBuilder().append(C.cRed).append(C.Bold).append("Remove ").toString()) + Math.abs(amount), arrstring), new IButton(){

            @Override
            public void onClick(Player player, ClickType clickType) {
                if (clickType == ClickType.DOUBLE_CLICK) {
                    return;
                }
                if (BarnShopArenasPage.this.amount + amount > 0) {
                    BarnShopArenasPage barnShopArenasPage = BarnShopArenasPage.this;
                    barnShopArenasPage.amount = barnShopArenasPage.amount + amount;
                } else {
                    BarnShopArenasPage.this.amount = 1;
                }
                BarnShopArenasPage.this.refresh();
            }
        });
    }

    private void l(int slot, final int amount) {
        String[] arrstring = new String[3];
        arrstring[0] = "";
        arrstring[1] = C.cYellow + "Current Value: " + C.cWhite + this.l;
        arrstring[2] = C.cYellow + "Post-Change Value: " + C.cWhite + (this.l + amount < 1 ? 1 : this.l + amount);
        this.addButton(slot, ItemStackFactory.Instance.CreateStack(Material.INK_SACK, (byte)(amount > 0 ? 10 : 1), Math.abs(amount), (amount > 0 ? new StringBuilder().append(C.cGreen).append(C.Bold).append("Add ").toString() : new StringBuilder().append(C.cRed).append(C.Bold).append("Remove ").toString()) + Math.abs(amount) + (amount > 0 ? " to" : " from") + " length", arrstring), new IButton(){

            @Override
            public void onClick(Player player, ClickType clickType) {
                if (clickType == ClickType.DOUBLE_CLICK) {
                    return;
                }
                if (BarnShopArenasPage.this.l + amount > 0) {
                    BarnShopArenasPage.this.l = BarnShopArenasPage.this.l + amount;
                } else {
                    BarnShopArenasPage.this.l = 1;
                }
                BarnShopArenasPage.this.refresh();
            }
        });
    }

    private void w(int slot, final int amount) {
        String[] arrstring = new String[3];
        arrstring[0] = "";
        arrstring[1] = C.cYellow + "Current Value: " + C.cWhite + this.w;
        arrstring[2] = C.cYellow + "Post-Change Value: " + C.cWhite + (this.w + amount < 1 ? 1 : this.w + amount);
        this.addButton(slot, ItemStackFactory.Instance.CreateStack(Material.INK_SACK, (byte)(amount > 0 ? 10 : 1), Math.abs(amount), (amount > 0 ? new StringBuilder().append(C.cGreen).append(C.Bold).append("Add ").toString() : new StringBuilder().append(C.cRed).append(C.Bold).append("Remove ").toString()) + Math.abs(amount) + (amount > 0 ? " to" : " from") + " width", arrstring), new IButton(){

            @Override
            public void onClick(Player player, ClickType clickType) {
                if (clickType == ClickType.DOUBLE_CLICK) {
                    return;
                }
                if (BarnShopArenasPage.this.w + amount > 0) {
                    BarnShopArenasPage.this.w = BarnShopArenasPage.this.w + amount;
                } else {
                    BarnShopArenasPage.this.w = 1;
                }
                BarnShopArenasPage.this.refresh();
            }
        });
    }

}
