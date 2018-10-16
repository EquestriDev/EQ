package com.equestriworlds.menu.page;

import com.equestriworlds.MiniPlugin;
import com.equestriworlds.account.CoreClient;
import com.equestriworlds.account.CoreClientManager;
import com.equestriworlds.common.CurrencyType;
import com.equestriworlds.menu.MenuBase;
import com.equestriworlds.menu.item.IButton;
import com.equestriworlds.util.UtilInv;
import java.util.HashMap;
import net.minecraft.server.v1_12_R1.EntityPlayer;
import net.minecraft.server.v1_12_R1.IInventory;
import net.minecraft.server.v1_12_R1.PlayerInventory;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftHumanEntity;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftInventoryCustom;
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

public abstract class AnvilPageBase<PluginType extends MiniPlugin, ShopType extends MenuBase<PluginType>>
extends CraftInventoryCustom
implements Listener {
    private PluginType _plugin;
    private CoreClientManager _clientManager;
    private ShopType _shop;
    private Player _player;
    private CoreClient _client;
    private CurrencyType _currencyType;
    private HashMap<Integer, IButton> _buttonMap;
    private boolean _showCurrency = false;
    private int _currencySlot = 4;

    public AnvilPageBase(PluginType plugin, ShopType shop, CoreClientManager clientManager, String name, Player player) {
        super(null, InventoryType.ANVIL, name);
        this._plugin = plugin;
        this._clientManager = clientManager;
        this._shop = shop;
        this._player = player;
        this._buttonMap = new HashMap();
        this._client = this._clientManager.Get(player);
    }

    protected abstract void buildPage();

    protected void addItem(int slot, ItemStack item) {
        if (slot > this.inventory.getSize() - 1) {
            this._player.getInventory().setItem(this.getPlayerSlot(slot), item);
        } else {
            this.setItem(slot, item);
        }
    }

    protected void addItemFakeCount(int slot, ItemStack item, int fakeCount) {
        net.minecraft.server.v1_12_R1.ItemStack nmsStack = CraftItemStack.asNMSCopy((ItemStack)item);
        nmsStack.setCount(fakeCount);
        if (slot > this.inventory.getSize() - 1) {
            ((CraftPlayer)this._player).getHandle().inventory.setItem(this.getPlayerSlot(slot), nmsStack);
        } else {
            this.getInventory().setItem(slot, nmsStack);
        }
    }

    protected int getPlayerSlot(int slot) {
        return slot >= this.inventory.getSize() + 27 ? slot - (this.inventory.getSize() + 27) : slot - (this.inventory.getSize() - 9);
    }

    protected void addButton(int slot, ItemStack item, IButton button) {
        this.addItem(slot, item);
        this._buttonMap.put(slot, button);
    }

    protected void addButtonFakeCount(int slot, ItemStack item, IButton button, int fakeItemCount) {
        this.addItemFakeCount(slot, item, fakeItemCount);
        this._buttonMap.put(slot, button);
    }

    protected void addGlow(int slot) {
        UtilInv.addGlow(this.getItem(slot));
    }

    protected void removeButton(int slot) {
        this.getInventory().setItem(slot, null);
        this._buttonMap.remove(slot);
    }

    public void playerClicked(InventoryClickEvent event) {
        if (this._buttonMap.containsKey(event.getRawSlot())) {
            this._buttonMap.get(event.getRawSlot()).onClick(this._player, event.getClick());
        } else if (event.getRawSlot() != -999) {
            if (event.getInventory().getTitle() == this.inventory.getName() && (this.inventory.getSize() <= event.getSlot() || this.inventory.getItem(event.getSlot()) != null)) {
                this.playDenySound(this._player);
            } else if (event.getInventory() == this._player.getInventory() && this._player.getInventory().getItem(event.getSlot()) != null) {
                this.playDenySound(this._player);
            }
        }
    }

    public void playerOpened() {
    }

    public void playerClosed() {
        this.inventory.onClose((CraftHumanEntity)((CraftPlayer)this._player));
    }

    public void playAcceptSound(Player player) {
        player.playSound(player.getLocation(), Sound.BLOCK_NOTE_PLING, 1.0f, 1.6f);
    }

    public void playRemoveSound(Player player) {
        player.playSound(player.getLocation(), Sound.BLOCK_NOTE_PLING, 1.0f, 0.6f);
    }

    public void playDenySound(Player player) {
        player.playSound(player.getLocation(), Sound.ENTITY_ITEM_BREAK, 1.0f, 0.6f);
    }

    public void dispose() {
        this._player = null;
        this._client = null;
        this._shop = null;
        this._plugin = null;
    }

    public void refresh() {
        this.clearPage();
        this.buildPage();
    }

    public void clearPage() {
        this.clear();
        this._buttonMap.clear();
    }

    public void setItem(int column, int row, ItemStack itemStack) {
        this.setItem(column + row * 9, itemStack);
    }

    public ShopType getShop() {
        return this._shop;
    }

    public PluginType getPlugin() {
        return this._plugin;
    }

    public CoreClientManager getClientManager() {
        return this._clientManager;
    }

    protected Player getPlayer() {
        return this._player;
    }

    protected CoreClient getClient() {
        return this._client;
    }

    protected CurrencyType getCurrencyType() {
        return this._currencyType;
    }

    protected void setCurrencyType(CurrencyType type) {
        this._currencyType = type;
    }

    protected HashMap<Integer, IButton> getButtonMap() {
        return this._buttonMap;
    }

    protected boolean shouldShowCurrency() {
        return this._showCurrency;
    }

    protected int getCurrencySlot() {
        return this._currencySlot;
    }
}
