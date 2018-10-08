/*
 * Decompiled with CFR 0_133.
 * 
 * Could not load the following classes:
 *  com.intellectualcrafters.plot.api.PlotAPI
 *  com.intellectualcrafters.plot.object.Plot
 *  net.milkbowl.vault.economy.Economy
 *  net.milkbowl.vault.economy.EconomyResponse
 *  org.apache.commons.lang.StringUtils
 *  org.bukkit.Bukkit
 *  org.bukkit.Material
 *  org.bukkit.OfflinePlayer
 *  org.bukkit.entity.Entity
 *  org.bukkit.entity.Player
 *  org.bukkit.event.inventory.ClickType
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.inventory.meta.ItemMeta
 */
package com.equestriworlds.barn.gui.barnlookup;

import com.equestriworlds.account.CoreClientManager;
import com.equestriworlds.barn.BarnManager;
import com.equestriworlds.barn.gui.barnlookup.BarnLookupMenu;
import com.equestriworlds.common.Rank;
import com.equestriworlds.itemstack.ItemBuilder;
import com.equestriworlds.itemstack.ItemStackFactory;
import com.equestriworlds.menu.item.IButton;
import com.equestriworlds.menu.page.MenuPageBase;
import com.equestriworlds.util.C;
import com.equestriworlds.util.F;
import com.equestriworlds.util.UtilPlayer;
import com.intellectualcrafters.plot.api.PlotAPI;
import com.intellectualcrafters.plot.object.Plot;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class BarnLookupPage
extends MenuPageBase<BarnManager, BarnLookupMenu> {
    private int _page;
    private Plot plot;
    private int total;
    private Economy eco;

    BarnLookupPage(BarnManager plugin, BarnLookupMenu shop, CoreClientManager clientManager, String name, Player player) {
        super(plugin, shop, clientManager, name, player);
        this.plot = ((BarnManager)this.getPlugin()).plot.getPlot(this.getPlayer());
        this.eco = ((BarnManager)this.getPlugin()).eco;
        this.buildPage();
    }

    @Override
    protected void buildPage() {
        this.total = 0;
        ArrayList<String> purchases = new ArrayList<String>();
        for (Plot plots : this.plot.getConnectedPlots()) {
            purchases.addAll(((BarnManager)this.getPlugin()).getFromConfig(plots.temp));
        }
        if (purchases.isEmpty()) {
            this.addItem(22, ItemStackFactory.Instance.CreateStack(Material.REDSTONE_BLOCK, (byte)0, 1, C.cRed + "No Purchases", new String[]{" ", C.cGray + "No purchases have been ", C.cGray + "Made for this plot.", C.cGray + "To purchase some builds", C.cGray + "For your barn, type", C.cYellow + "/barn shop"}));
            return;
        }
        boolean pages = this.addPages(purchases.size(), this::buildPage);
        for (int i = 0; i < (pages ? 45 : 45); ++i) {
            int itemSlot = this._page * 45 + i;
            if (itemSlot >= purchases.size()) {
                ItemStack item = this.getItem(i);
                if (item == null || item.getType() == Material.AIR) break;
                this.setItem(i, new ItemStack(Material.AIR));
                continue;
            }
            this.addButton(i, this.item((String)purchases.get(itemSlot)), (player, clickType) -> {
                if (Rank.Has(this.getPlayer(), Rank.ADMIN, false) && clickType == ClickType.CONTROL_DROP) {
                    String item = (String)purchases.get(itemSlot);
                    OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer((UUID)((UUID)this.plot.getOwners().toArray()[0]));
                    int value = this.getMoneyFromItem((String)purchases.get(itemSlot));
                    ((BarnManager)this.getPlugin()).removeFromList(this.plot.temp, item, player);
                    this.eco.depositPlayer(offlinePlayer, (double)value);
                    UtilPlayer.message((Entity)player, F.main("Barn", F.name(offlinePlayer.getName()) + " has had " + value + " added to their balance"));
                    this.refresh();
                }
            });
        }
        this.addButton(49, this.totalItem(this.total));
    }

    private ItemStack totalItem(int total) {
        ItemStack item = new ItemStack(Material.EMERALD, 1);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(C.convert("&a&lTotal Price"));
        ArrayList<String> lore = new ArrayList<String>();
        lore.add(C.convert("&2$&a" + total));
        meta.setLore(lore);
        item.setItemMeta(meta);
        return item;
    }

    private ItemStack item(String name) {
        String[] arrstring;
        int money = this.getMoneyFromItem(name);
        this.total += money;
        if (Rank.Has(this.getPlayer(), Rank.ADMIN, false)) {
            String[] arrstring2 = new String[3];
            arrstring2[0] = C.cDGreen + "$" + C.cGreen + money;
            arrstring2[1] = " ";
            arrstring = arrstring2;
            arrstring2[2] = C.cYellow + "Ctrl+Drop" + C.cGray + " to remove";
        } else {
            String[] arrstring3 = new String[1];
            arrstring = arrstring3;
            arrstring3[0] = C.cDGreen + "$" + C.cGreen + money;
        }
        return ItemStackFactory.Instance.CreateStack(Material.PAPER, (byte)0, 1, C.cGreen + C.Bold + name, arrstring);
    }

    private boolean addPages(int amount, Runnable runnable) {
        if (amount > 45) {
            if (this._page > 0) {
                this.addButton(45, new ItemBuilder(Material.STAINED_GLASS_PANE, this._page, (short)14).setTitle("To Page " + this._page).build(), (player, clickType) -> {
                    Iterator<Integer> itel = this.getButtonMap().keySet().iterator();
                    while (itel.hasNext()) {
                        int slot = itel.next();
                        if (slot <= 8) continue;
                        itel.remove();
                        this.setItem(slot, new ItemStack(Material.AIR));
                    }
                    --this._page;
                    runnable.run();
                });
            }
            if (amount > (this._page + 1) * 45) {
                this.addButton(53, new ItemBuilder(Material.STAINED_GLASS_PANE, this._page + 2, (short)14).setTitle("To Page " + (this._page + 2)).build(), (player, clickType) -> {
                    Iterator<Integer> itel = this.getButtonMap().keySet().iterator();
                    while (itel.hasNext()) {
                        int slot = itel.next();
                        if (slot <= 8) continue;
                        itel.remove();
                        this.setItem(slot, new ItemStack(Material.AIR));
                    }
                    ++this._page;
                    runnable.run();
                });
            }
            return true;
        }
        return false;
    }

    private int getMoneyFromItem(String name) {
        if (name.contains("Arena")) {
            return this.getArenaPrice(name);
        }
        if (name.contains("House")) {
            return this.getHousePrice(name);
        }
        if (name.contains("Wash")) {
            return 1000;
        }
        if (name.contains("Stall")) {
            return this.getStallPrice(name);
        }
        if (name.contains("Pasture")) {
            return this.getArenaPrice(name);
        }
        if (name.contains("Storage")) {
            return 5000;
        }
        if (name.contains("Track")) {
            return 45000;
        }
        if (name.contains("Pen")) {
            return 2000;
        }
        if (name.contains("Water")) {
            return 1500;
        }
        return 0;
    }

    private int getArenaPrice(String name) {
        int count = StringUtils.countMatches((String)name, (String)"x");
        if (count == 1) {
            String[] split = name.split(" ");
            String[] size = split[0].split("x");
            int length = Integer.parseInt(size[0]);
            int width = Integer.parseInt(size[1]);
            if (name.contains("Grass")) {
                return length * width * 9;
            }
            if (name.contains("White")) {
                return length * width * 12;
            }
            if (name.contains("Red")) {
                return length * width * 12;
            }
            if (name.contains("Clay")) {
                return length * width * 15;
            }
            if (name.contains("Concrete")) {
                return length * width * 16;
            }
            if (name.contains("Sand")) {
                return length * width * 10;
            }
            if (name.contains("Pasture")) {
                return length * width * 7;
            }
        } else if (count == 2) {
            String[] split = name.split(" ");
            String[] size = split[1].split("x");
            int length = Integer.parseInt(size[0]);
            int width = Integer.parseInt(size[1]);
            if (name.contains("Grass")) {
                return length * width * 9 * Integer.valueOf(split[0].substring(1));
            }
            if (name.contains("White")) {
                return length * width * 12 * Integer.valueOf(split[0].substring(1));
            }
            if (name.contains("Red")) {
                return length * width * 12 * Integer.valueOf(split[0].substring(1));
            }
            if (name.contains("Clay")) {
                return length * width * 15 * Integer.valueOf(split[0].substring(1));
            }
            if (name.contains("Concrete")) {
                return length * width * 16 * Integer.valueOf(split[0].substring(1));
            }
            if (name.contains("Sand")) {
                return length * width * 10 * Integer.valueOf(split[0].substring(1));
            }
            if (name.contains("Pasture")) {
                return length * width * 7 * Integer.valueOf(split[0].substring(1));
            }
        }
        return 0;
    }

    private int getHousePrice(String name) {
        if (name.contains("Small")) {
            return 50000;
        }
        if (name.contains("Medium")) {
            return 75000;
        }
        if (name.contains("Large")) {
            return 100000;
        }
        return 0;
    }

    private int getStallPrice(String name) {
        String[] split = name.split("x");
        return Integer.valueOf(split[1].replace(" ", "")) * 1200;
    }
}
