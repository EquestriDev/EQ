package com.equestriworlds.menu.page;

import com.equestriworlds.MiniPlugin;
import com.equestriworlds.account.CoreClient;
import com.equestriworlds.account.CoreClientManager;
import com.equestriworlds.itemstack.ItemStackFactory;
import com.equestriworlds.menu.MenuBase;
import com.equestriworlds.menu.item.IButton;
import com.equestriworlds.menu.page.PurchaseConfirmationPage;
import com.equestriworlds.util.C;
import com.equestriworlds.util.UtilInv;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import net.milkbowl.vault.economy.Economy;
import net.minecraft.server.v1_12_R1.EntityPlayer;
import net.minecraft.server.v1_12_R1.IInventory;
import net.minecraft.server.v1_12_R1.PlayerInventory;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.Sound;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftHumanEntity;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftInventoryCustom;
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

/**
 * Chest Menu
 */
public abstract class MenuPageBase<PluginType extends MiniPlugin, ShopType extends MenuBase<PluginType>>
extends CraftInventoryCustom
implements Listener {
    private PluginType _plugin;
    private CoreClientManager _clientManager;
    private ShopType _shop;
    private Player _player;
    private CoreClient _client;
    private HashMap<Integer, IButton> _buttonMap;
    private boolean _showCurrency = false;
    private int _currencySlot = 4;

    public MenuPageBase(PluginType plugin, ShopType shop, CoreClientManager clientManager, String name, Player player) {
        this(plugin, shop, clientManager, name, player, 54);
    }

    public MenuPageBase(PluginType plugin, ShopType shop, CoreClientManager clientManager, String name, Player player, int slots) {
        super(null, slots, name);
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

    private void addItemFakeCount(int slot, ItemStack item, int fakeCount) {
        net.minecraft.server.v1_12_R1.ItemStack nmsStack = CraftItemStack.asNMSCopy((ItemStack)item);
        nmsStack.setCount(fakeCount);
        if (slot > this.inventory.getSize() - 1) {
            ((CraftPlayer)this._player).getHandle().inventory.setItem(this.getPlayerSlot(slot), nmsStack);
        } else {
            this.getInventory().setItem(slot, nmsStack);
        }
    }

    private int getPlayerSlot(int slot) {
        return slot >= this.inventory.getSize() + 27 ? slot - (this.inventory.getSize() + 27) : slot - (this.inventory.getSize() - 9);
    }

    protected void addButton(int slot, ItemStack item, IButton button) {
        this.addItem(slot, item);
        this._buttonMap.put(slot, button);
    }

    protected void addButton(int slot, ItemStack item) {
        this.addItem(slot, item);
    }

    private void addButtonFakeCount(int slot, ItemStack item, IButton button, int fakeItemCount) {
        this.addItemFakeCount(slot, item, fakeItemCount);
        this._buttonMap.put(slot, button);
    }

    protected void addGlow(int slot) {
        UtilInv.addGlow(this.getItem(slot));
    }

    protected void addGlow(ItemStack item) {
        UtilInv.addGlow(item);
    }

    protected void addConfirmation(Economy eco, int slot, Material material, String name, int price, Runnable runnable) {
        this.addConfirmation(eco, slot, material, 1, name, new String[0], price, runnable);
    }

    protected void addConfirmation(Economy eco, int slot, Material material, int amount, String name, int price, Runnable runnable) {
        this.addConfirmation(eco, slot, material, amount, name, new String[0], price, runnable);
    }

    protected void addConfirmation(Economy eco, int slot, Material material, int amount, String name, String[] description, int price, Runnable runnable) {
        MenuPageBase shop = this;
        this.addConfirmation(eco, slot, material, amount, name, description, price, runnable, shop);
    }

    private void addConfirmation(Economy eco, int slot, Material material, int amount, String name, String[] description, int price, Runnable runnable, MenuPageBase<PluginType, ShopType> shop) {
        this.addConfirmation(eco, slot, material, (byte)0, amount, name, description, price, runnable, shop);
    }

    protected void addConfirmation(Economy eco, int slot, Material material, byte data, int amount, String name, String[] description, int price, Runnable runnable, MenuPageBase<PluginType, ShopType> shop) {
        ArrayList<String> lore = new ArrayList<String>();
        lore.add(" ");
        for (String string : description) {
            lore.add(C.cGray + string);
        }
        if (!eco.has((OfflinePlayer)this.getPlayer(), (double)price)) {
            lore.add(" ");
            lore.add(C.cRed + "You have insufficient funds.");
        }
        this.addButtonFakeCount(slot, ItemStackFactory.Instance.CreateStack(material, data, amount, C.cYellow + C.Bold + name + " " + (eco.has((OfflinePlayer)this.getPlayer(), (double)price) ? C.cGreen : C.cRed) + C.Bold + "$" + price, lore), (player, clickType) -> this.getShop().openPageForPlayer(this.getPlayer(), new PurchaseConfirmationPage<PluginType, ShopType>(this.getPlugin(), this.getShop(), this.getClientManager(), eco, ItemStackFactory.Instance.CreateStack(material, data, amount, C.cYellow + C.Bold + name + " " + (eco.has((OfflinePlayer)this.getPlayer(), (double)price) ? C.cGreen : C.cRed) + C.Bold + "$" + price, (List<String>)lore), name, this.getPlayer(), price, runnable, shop)), amount);
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

    public void playClickSound(Player player) {
        player.playSound(player.getLocation(), Sound.BLOCK_DISPENSER_DISPENSE, 1.0f, 1.0f);
    }

    public void playAcceptSound(Player player) {
        player.playSound(player.getLocation(), Sound.BLOCK_NOTE_PLING, 1.0f, 1.6f);
    }

    public void playRemoveSound(Player player) {
        player.playSound(player.getLocation(), Sound.BLOCK_NOTE_PLING, 1.0f, 0.6f);
    }

    public void playDenySound(Player player) {
        player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BASS, 1.0f, 0.6f);
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
