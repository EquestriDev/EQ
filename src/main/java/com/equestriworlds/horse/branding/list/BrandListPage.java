package com.equestriworlds.horse.branding.list;

import com.equestriworlds.account.CoreClientManager;
import com.equestriworlds.horse.HorseManager;
import com.equestriworlds.horse.branding.Brand;
import com.equestriworlds.horse.branding.BrandConfig;
import com.equestriworlds.horse.branding.BrandToken;
import com.equestriworlds.horse.branding.list.BrandListMenu;
import com.equestriworlds.horse.branding.list.BrandListSorter;
import com.equestriworlds.horse.breeding.breedingStages;
import com.equestriworlds.horse.config.CustomHorse;
import com.equestriworlds.horse.config.CustomHorseToken;
import com.equestriworlds.horse.config.Gender;
import com.equestriworlds.horse.config.HorseConfig;
import com.equestriworlds.horse.gui.list.HorseListSorter;
import com.equestriworlds.itemstack.ItemBuilder;
import com.equestriworlds.itemstack.ItemLayout;
import com.equestriworlds.itemstack.ItemStackFactory;
import com.equestriworlds.menu.item.IButton;
import com.equestriworlds.menu.page.MenuPageBase;
import com.equestriworlds.util.C;
import com.equestriworlds.util.F;
import com.equestriworlds.util.UtilMath;
import com.equestriworlds.util.UtilServer;
import com.equestriworlds.util.UtilTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftAbstractHorse;
import org.bukkit.entity.Horse;
import org.bukkit.entity.Llama;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class BrandListPage
extends MenuPageBase<HorseManager, BrandListMenu> {
    private int _page;
    private boolean listHorses = false;
    private Brand displayBrand;

    BrandListPage(HorseManager plugin, BrandListMenu shop, CoreClientManager clientManager, Player player) {
        super(plugin, shop, clientManager, "Brands", player);
        this.buildPage();
    }

    private boolean addPages(int amount, Runnable runnable) {
        if (amount > 27) {
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
            if (amount > (this._page + 1) * 27) {
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

    @Override
    protected void buildPage() {
        ArrayList<Brand> brands = new ArrayList<Brand>(((HorseManager)this.getPlugin()).brandConfig.brands.values());
        List<CustomHorse> horses = null;
        if (this.displayBrand != null) {
            horses = ((HorseManager)this.getPlugin()).config.getHorsesByBrand(this.displayBrand.id);
        }
        this.buildTop();
        if (horses != null) {
            boolean pages = this.addPages(horses.size(), this::buildPage);
            horses.sort(new HorseListSorter());
            for (int i = 0; i < (pages ? 27 : 27); ++i) {
                int itemSlot = this._page * 27 + i;
                int slot = i + 18;
                if (itemSlot >= horses.size()) {
                    ItemStack item = this.getItem(slot);
                    if (item == null || item.getType() == Material.AIR) break;
                    this.setItem(slot, new ItemStack(Material.AIR));
                    continue;
                }
                this.addButton(slot, this.horseItem(horses.get(itemSlot)));
            }
            this.addButton(49, this.returnItem(), this.buttonReturn());
        } else {
            boolean pages = this.addPages(brands.size(), this::buildPage);
            brands.sort(new BrandListSorter());
            for (int i = 0; i < (pages ? 27 : 27); ++i) {
                int itemSlot = this._page * 27 + i;
                int slot = i + 18;
                if (itemSlot >= brands.size()) {
                    ItemStack item = this.getItem(slot);
                    if (item == null || item.getType() == Material.AIR) break;
                    this.setItem(slot, new ItemStack(Material.AIR));
                }
                this.addButton(slot, this.horseItem(brands.get(itemSlot)), this.button(brands.get(itemSlot)));
            }
        }
    }

    private void buildTop() {
        ArrayList<Integer> layout = new ItemLayout("xxxxoxxxx").getItemSlots();
        Brand brand = ((HorseManager)this.getPlugin()).brandConfig.getBrandByID(this.getPlayer().getUniqueId());
        if (brand == null) {
            this.addItem(layout.get(0), ItemStackFactory.Instance.CreateStack(Material.BOOK, (byte)0, 1, C.cAqua + C.Bold + "Information:", new String[]{C.cDAqua + "You don't have a registered brand! ", "", C.cDAqua + "Total Brands: " + C.cWhite + ((HorseManager)this.getPlugin()).brandConfig.brands.size()}));
            return;
        }
        List<UUID> coowners = brand.token.coowners;
        ArrayList<String> coownersLore = new ArrayList<String>();
        coownersLore.add(C.cDAqua + "Format: " + C.cWhite + C.convert(brand.token.format));
        coownersLore.add(C.cDAqua + "Horse Amount: " + C.cWhite + String.valueOf(((HorseManager)this.getPlugin()).config.getHorseAmountByBrand(brand.id)));
        coownersLore.add(coowners.size() != 0 ? C.cDAqua + "Co-Owners:" : "");
        for (UUID uuid : coowners) {
            OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer((UUID)uuid);
            coownersLore.add("&7- &3" + offlinePlayer.getName());
        }
        if (coowners.size() != 0) {
            coownersLore.add("");
        }
        coownersLore.add(C.cDAqua + "Total Brands: " + C.cWhite + ((HorseManager)this.getPlugin()).brandConfig.brands.size());
        this.addItem(layout.get(0), ItemStackFactory.Instance.CreateStack(Material.BOOK, (byte)0, 1, C.cAqua + C.Bold + "Information:", coownersLore));
    }

    private ItemStack horseItem(Brand brand) {
        ItemStack item = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short)3);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(C.convert(brand.token.format));
        OfflinePlayer player = Bukkit.getOfflinePlayer((UUID)brand.id);
        ArrayList<String> lore = new ArrayList<String>();
        lore.add(F.desc("Owner", player.getName()));
        lore.add(F.desc("Horse Amount", String.valueOf(((HorseManager)this.getPlugin()).config.getHorseAmountByBrand(brand.id))));
        meta.setLore(lore);
        item.setItemMeta(meta);
        return item;
    }

    private ItemStack returnItem() {
        ItemStack item = new ItemStack(Material.BARRIER, 1);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(C.convert("&4&lReturn"));
        ArrayList<String> lore = new ArrayList<String>();
        lore.add(C.convert("&cClick to return to brands!"));
        meta.setLore(lore);
        item.setItemMeta(meta);
        return item;
    }

    private ItemStack horseItem(CustomHorse horse) {
        ItemStack item = new ItemStack(Material.INK_SACK, 1, this.icon(horse));
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(C.cAqua + horse.token.name);
        ArrayList<String> lore = new ArrayList<String>();
        lore.add(" ");
        if (horse.token.free) {
            lore.add(F.desc("Owner", "This horse is free"));
        } else if (horse.token.trusted.contains(this.getPlayer().getUniqueId()) && !horse.token.owner.equals(this.getPlayer().getUniqueId())) {
            lore.add(F.desc("Owner", UtilServer.getServer().getOfflinePlayer(horse.token.owner).getName()));
        } else if (!horse.token.owner.equals(this.getPlayer().getUniqueId())) {
            lore.add(F.desc("Owner", UtilServer.getServer().getOfflinePlayer(horse.token.owner).getName()));
        }
        lore.add(F.desc("ID", horse.id + ""));
        lore.add(F.desc("Type", horse.token.variant.toString()));
        lore.add(F.desc("Gender", horse.token.gender.name));
        if (horse.token.age != null) {
            lore.add(F.desc("Age", UtilTime.formatDateDiff(horse.token.age, false)));
        }
        lore.add(" ");
        lore.add(F.desc("Speed", UtilMath.trim(3, horse.token.speed) + ""));
        lore.add(F.desc("Jump", UtilMath.trim(3, horse.token.jump) + ""));
        lore.add(" ");
        if (horse.horse instanceof Horse) {
            lore.add(F.desc("Color", horse.token.color.toString()));
            lore.add(F.desc("Style", horse.token.style.toString()));
        }
        if (horse.horse instanceof Llama) {
            lore.add(F.desc("Color", horse.token.llamaColor.toString()));
        }
        lore.add(F.desc("Adult", F.yn(horse.token.adult)));
        if (horse.token.stage != null && !horse.token.stage.equals((Object)breedingStages.NONE)) {
            lore.add(F.desc(horse.token.stage.name, UtilTime.formatDateDiff(horse.token.breedingTime, true)));
        }
        meta.setLore(lore);
        item.setItemMeta(meta);
        return item;
    }

    private IButton button(Brand brand) {
        return (player, clickType) -> {
            this.listHorses = !this.listHorses;
            this.displayBrand = brand;
            this._page = 0;
            this.refresh();
        };
    }

    private IButton buttonReturn() {
        return (player, clickType) -> {
            this.listHorses = !this.listHorses;
            this.displayBrand = null;
            this._page = 0;
            this.refresh();
        };
    }

    private short icon(CustomHorse horse) {
        if (horse.token.gender.equals((Object)Gender.MARE)) {
            return 9;
        }
        if (horse.token.gender.equals((Object)Gender.STALLION)) {
            return 12;
        }
        return 2;
    }
}
