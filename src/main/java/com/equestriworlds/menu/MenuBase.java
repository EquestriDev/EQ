package com.equestriworlds.menu;

import com.equestriworlds.MiniPlugin;
import com.equestriworlds.account.CoreClientManager;
import com.equestriworlds.menu.page.MenuPageBase;
import java.util.HashMap;
import java.util.HashSet;
import net.minecraft.server.v1_12_R1.Container;
import net.minecraft.server.v1_12_R1.EntityHuman;
import net.minecraft.server.v1_12_R1.EntityPlayer;
import org.bukkit.ChatColor;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_12_R1.event.CraftEventFactory;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;

public abstract class MenuBase<PluginType extends MiniPlugin>
implements Listener {
    private HashMap<String, Long> _errorThrottling;
    private HashMap<String, Long> _purchaseBlock;
    private PluginType _plugin;
    private CoreClientManager _clientManager;
    private String _name;
    private HashMap<String, MenuPageBase<PluginType, ? extends MenuBase<PluginType>>> _playerPageMap;
    private HashSet<String> _openedShop = new HashSet();

    public MenuBase(PluginType plugin, CoreClientManager clientManager, String name) {
        this._plugin = plugin;
        this._clientManager = clientManager;
        this._name = name;
        this._playerPageMap = new HashMap();
        this._errorThrottling = new HashMap();
        this._purchaseBlock = new HashMap();
        this._plugin.registerEvents(this);
    }

    private boolean attemptShopOpen(Player player, LivingEntity entity) {
        if (!this._openedShop.contains(player.getName()) && entity.isCustomNameVisible() && entity.getCustomName() != null && ChatColor.stripColor((String)entity.getCustomName()).equalsIgnoreCase(ChatColor.stripColor((String)this._name))) {
            if (!this.canOpenShop(player)) {
                return false;
            }
            this._openedShop.add(player.getName());
            this.openShopForPlayer(player);
            if (!this._playerPageMap.containsKey(player.getName())) {
                this._playerPageMap.put(player.getName(), this.buildPagesFor(player));
            }
            this.openPageForPlayer(player, this.getOpeningPageForPlayer(player));
            return true;
        }
        return false;
    }

    public boolean attemptShopOpen(Player player) {
        if (!this._openedShop.contains(player.getName())) {
            if (!this.canOpenShop(player)) {
                return false;
            }
            this._openedShop.add(player.getName());
            this.openShopForPlayer(player);
            if (!this._playerPageMap.containsKey(player.getName())) {
                this._playerPageMap.put(player.getName(), this.buildPagesFor(player));
            }
            this.openPageForPlayer(player, this.getOpeningPageForPlayer(player));
            return true;
        }
        return false;
    }

    private MenuPageBase<PluginType, ? extends MenuBase<PluginType>> getOpeningPageForPlayer(Player player) {
        return this._playerPageMap.get(player.getName());
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (this._playerPageMap.containsKey(event.getWhoClicked().getName()) && this._playerPageMap.get(event.getWhoClicked().getName()).getName().equalsIgnoreCase(event.getInventory().getName())) {
            this._playerPageMap.get(event.getWhoClicked().getName()).playerClicked(event);
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        if (this._playerPageMap.containsKey(event.getPlayer().getName()) && this._playerPageMap.get(event.getPlayer().getName()).getTitle() != null && this._playerPageMap.get(event.getPlayer().getName()).getTitle().equalsIgnoreCase(event.getInventory().getTitle())) {
            this._playerPageMap.get(event.getPlayer().getName()).playerClosed();
            this._playerPageMap.get(event.getPlayer().getName()).dispose();
            this._playerPageMap.remove(event.getPlayer().getName());
            this.closeShopForPlayer((Player)event.getPlayer());
            this._openedShop.remove(event.getPlayer().getName());
        }
    }

    @EventHandler(priority=EventPriority.MONITOR)
    public void onInventoryOpen(InventoryOpenEvent event) {
        if (!event.isCancelled()) {
            return;
        }
        if (this._playerPageMap.containsKey(event.getPlayer().getName()) && this._playerPageMap.get(event.getPlayer().getName()).getTitle() != null && this._playerPageMap.get(event.getPlayer().getName()).getTitle().equalsIgnoreCase(event.getInventory().getTitle())) {
            this._playerPageMap.get(event.getPlayer().getName()).playerClosed();
            this._playerPageMap.get(event.getPlayer().getName()).dispose();
            this._playerPageMap.remove(event.getPlayer().getName());
            this.closeShopForPlayer((Player)event.getPlayer());
            this._openedShop.remove(event.getPlayer().getName());
        }
    }

    protected boolean canOpenShop(Player player) {
        return true;
    }

    protected void openShopForPlayer(Player player) {
    }

    protected void closeShopForPlayer(Player player) {
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        if (this._playerPageMap.containsKey(event.getPlayer().getName())) {
            this._playerPageMap.get(event.getPlayer().getName()).playerClosed();
            this._playerPageMap.get(event.getPlayer().getName()).dispose();
            event.getPlayer().closeInventory();
            this.closeShopForPlayer(event.getPlayer());
            this._playerPageMap.remove(event.getPlayer().getName());
            this._openedShop.remove(event.getPlayer().getName());
        }
    }

    public void openPageForPlayer(Player player, MenuPageBase<PluginType, ? extends MenuBase<PluginType>> page) {
        if (this._playerPageMap.containsKey(player.getName())) {
            this._playerPageMap.get(player.getName()).playerClosed();
        }
        this.setCurrentPageForPlayer(player, page);
        EntityPlayer nmsPlayer = ((CraftPlayer)player).getHandle();
        if (nmsPlayer.activeContainer != nmsPlayer.defaultContainer) {
            CraftEventFactory.handleInventoryCloseEvent((EntityHuman)nmsPlayer);
            nmsPlayer.s();
        }
        player.openInventory(page);
        try {
            this._playerPageMap.get(player.getName()).refresh();
        }
        catch (NullPointerException nullPointerException) {
            // empty catch block
        }
    }

    public void setCurrentPageForPlayer(Player player, MenuPageBase<PluginType, ? extends MenuBase<PluginType>> page) {
        this._playerPageMap.put(player.getName(), page);
    }

    public void addPlayerProcessError(Player player) {
        if (this._errorThrottling.containsKey(player.getName()) && System.currentTimeMillis() - this._errorThrottling.get(player.getName()) <= 5000L) {
            this._purchaseBlock.put(player.getName(), System.currentTimeMillis());
        }
        this._errorThrottling.put(player.getName(), System.currentTimeMillis());
    }

    public boolean canPlayerAttemptPurchase(Player player) {
        return !this._purchaseBlock.containsKey(player.getName()) || System.currentTimeMillis() - this._purchaseBlock.get(player.getName()) > 10000L;
    }

    public HashMap<String, MenuPageBase<PluginType, ? extends MenuBase<PluginType>>> getPageMap() {
        return this._playerPageMap;
    }

    protected abstract MenuPageBase<PluginType, ? extends MenuBase<PluginType>> buildPagesFor(Player var1);

    public boolean isPlayerInShop(Player player) {
        return this._playerPageMap.containsKey(player.getName());
    }

    protected PluginType getPlugin() {
        return this._plugin;
    }

    protected CoreClientManager getClientManager() {
        return this._clientManager;
    }

    protected String getName() {
        return this._name;
    }

    protected HashMap<String, MenuPageBase<PluginType, ? extends MenuBase<PluginType>>> getPlayerPageMap() {
        return this._playerPageMap;
    }

    protected HashSet<String> getOpenedShop() {
        return this._openedShop;
    }
}
