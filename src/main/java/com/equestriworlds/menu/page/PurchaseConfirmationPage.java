package com.equestriworlds.menu.page;

import com.equestriworlds.MiniPlugin;
import com.equestriworlds.account.CoreClientManager;
import com.equestriworlds.itemstack.ItemStackFactory;
import com.equestriworlds.menu.MenuBase;
import com.equestriworlds.menu.item.IButton;
import com.equestriworlds.menu.item.ShopItem;
import com.equestriworlds.menu.page.MenuPageBase;
import com.equestriworlds.util.C;
import java.util.HashMap;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;

public class PurchaseConfirmationPage<PluginType extends MiniPlugin, ShopType extends MenuBase<PluginType>>
extends MenuPageBase<PluginType, ShopType>
implements Runnable {
    private Economy eco;
    private ItemStack item;
    private int price;
    private Runnable success;
    private MenuPageBase<PluginType, ShopType> returnPage;
    private int _cancelSquareSlotStart = 27;
    private boolean _processing;
    private int _progressCount;
    private ShopItem _progressItem = new ShopItem(Material.LAPIS_BLOCK, (byte)11, (Object)ChatColor.BLUE + "Processing", null, 1, false, true);
    private int _taskId;

    PurchaseConfirmationPage(PluginType plugin, ShopType shop, CoreClientManager clientManager, Economy eco, ItemStack item, String name, Player player, int price, Runnable success, MenuPageBase<PluginType, ShopType> returnPage) {
        super(plugin, shop, clientManager, "Purchase: " + C.Underline + name, player);
        this.eco = eco;
        this.item = item;
        this.price = price;
        this.success = success;
        this.returnPage = returnPage;
        this.buildPage();
    }

    @Override
    protected void buildPage() {
        this.setItem(22, this.item);
        IButton okClicked = (player, clickType) -> this.yesClicked(player);
        IButton cancelClicked = (player, clickType) -> this.noClicked(player);
        this.buildSquareAt(this._cancelSquareSlotStart, new ShopItem(Material.REDSTONE_BLOCK, (byte)0, (Object)ChatColor.RED + "BACK", null, 1, false, true), cancelClicked);
        this.buildSquareAt(this._cancelSquareSlotStart + 6, new ShopItem(Material.EMERALD_BLOCK, (byte)0, (Object)ChatColor.GREEN + "CONFIRM", null, 1, false, true), okClicked);
        this.addItem(4, ItemStackFactory.Instance.CreateStack(Material.EMERALD, (byte)0, 1, C.cGreen + C.Bold + "$" + this.price, new String[]{C.cGray + "will be taken from your balance."}));
    }

    private void yesClicked(Player player) {
        this.processTransaction();
    }

    private void processTransaction() {
        for (int i = this._cancelSquareSlotStart; i < 54; ++i) {
            this.getButtonMap().remove(i);
            this.clear(i);
        }
        this._processing = true;
        this._taskId = this.getPlugin().getScheduler().scheduleSyncRepeatingTask((Plugin)this.getPlugin().getPlugin(), (Runnable)this, 2L, 2L);
        this._processing = false;
        if (!this.eco.has((OfflinePlayer)this.getPlayer(), (double)this.price)) {
            this.buildErrorPage(C.cRed + "You have insufficient funds.");
            this.getShop().addPlayerProcessError(this.getPlayer());
        } else {
            this.eco.withdrawPlayer((OfflinePlayer)this.getPlayer(), (double)this.price);
            this.buildSuccessPage();
            if (this.success != null) {
                this.success.run();
            }
        }
        this._progressCount = 0;
    }

    private /* varargs */ void buildErrorPage(String ... message) {
        IButton returnButton = (player, clickType) -> this.noClicked(player);
        ShopItem item = new ShopItem(Material.REDSTONE_BLOCK, (byte)0, (Object)ChatColor.RED + "" + (Object)ChatColor.UNDERLINE + "ERROR", message, 1, false, true);
        for (int i = 0; i < this.getSize(); ++i) {
            this.addButton(i, item, returnButton);
        }
        this.getPlayer().playSound(this.getPlayer().getLocation(), Sound.ENTITY_BLAZE_DEATH, 1.0f, 0.1f);
    }

    private void buildSuccessPage() {
        IButton returnButton = (player, clickType) -> this.noClicked(player);
        ShopItem item = new ShopItem(Material.EMERALD_BLOCK, (byte)0, (Object)ChatColor.GREEN + "Success!", null, 1, false, true);
        for (int i = 0; i < this.getSize(); ++i) {
            this.addButton(i, item, returnButton);
        }
        this.getPlayer().playSound(this.getPlayer().getLocation(), Sound.BLOCK_NOTE_PLING, 1.0f, 0.9f);
    }

    private void noClicked(Player player) {
        if (this.returnPage != null) {
            this.getShop().openPageForPlayer(player, this.returnPage);
        } else {
            player.closeInventory();
        }
    }

    private void buildSquareAt(int slot, ItemStack item, IButton button) {
        this.addButton(slot, item, button);
        this.addButton(slot + 1, item, button);
        this.addButton(slot + 2, item, button);
        this.addButton(slot += 9, item, button);
        this.addButton(slot + 1, item, button);
        this.addButton(slot + 2, item, button);
        this.addButton(slot += 9, item, button);
        this.addButton(slot + 1, item, button);
        this.addButton(slot + 2, item, button);
    }

    @Override
    public void playerClosed() {
        super.playerClosed();
        Bukkit.getScheduler().cancelTask(this._taskId);
        if (this.returnPage != null && this.getShop() != null) {
            this.getShop().setCurrentPageForPlayer(this.getPlayer(), this.returnPage);
        }
    }

    @Override
    public void run() {
        if (this._processing) {
            if (this._progressCount == 9) {
                for (int i = 45; i < 54; ++i) {
                    this.clear(i);
                }
                this._progressCount = 0;
            }
            this.setItem(45 + this._progressCount, (ItemStack)this._progressItem);
        } else if (this._progressCount >= 20) {
            try {
                Bukkit.getScheduler().cancelTask(this._taskId);
                if (this.returnPage != null && this.getShop() != null) {
                    this.getShop().openPageForPlayer(this.getPlayer(), this.returnPage);
                    this.returnPage.refresh();
                } else if (this.getPlayer() != null) {
                    this.getPlayer().closeInventory();
                }
            }
            catch (Exception exception) {
                exception.printStackTrace();
            }
            finally {
                this.dispose();
            }
        }
        ++this._progressCount;
    }

    @Override
    public void dispose() {
        super.dispose();
        Bukkit.getScheduler().cancelTask(this._taskId);
    }
}
