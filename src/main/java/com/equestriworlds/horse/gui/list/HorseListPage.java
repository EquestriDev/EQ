package com.equestriworlds.horse.gui.list;

import com.equestriworlds.account.CoreClientManager;
import com.equestriworlds.common.Rank;
import com.equestriworlds.horse.HorseManager;
import com.equestriworlds.horse.breeding.breedingStages;
import com.equestriworlds.horse.config.CustomHorse;
import com.equestriworlds.horse.config.CustomHorseToken;
import com.equestriworlds.horse.config.Gender;
import com.equestriworlds.horse.config.HorseConfig;
import com.equestriworlds.horse.gui.list.HorseListMenu;
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
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftAbstractHorse;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Horse;
import org.bukkit.entity.Llama;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class HorseListPage
extends MenuPageBase<HorseManager, HorseListMenu> {
    private int _page;
    private boolean all = false;
    private boolean trusted = false;
    private String playerView;

    HorseListPage(HorseManager plugin, HorseListMenu shop, CoreClientManager clientManager, Player player, String playerView) {
        super(plugin, shop, clientManager, "Horses", player);
        this.playerView = playerView;
        this.buildPage();
    }

    private boolean addPages(int amount, Runnable runnable) {
        if (amount > 36) {
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
        List<CustomHorse> horses = !this.trusted ? (this.all ? new ArrayList<CustomHorse>(((HorseManager)this.getPlugin()).config.horses.values()) : ((HorseManager)this.getPlugin()).config.getPlayerHorses(this.playerView)) : (this.trusted ? new ArrayList<CustomHorse>(((HorseManager)this.getPlugin()).config.getPlayerTrusted(this.playerView)) : ((HorseManager)this.getPlugin()).config.getPlayerHorses(this.playerView));
        this.buildTop();
        boolean pages = this.addPages(horses.size(), this::buildPage);
        horses.sort(new HorseListSorter());
        for (int i = 0; i < (pages ? 27 : 36); ++i) {
            int itemSlot = this._page * 27 + i;
            int slot = i + 18;
            if (itemSlot >= horses.size()) {
                ItemStack item = this.getItem(slot);
                if (item == null || item.getType() == Material.AIR) break;
                this.setItem(slot, new ItemStack(Material.AIR));
                continue;
            }
            this.addButton(slot, this.horseItem(horses.get(itemSlot)), this.button(horses.get(itemSlot)));
        }
    }

    private void buildTop() {
        String[] arrstring = new String[1];
        arrstring[0] = Rank.Has(this.getPlayer(), Rank.JRMOD, false) ? "xxxoxoxxo" : "xxxxoxxxo";
        ArrayList<Integer> layout = new ItemLayout(arrstring).getItemSlots();
        String[] arrstring2 = new String[7];
        arrstring2[0] = C.cDAqua + (this.getPlayer().getName().equals(this.playerView) ? "Your" : new StringBuilder().append(this.playerView).append("'s").toString()) + " Horses: " + C.cWhite + ((HorseManager)this.getPlugin()).config.getPlayerHorses(this.playerView).size();
        arrstring2[1] = C.cDAqua + (this.getPlayer().getName().equals(this.playerView) ? "Your" : new StringBuilder().append(this.playerView).append("'s").toString()) + " Mares: " + C.cWhite + ((HorseManager)this.getPlugin()).config.getPlayerHorseGender(this.playerView, Gender.MARE).size();
        arrstring2[2] = C.cDAqua + (this.getPlayer().getName().equals(this.playerView) ? "Your" : new StringBuilder().append(this.playerView).append("'s").toString()) + " Stallions: " + C.cWhite + ((HorseManager)this.getPlugin()).config.getPlayerHorseGender(this.playerView, Gender.STALLION).size();
        arrstring2[3] = C.cDAqua + (this.getPlayer().getName().equals(this.playerView) ? "Your" : new StringBuilder().append(this.playerView).append("'s").toString()) + " Geldings: " + C.cWhite + ((HorseManager)this.getPlugin()).config.getPlayerHorseGender(this.playerView, Gender.GELDING).size();
        arrstring2[4] = " ";
        arrstring2[5] = C.cDAqua + "Total Horses: " + C.cWhite + ((HorseManager)this.getPlugin()).config.horses.size();
        arrstring2[6] = C.cDAqua + "Total Pages: " + C.cWhite + (int)Math.ceil((double)((HorseManager)this.getPlugin()).config.horses.size() / 27.0);
        this.addItem(layout.get(0), ItemStackFactory.Instance.CreateStack(Material.BOOK, (byte)0, 1, C.cAqua + C.Bold + "Information:", arrstring2));
        if (Rank.Has(this.getPlayer(), Rank.JRMOD, false)) {
            this.addButton(layout.get(1), this.vision(), (player, clickType) -> {
                if (this.trusted) {
                    return;
                }
                this.all = !this.all;
                this.playClickSound(player);
                this.refresh();
            });
        }
        if (Rank.Has(this.getPlayer(), Rank.JRMOD, false)) {
            this.addButton(layout.get(2), this.trusted(), (player, clickType) -> {
                if (this.all) {
                    return;
                }
                this.trusted = !this.trusted;
                this.playClickSound(player);
                this.refresh();
            });
        } else {
            this.addButton(layout.get(1), this.trusted(), (player, clickType) -> {
                if (this.all) {
                    return;
                }
                this.trusted = !this.trusted;
                this.playClickSound(player);
                this.refresh();
            });
        }
    }

    private ItemStack vision() {
        ItemStack item;
        if (this.all) {
            item = new ItemStack(Material.EYE_OF_ENDER);
            ItemMeta meta = item.getItemMeta();
            meta.setDisplayName(C.cAqua + "View all horses: " + F.ed(this.all));
            ArrayList<String> lore = new ArrayList<String>();
            lore.add(" ");
            lore.add(C.cGray + "This mode allows staff to");
            lore.add(C.cGray + "view all horses, regardless");
            lore.add(C.cGray + "of ownership.");
            lore.add(" ");
            lore.add(C.cDAqua + "Click to " + C.cRed + "Disable");
            meta.setLore(lore);
            item.setItemMeta(meta);
        } else {
            item = new ItemStack(Material.ENDER_PEARL);
            ItemMeta meta = item.getItemMeta();
            meta.setDisplayName(C.cAqua + "View all horses: " + F.ed(this.all));
            ArrayList<String> lore = new ArrayList<String>();
            lore.add(" ");
            lore.add(C.cGray + "This mode allows staff to");
            lore.add(C.cGray + "view all horses, regardless");
            lore.add(C.cGray + "of ownership.");
            lore.add(" ");
            lore.add(C.cDAqua + "Click to " + C.cGreen + "Enable");
            meta.setLore(lore);
            item.setItemMeta(meta);
        }
        return item;
    }

    private ItemStack trusted() {
        ItemStack item;
        if (this.trusted) {
            item = new ItemStack(Material.EYE_OF_ENDER);
            ItemMeta meta = item.getItemMeta();
            meta.setDisplayName(C.cAqua + "View trusted: " + F.ed(this.trusted));
            ArrayList<String> lore = new ArrayList<String>();
            lore.add(" ");
            lore.add(C.cGray + "This mode allows you to view");
            lore.add(C.cGray + "all of the horses " + (this.getPlayer().getName().equals(this.playerView) ? "you are" : new StringBuilder().append(this.playerView).append(" is").toString()));
            lore.add(C.cGray + "trusted to.");
            lore.add(" ");
            lore.add(C.cDAqua + "Click to " + C.cRed + "Disable");
            meta.setLore(lore);
            item.setItemMeta(meta);
        } else {
            item = new ItemStack(Material.ENDER_PEARL);
            ItemMeta meta = item.getItemMeta();
            meta.setDisplayName(C.cAqua + "View trusted: " + F.ed(this.trusted));
            ArrayList<String> lore = new ArrayList<String>();
            lore.add(" ");
            lore.add(C.cGray + "This mode allows you to view");
            lore.add(C.cGray + "all of the horses " + (this.getPlayer().getName().equals(this.playerView) ? "you are" : new StringBuilder().append(this.playerView).append(" is").toString()));
            lore.add(C.cGray + "trusted to.");
            lore.add(" ");
            lore.add(C.cDAqua + "Click to " + C.cGreen + "Enable");
            meta.setLore(lore);
            item.setItemMeta(meta);
        }
        return item;
    }

    private ItemStack horseItem(CustomHorse horse) {
        ItemStack item = new ItemStack(Material.INK_SACK, 1, this.icon(horse));
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(C.cAqua + horse.token.name);
        ArrayList<String> lore = new ArrayList<String>();
        lore.add(" ");
        if (this.all) {
            lore.add(F.desc("Owner", horse.token.free ? "This horse is free" : UtilServer.getServer().getOfflinePlayer(horse.token.owner).getName()));
        } else if (horse.token.free) {
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
        lore.add(" ");
        if (this.all || horse.token.owner.equals(this.getPlayer().getUniqueId()) || Rank.Has(this.getPlayer(), Rank.JRMOD, false) || horse.token.trusted.contains(this.getPlayer().getUniqueId())) {
            if (this.getPlayer().getLocation().getWorld().getName().equals("Survival")) {
                lore.add(C.cRed + "You cannot spawn horses in Survival world.");
            } else {
                lore.add(C.cAqua + "Left-Click" + C.cWhite + " to summon");
            }
            if (horse.alive()) {
                lore.add(C.cAqua + "Right-Click" + C.cWhite + " to teleport");
                lore.add(C.cAqua + "Ctrl+Drop" + C.cWhite + " to despawn");
            } else {
                lore.add(" ");
                lore.add(C.cRed + "More option become available");
                lore.add(C.cRed + "when the horse is alive");
            }
        }
        meta.setLore(lore);
        item.setItemMeta(meta);
        return item;
    }

    private IButton button(CustomHorse horse) {
        return (player, clickType) -> {
            if (this.all || horse.token.owner.equals(this.getPlayer().getUniqueId()) || Rank.Has(this.getPlayer(), Rank.JRMOD, false) || horse.token.trusted.contains(this.getPlayer().getUniqueId())) {
                if (clickType.equals((Object)ClickType.LEFT) && !this.getPlayer().getLocation().getWorld().getName().equals("Survival")) {
                    if (horse.token.age == null) {
                        horse.setAge(System.currentTimeMillis());
                    }
                    horse.spawn(player.getLocation());
                    this.refresh();
                }
                if (horse.alive() && clickType.equals((Object)ClickType.RIGHT)) {
                    player.teleport((Entity)horse.horse);
                }
                if (horse.alive() && clickType.equals((Object)ClickType.CONTROL_DROP)) {
                    horse.remove();
                    horse.token.lastKnown = null;
                    this.refresh();
                }
            }
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
