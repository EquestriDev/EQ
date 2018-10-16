package com.equestriworlds.barn.gui.barnshop;

import com.equestriworlds.account.CoreClientManager;
import com.equestriworlds.barn.BarnManager;
import com.equestriworlds.barn.gui.barnshop.BarnShopArenasPage;
import com.equestriworlds.barn.gui.barnshop.BarnShopMenu;
import com.equestriworlds.barn.gui.barnshop.BarnShopStallsPage;
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
import com.intellectualcrafters.plot.object.PlotArea;
import com.intellectualcrafters.plot.object.PlotId;
import com.intellectualcrafters.plot.object.PlotPlayer;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

/**
 * /barn shop
 * Chest menu to buy barn supplies.
 */
public class BarnShopPage
extends MenuPageBase<BarnManager, BarnShopMenu> {
    private Plot plot;
    private BarnShopPageType menuType;
    private Economy eco;

    BarnShopPage(BarnManager plugin, BarnShopMenu shop, CoreClientManager clientManager, String name, Player player) {
        super(plugin, shop, clientManager, name, player);
        this.plot = ((BarnManager)this.getPlugin()).plot.getPlot(this.getPlayer());
        this.menuType = BarnShopPageType.Stalls;
        this.eco = ((BarnManager)this.getPlugin()).eco;
        this.buildPage();
    }

    @Override
    protected void buildPage() {
        this.addConfirmation(this.eco, 13, Material.GRASS, 1, "Plot", new String[]{"Purchase a plot to build", "Your dream barn and store", "All your beautiful horses!"}, 60000, () -> {
            PlotPlayer pplayer = PlotPlayer.wrap((Object)this.getPlayer());
            PlotArea plotArea = pplayer.getApplicablePlotArea();
            Plot freePlot = plotArea.getNextFreePlot(pplayer, null);
            ((BarnManager)this.getPlugin()).runSyncLater(() -> UtilPlayer.message((Entity)this.getPlayer(), F.main("Barn", "You have claimed this plot for your barn. Enjoy!")), 1L);
            freePlot.claim(pplayer, true, null);
        });
        if (this.plot != null && (this.plot.getOwners().contains(this.getPlayer().getUniqueId()) || this.plot.getTrusted().contains(this.getPlayer().getUniqueId()))) {
            this.buildSectionChoice();
            if (this.menuType == BarnShopPageType.Stalls) {
                this.buildStalls();
            } else if (this.menuType == BarnShopPageType.Arenas) {
                this.buildArenas();
            } else if (this.menuType == BarnShopPageType.Addons) {
                this.buildAddons();
            } else if (this.menuType == BarnShopPageType.Houses) {
                this.buildHouses();
            }
        } else {
            this.addItem(40, ItemStackFactory.Instance.CreateStack(Material.REDSTONE_BLOCK, (byte)0, 1, C.cRed + C.Bold + "Get on your plot!", new String[]{"", C.cWhite + "You need to be on your plot", C.cWhite + "to purchase the barn add-ons."}));
        }
    }

    private void buildSectionChoice() {
        this.addButton(1, ItemStackFactory.Instance.CreateStack(Material.FENCE, (byte)0, 1, (this.menuType == BarnShopPageType.Stalls ? new StringBuilder().append(C.cGreen).append(C.Bold).toString() : C.cYellow) + "Stalls"), (player, clickType) -> {
            this.menuType = BarnShopPageType.Stalls;
            this.refresh();
            this.addGlow(1);
        });
        this.addButton(3, ItemStackFactory.Instance.CreateStack(Material.SAND, (byte)0, 1, (this.menuType == BarnShopPageType.Arenas ? new StringBuilder().append(C.cGreen).append(C.Bold).toString() : C.cYellow) + "Arenas"), (player, clickType) -> {
            this.menuType = BarnShopPageType.Arenas;
            this.refresh();
            this.addGlow(3);
        });
        this.addButton(5, ItemStackFactory.Instance.CreateStack(Material.CARPET, (byte)12, 1, (this.menuType == BarnShopPageType.Addons ? new StringBuilder().append(C.cGreen).append(C.Bold).toString() : C.cYellow) + "Add-Ons"), (player, clickType) -> {
            this.menuType = BarnShopPageType.Addons;
            this.refresh();
            this.addGlow(5);
        });
        this.addButton(7, ItemStackFactory.Instance.CreateStack(Material.DARK_OAK_DOOR_ITEM, (byte)0, 1, (this.menuType == BarnShopPageType.Houses ? new StringBuilder().append(C.cGreen).append(C.Bold).toString() : C.cYellow) + "Houses"), (player, clickType) -> {
            this.menuType = BarnShopPageType.Houses;
            this.refresh();
            this.addGlow(7);
        });
    }

    private void buildStalls() {
        ArrayList<Integer> slots = new ItemLayout("xxxxxxxxx", "xxxxxxxxx", "xxxxxxxxx", "xxxoxoxxx", "xxxxxxxxx", "xxoxoxoxo").getItemSlots();
        this.stall(slots.get(0), 1);
        this.stall(slots.get(1), 2);
        this.stall(slots.get(2), 5);
        this.stall(slots.get(3), 10);
        this.stall(slots.get(4), 20);
        this.stall(slots.get(5));
    }

    private void buildArenas() {
        ArrayList<Integer> slots = new ItemLayout("xxxxxxxxx", "xxxxxxxxx", "xxxxxxxxx", "xxoxoxoxx", "xxxxxxxxx", "xoxoxoxox").getItemSlots();
        BarnShopPage m = this;
        this.addButton(slots.get(0), ItemStackFactory.Instance.CreateStack(Material.STONE, (byte)1, 1, C.cYellow + "Pasture", new String[]{" ", C.cGray + "Create a Pasture for your horses to graze!", " ", F.elem("1 Square Meter") + " costs " + F.elem("$7")}), (player, clickType) -> ((BarnShopMenu)this.getShop()).openPageForPlayer(this.getPlayer(), new BarnShopArenasPage((BarnManager)this.getPlugin(), (BarnShopMenu)this.getShop(), m, this.getClientManager(), "Pasture", player, Material.STONE, (byte)1, 7)));
        this.arena(slots.get(1), Material.GRASS, (byte)0, "Grass", 9);
        this.arena(slots.get(2), Material.SAND, (byte)0, "Sand", 10);
        this.arena(slots.get(3), Material.HUGE_MUSHROOM_1, (byte)0, "White Sand", 12);
        this.arena(slots.get(4), Material.SAND, (byte)1, "Red Sand", 12);
        this.arena(slots.get(5), Material.CLAY, (byte)0, "Clay", 15);
        this.arena(slots.get(6), Material.CONCRETE_POWDER, (byte)1, "Concrete Powder", 16);
    }

    private void buildAddons() {
        ArrayList<Integer> slots = new ItemLayout("xxxxxxxxx", "xxxxxxxxx", "xxxxxxxxx", "xoxoxoxox", "xxxxoxxxx").getItemSlots();
        this.addon(slots.get(0), Material.GOLD_BARDING, "Race Track", new String[]{"Purchase a Race Track to race on!", " ", C.cRed + "MUST HAVE A REGULAR SAND ARENA!"}, 45000);
        this.addon(slots.get(1), Material.SNOW_BALL, "Round Pen", new String[]{"Exercise your horse in a Round Pen!", " ", C.cRed + "MUST HAVE AN ARENA!"}, 2000);
        this.addon(slots.get(2), Material.WATER_BUCKET, "Wash Stall", new String[]{"Wash your horse in a new Wash Stall!"}, 1000);
        this.addon(slots.get(3), Material.CHEST, "Storage Room", new String[]{"Create a space to store anything in!"}, 5000);
        this.addon(slots.get(4), Material.WATER_BUCKET, "Water Source", new String[]{"Create a water source your horse can drink from"}, 1500);
    }

    private void buildHouses() {
        ArrayList<Integer> slots = new ItemLayout("xxxxxxxxx", "xxxxxxxxx", "xxxxxxxxx", "xxoxoxoxx").getItemSlots();
        this.house(slots.get(0), Material.WOOD_DOOR, "Small", 50000);
        this.house(slots.get(1), Material.BIRCH_DOOR_ITEM, "Medium", 75000);
        this.house(slots.get(2), Material.DARK_OAK_DOOR_ITEM, "Large", 100000);
    }

    private void stall(int slot, int amount) {
        this.addConfirmation(this.eco, slot, Material.FENCE_GATE, amount, amount + (amount == 1 ? " Stall" : " Stalls"), new String[]{"Purchase stalls for your horses!"}, amount * 1200, () -> {
            ((BarnManager)this.getPlugin()).addToConfig(this.plot.temp, "Stall x" + amount);
            UtilPlayer.message((Entity)this.getPlayer(), F.main("Shop", "You have successfully purchased: " + F.elem(new StringBuilder().append(amount).append(amount == 1 ? " Stall" : " Stalls").toString()) + "!"));
        });
    }

    private void stall(int slot) {
        BarnShopPage m = this;
        this.addButton(slot, ItemStackFactory.Instance.CreateStack(Material.FENCE_GATE, (byte)0, 64, C.cYellow + "Custom Stalls", new String[]{" ", C.cGray + "Purchase stalls for your horses!"}), (player, clickType) -> ((BarnShopMenu)this.getShop()).openPageForPlayer(this.getPlayer(), new BarnShopStallsPage((BarnManager)this.getPlugin(), (BarnShopMenu)this.getShop(), m, this.getClientManager(), "Custom Stalls", player, Material.FENCE_GATE, (byte)0, 1200, 9)));
    }

    private void arena(int slot, Material material, byte data, String name, int pricepersquaredmeter) {
        BarnShopPage m = this;
        this.addButton(slot, ItemStackFactory.Instance.CreateStack(material, data, 1, C.cYellow + name + " Arena", new String[]{" ", C.cGray + "Create a " + name + " Arena to ride in!", " ", F.elem("1 Square Meter") + " costs " + F.elem(new StringBuilder().append("$").append(pricepersquaredmeter).toString())}), (player, clickType) -> ((BarnShopMenu)this.getShop()).openPageForPlayer(this.getPlayer(), new BarnShopArenasPage((BarnManager)this.getPlugin(), (BarnShopMenu)this.getShop(), m, this.getClientManager(), name, player, material, data, pricepersquaredmeter)));
    }

    private void addon(int slot, Material material, String name, String[] desc, int price) {
        this.addConfirmation(this.eco, slot, material, 1, name, desc, price, () -> ((BarnManager)this.getPlugin()).addToConfig(this.plot.temp, name));
    }

    private void house(int slot, Material material, String size, int price) {
        this.addConfirmation(this.eco, slot, material, 1, size + " House", new String[]{"Build your own " + size + " House!"}, price, () -> {
            ((BarnManager)this.getPlugin()).addToConfig(this.plot.temp, size + " House");
            UtilPlayer.message((Entity)this.getPlayer(), F.main("Shop", new StringBuilder().append("You have successfully purchased: ").append(F.elem(new StringBuilder().append(size).append(" House").toString())).toString()) + "!");
        });
    }

    public static enum BarnShopPageType {
        Stalls,
        Arenas,
        Addons,
        Houses;
        

        private BarnShopPageType() {
        }
    }

}
