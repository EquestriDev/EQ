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
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

public class BarnShopStallsPage
extends MenuPageBase<BarnManager, BarnShopMenu> {
    private MenuPageBase<BarnManager, BarnShopMenu> m;
    private Plot plot;
    private int amount;
    private Material material;
    private byte data;
    private int cost;

    BarnShopStallsPage(BarnManager plugin, BarnShopMenu shop, MenuPageBase<BarnManager, BarnShopMenu> m, CoreClientManager clientManager, String name, Player player, Material material, byte data, int cost, int size) {
        super(plugin, shop, clientManager, name, player, size);
        this.plot = ((BarnManager)this.getPlugin()).plot.getPlot(this.getPlayer());
        this.amount = 1;
        this.m = m;
        this.material = material;
        this.data = data;
        this.cost = cost;
        this.buildPage();
    }

    @Override
    protected void buildPage() {
        this.addButton(0, ItemStackFactory.Instance.CreateStack(Material.BED, (byte)14, 1, C.cRed + C.Bold + "<- Go Back"), (player, clickType) -> ((BarnShopMenu)this.getShop()).openPageForPlayer(this.getPlayer(), this.m));
        this.buildA();
        this.addConfirmation(((BarnManager)this.getPlugin()).eco, 8, this.material, this.data, this.amount, (this.amount == 1 ? "" : new StringBuilder().append("x").append(this.amount).append(" ").toString()) + this.n(), new String[]{"Click to purchase"}, this.cost * this.amount, () -> {
            ((BarnManager)this.getPlugin()).addToConfig(this.plot.temp, "Stall x" + this.amount);
            UtilPlayer.message((Entity)this.getPlayer(), F.main("Shop", new StringBuilder().append("You have successfully purchased: ").append(F.elem(new StringBuilder().append(this.amount == 1 ? "" : new StringBuilder().append("x").append(this.amount).append(" ").toString()).append(this.n()).toString())).toString()) + "!");
        }, this.m);
    }

    private String n() {
        return this.amount == 1 ? "Stall" : "Stalls";
    }

    private void buildA() {
        ArrayList<Integer> slots = new ItemLayout("xxxoooxxx").getItemSlots();
        this.a(slots.get(0), -1);
        this.addItem(slots.get(1), ItemStackFactory.Instance.CreateStack(Material.FENCE_GATE, (byte)0, this.amount, C.cYellow + "Amount: " + C.cWhite + this.amount));
        this.a(slots.get(2), 1);
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
                if (BarnShopStallsPage.this.amount + amount > 0) {
                    BarnShopStallsPage barnShopStallsPage = BarnShopStallsPage.this;
                    barnShopStallsPage.amount = barnShopStallsPage.amount + amount;
                } else {
                    BarnShopStallsPage.this.amount = 1;
                }
                BarnShopStallsPage.this.refresh();
            }
        });
    }

}
