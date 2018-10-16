package com.equestriworlds.itemstack;

import com.equestriworlds.MiniPlugin;
import com.equestriworlds.util.C;
import com.equestriworlds.util.UtilInv;
import com.equestriworlds.util.UtilMath;
import com.equestriworlds.util.UtilTime;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftItemStack;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Item;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.ItemSpawnEvent;
import org.bukkit.event.inventory.FurnaceSmeltEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.CraftingInventory;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.material.MaterialData;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;

/**
 * Way overengineered item creation helper routines.
 */
public class ItemStackFactory
extends MiniPlugin {
    public static ItemStackFactory Instance;
    private HashMap<Integer, HashMap<Byte, AbstractMap.SimpleEntry<String, Boolean>>> _names;
    private HashMap<Integer, HashMap<Byte, String[]>> _lores;
    private String _nameFormat = "\u00a7r" + C.mItem;
    private HashSet<Listener> _statListeners = new HashSet();
    private boolean _customNames = false;

    protected ItemStackFactory(JavaPlugin plugin, boolean customNames) {
        super("ItemStack Factory", plugin);
        this.AddDefault();
    }

    public static void Initialize(JavaPlugin plugin, boolean customNames) {
        Instance = new ItemStackFactory(plugin, customNames);
    }

    public void AddStatListener(Listener listener) {
        this._statListeners.add(listener);
        this.registerEvents(listener);
    }

    private void Add(Material mat, byte data, String name, boolean special) {
        this.Add(mat.getId(), data, name, null, special);
    }

    private void Add(int id, byte data, String name, boolean special) {
        this.Add(id, data, name, null, special);
    }

    private void Add(Material mat, byte data, String name, String[] lore, boolean special) {
        this.Add(mat.getId(), data, name, lore, special);
    }

    private void Add(int id, byte data, String name, String[] lore, boolean special) {
        if (!this._names.containsKey(id)) {
            this._names.put(id, new HashMap());
        }
        this._names.get(id).put(data, new AbstractMap.SimpleEntry<String, Boolean>(name, special));
        if (lore == null) {
            return;
        }
        if (!this._lores.containsKey(id)) {
            this._lores.put(id, new HashMap());
        }
        this._lores.get(id).put(data, lore);
    }

    private void AddDefault() {
        this._names = new HashMap();
        this._lores = new HashMap();
        for (int id = 0; id < 10000; ++id) {
            Material mat = Material.getMaterial((int)id);
            if (mat == null) continue;
            HashMap<Byte, AbstractMap.SimpleEntry<String, Boolean>> variants = new HashMap<Byte, AbstractMap.SimpleEntry<String, Boolean>>();
            this._names.put(id, variants);
            for (byte data = 0; data < 50; data = (byte)((byte)(data + 1))) {
                try {
                    String name = "";
                    ItemStack stack = new ItemStack(id, 1, (short)data);
                    if (CraftItemStack.asNMSCopy((ItemStack)stack) != null && CraftItemStack.asNMSCopy((ItemStack)stack).getName() != null) {
                        name = CraftItemStack.asNMSCopy((ItemStack)stack).getName();
                    }
                    if (id == 140) {
                        name = "Flower Pot";
                    }
                    if (name.length() == 0) {
                        name = this.Clean(mat.toString());
                    }
                    boolean duplicate = false;
                    for (Map.Entry cur : variants.values()) {
                        if (!((String)cur.getKey()).equals(name)) continue;
                        duplicate = true;
                        break;
                    }
                    if (duplicate) continue;
                    variants.put(data, new AbstractMap.SimpleEntry<String, Boolean>(name, mat.getMaxStackSize() == 1));
                    continue;
                }
                catch (Exception name) {
                    // empty catch block
                }
            }
        }
    }

    private String Clean(String string) {
        String[] words;
        String out = "";
        for (String word : words = string.split("_")) {
            if (word.length() < 1) {
                return "Unknown";
            }
            out = out + word.charAt(0) + word.substring(1, word.length()).toLowerCase() + " ";
        }
        return out.substring(0, out.length() - 1);
    }

    private String GetItemStackName(ItemStack stack) {
        return stack.getItemMeta().getDisplayName();
    }

    private String GetName(ItemStack stack, boolean formatted) {
        if (stack == null) {
            return "Unarmed";
        }
        if (stack.getData() != null) {
            return this.GetName(stack.getTypeId(), stack.getData().getData(), formatted);
        }
        return this.GetName(stack.getTypeId(), (byte)0, formatted);
    }

    public String GetName(Block block, boolean formatted) {
        return this.GetName(block.getTypeId(), block.getData(), formatted);
    }

    public String GetName(Material mat, byte data, boolean formatted) {
        return this.GetName(mat.getId(), data, formatted);
    }

    public String GetName(int id, byte data, boolean formatted) {
        String out = "";
        if (formatted) {
            out = this._nameFormat;
        }
        if (!this._names.containsKey(id)) {
            return out + "Unknown";
        }
        if (!this._names.get(id).containsKey(data)) {
            if (this._names.get(id).containsKey(0)) {
                return out + this._names.get(id).get(0).getKey();
            }
            Iterator<? extends Map.Entry<String, Boolean>> iterator = this._names.get(id).values().iterator();
            if (iterator.hasNext()) {
                Map.Entry<String, Boolean> cur = iterator.next();
                return cur.getKey();
            }
            return out + "Unknown";
        }
        return out + this._names.get(id).get(data).getKey();
    }

    public boolean IsSpecial(ItemStack stack) {
        if (stack == null) {
            return false;
        }
        if (stack.getData() != null) {
            return this.IsSpecial(stack.getTypeId(), stack.getData().getData());
        }
        return this.IsSpecial(stack.getTypeId(), (byte)0);
    }

    public boolean IsSpecial(Material mat, byte data) {
        return this.IsSpecial(mat.getId(), data);
    }

    public boolean IsSpecial(int id, byte data) {
        if (!this._names.containsKey(id)) {
            return false;
        }
        if (!this._names.get(id).containsKey(data)) {
            if (this._names.get(id).containsKey(0)) {
                return this._names.get(id).get(0).getValue();
            }
            return false;
        }
        return this._names.get(id).get(data).getValue();
    }

    public void StatsArmorRename(ItemStack item, int damage) {
        if (!this._customNames) {
            return;
        }
        if (item == null) {
            return;
        }
        if (item.getMaxStackSize() > 1) {
            return;
        }
        this.SetLoreVar(item, "Damage Tanked", "" + (damage += this.GetLoreVar(item, "Damage Tanked", 0)));
        if (damage >= 10000) {
            item.addEnchantment(Enchantment.DURABILITY, 1);
        }
    }

    @EventHandler(priority=EventPriority.MONITOR)
    public void StatsBlockMined(BlockBreakEvent event) {
        if (!this._customNames) {
            return;
        }
        if (event.isCancelled()) {
            return;
        }
        ItemStack item = event.getPlayer().getItemInHand();
        if (item == null) {
            return;
        }
        if (item.getMaxStackSize() > 1) {
            return;
        }
        int blocks = 1 + this.GetLoreVar(item, "Blocks Mined", 0);
        this.SetLoreVar(item, "Blocks Mined", blocks + "");
    }

    @EventHandler(priority=EventPriority.HIGHEST)
    public void StatsKillMob(EntityDeathEvent event) {
        if (!this._customNames) {
            return;
        }
        if (!(event.getEntity() instanceof Monster)) {
            return;
        }
        Monster ent = (Monster)event.getEntity();
        if (ent.getKiller() == null) {
            return;
        }
        if (ent.getKiller().isBlocking()) {
            return;
        }
        ItemStack item = ent.getKiller().getItemInHand();
        if (item == null) {
            return;
        }
        if (item.getMaxStackSize() > 1) {
            return;
        }
        int kills = 1 + this.GetLoreVar(item, "Monster Kills", 0);
        this.SetLoreVar(item, "Monster Kills", "" + kills);
    }

    @EventHandler(priority=EventPriority.MONITOR)
    public void StatsBowShoot(EntityShootBowEvent event) {
        if (!this._customNames) {
            return;
        }
        if (event.isCancelled()) {
            return;
        }
        int shots = 1 + this.GetLoreVar(event.getBow(), "Arrows Shot", 0);
        this.SetLoreVar(event.getBow(), "Arrows Shot", "" + shots);
        int hits = this.GetLoreVar(event.getBow(), "Arrows Hit", 0);
        double acc = UtilMath.trim(1, (double)hits / (double)shots * 100.0);
        this.SetLoreVar(event.getBow(), "Accuracy", acc + "%");
    }

    @EventHandler
    public void RenameSpawn(ItemSpawnEvent event) {
        if (!this._customNames) {
            return;
        }
        if (event.isCancelled()) {
            return;
        }
        String color = ChatColor.getLastColors((String)this.GetItemStackName(event.getEntity().getItemStack()));
        if (color != null && color.length() >= 2 && color.charAt(1) != 'f') {
            return;
        }
        int id = event.getEntity().getItemStack().getTypeId();
        byte data = 0;
        if (event.getEntity().getItemStack().getData() != null) {
            data = event.getEntity().getItemStack().getData().getData();
        }
    }

    @EventHandler
    public void RenameArrow(PlayerPickupItemEvent event) {
        if (!this._customNames) {
            return;
        }
        if (event.isCancelled()) {
            return;
        }
        ItemStack stack = event.getItem().getItemStack();
        if (stack.getType() != Material.ARROW) {
            return;
        }
        String color = ChatColor.getLastColors((String)this.GetItemStackName(stack));
        if (color != null && color.length() >= 2 && color.charAt(1) != 'f') {
            return;
        }
        byte data = 0;
        if (stack.getData() != null) {
            data = stack.getData().getData();
        }
        event.setCancelled(true);
        event.getItem().remove();
        if (data == 1) {
            return;
        }
        event.getPlayer().getInventory().addItem(new ItemStack[]{this.CreateStack(stack.getTypeId(), data, stack.getAmount())});
    }

    @EventHandler
    public void RenameSmelt(FurnaceSmeltEvent event) {
        if (!this._customNames) {
            return;
        }
        ItemStack stack = event.getResult();
        byte data = 0;
        if (stack.getData() != null) {
            data = stack.getData().getData();
        }
        ItemStack result = this.CreateStack(stack.getTypeId(), data, stack.getAmount());
        event.setResult(result);
    }

    @EventHandler
    public void RenameCraft(PrepareItemCraftEvent event) {
        if (!this._customNames) {
            return;
        }
        ItemStack stack = event.getInventory().getResult();
        byte data = 0;
        if (stack.getData() != null) {
            data = stack.getData().getData();
        }
        String crafter = null;
        if (event.getViewers().size() == 1 && stack.getMaxStackSize() == 1) {
            crafter = ((HumanEntity)event.getViewers().get(0)).getName() + " Crafting";
        }
        ItemStack result = this.CreateStack(stack.getTypeId(), data, stack.getAmount(), null, new String[0], crafter);
        event.getInventory().setResult(result);
    }

    @EventHandler
    public void RenameCraftAlg(InventoryClickEvent event) {
        if (!this._customNames) {
            return;
        }
        if (!event.isShiftClick()) {
            return;
        }
        if (event.getSlotType() != InventoryType.SlotType.RESULT) {
            return;
        }
        if (!(event.getInventory() instanceof CraftingInventory)) {
            return;
        }
        CraftingInventory inv = (CraftingInventory)event.getInventory();
        int make = 64;
        for (ItemStack item : inv.getMatrix()) {
            if (item == null || item.getType() == Material.AIR || item.getAmount() >= make) continue;
            make = item.getAmount();
        }
        --make;
        for (int i = 0; i < inv.getMatrix().length; ++i) {
            if (inv.getMatrix()[i] == null || inv.getMatrix()[i].getType() == Material.AIR) continue;
            if (inv.getMatrix()[i].getAmount() > make) {
                inv.getMatrix()[i].setAmount(inv.getMatrix()[i].getAmount() - make);
                continue;
            }
            inv.getMatrix()[i].setAmount(1);
        }
        int id = event.getCurrentItem().getTypeId();
        byte data = 0;
        if (event.getCurrentItem().getData() != null) {
            data = event.getCurrentItem().getData().getData();
        }
        int amount = event.getCurrentItem().getAmount();
        String crafter = null;
        if (event.getViewers().size() == 1 && event.getCurrentItem().getMaxStackSize() == 1) {
            crafter = ((HumanEntity)event.getViewers().get(0)).getName() + " Crafting";
        }
        for (int i = 0; i < make; ++i) {
            ItemStack result = this.CreateStack(id, data, amount, null, new String[0], crafter);
            if (result == null) continue;
            event.getWhoClicked().getInventory().addItem(new ItemStack[]{result});
        }
        if (event.getWhoClicked() instanceof Player) {
            final Player player = (Player)event.getWhoClicked();
            this._plugin.getServer().getScheduler().scheduleSyncDelayedTask((Plugin)this._plugin, new Runnable(){

                @Override
                public void run() {
                    UtilInv.Update((Entity)player);
                }
            }, 0L);
        }
    }

    public ItemStack CreateStack(Material type) {
        return this.CreateStack(type.getId(), (byte)0, 1, (short)0, null, new String[0], null);
    }

    public ItemStack CreateStack(int id) {
        return this.CreateStack(id, (byte)0, 1, (short)0, null, new String[0], null);
    }

    public ItemStack CreateStack(Material type, int amount) {
        return this.CreateStack(type.getId(), (byte)0, amount, (short)0, null, new String[0], null);
    }

    public ItemStack CreateStack(int id, int amount) {
        return this.CreateStack(id, (byte)0, amount, (short)0, null, new String[0], null);
    }

    public ItemStack CreateStack(Material type, byte data) {
        return this.CreateStack(type.getId(), data, 1, (short)0, null, new String[0], null);
    }

    public ItemStack CreateStack(int id, byte data) {
        return this.CreateStack(id, data, 1, (short)0, null, new String[0], null);
    }

    public ItemStack CreateStack(Material type, byte data, int amount) {
        return this.CreateStack(type.getId(), data, amount, (short)0, null, new String[0], null);
    }

    public ItemStack CreateStack(int id, byte data, int amount) {
        return this.CreateStack(id, data, amount, (short)0, null, new String[0], null);
    }

    public ItemStack CreateStack(Material type, byte data, int amount, String name) {
        return this.CreateStack(type.getId(), data, amount, (short)0, name, new String[0], null);
    }

    public ItemStack CreateStack(int id, byte data, int amount, String name) {
        return this.CreateStack(id, data, amount, (short)0, name, new String[0], null);
    }

    public ItemStack CreateStack(Material type, byte data, int amount, String name, List<String> lore) {
        return this.CreateStack(type.getId(), data, amount, (short)0, name, lore, null);
    }

    public ItemStack CreateStack(int id, byte data, int amount, String name, List<String> lore) {
        return this.CreateStack(id, data, amount, (short)0, name, lore, null);
    }

    public ItemStack CreateStack(Material type, byte data, int amount, String name, String[] lore) {
        return this.CreateStack(type.getId(), data, amount, (short)0, name, this.ArrayToList(lore), null);
    }

    public ItemStack CreateStack(int id, byte data, int amount, String name, String[] lore) {
        return this.CreateStack(id, data, amount, (short)0, name, this.ArrayToList(lore), null);
    }

    public ItemStack CreateStack(Material type, byte data, int amount, short damage, String name, String[] lore) {
        return this.CreateStack(type.getId(), data, amount, damage, name, this.ArrayToList(lore), null);
    }

    public ItemStack CreateStack(int id, byte data, int amount, short damage, String name, String[] lore) {
        return this.CreateStack(id, data, amount, damage, name, this.ArrayToList(lore), null);
    }

    public ItemStack CreateStack(Material type, byte data, int amount, short damage, String name, List<String> lore) {
        return this.CreateStack(type.getId(), data, amount, damage, name, lore, null);
    }

    public ItemStack CreateStack(Material type, byte data, int amount, String name, List<String> lore, String owner) {
        return this.CreateStack(type.getId(), data, amount, (short)0, name, lore, owner);
    }

    public ItemStack CreateStack(int id, byte data, int amount, String name, List<String> lore, String owner) {
        return this.CreateStack(id, data, amount, (short)0, name, lore, owner);
    }

    public ItemStack CreateStack(Material type, byte data, int amount, String name, String[] lore, String owner) {
        return this.CreateStack(type.getId(), data, amount, (short)0, name, this.ArrayToList(lore), owner);
    }

    public ItemStack CreateStack(int id, byte data, int amount, String name, String[] lore, String owner) {
        return this.CreateStack(id, data, amount, (short)0, name, this.ArrayToList(lore), owner);
    }

    public ItemStack CreateStack(Material type, byte data, int amount, short damage, String name, String[] lore, String owner) {
        return this.CreateStack(type.getId(), data, amount, damage, name, this.ArrayToList(lore), owner);
    }

    public ItemStack CreateStack(int id, byte data, int amount, short damage, String name, String[] lore, String owner) {
        return this.CreateStack(id, data, amount, damage, name, this.ArrayToList(lore), owner);
    }

    public ItemStack CreateStack(Material type, byte data, int amount, short damage, String name, List<String> lore, String owner) {
        return this.CreateStack(type.getId(), data, amount, damage, name, lore, owner);
    }

    public ItemStack CreateStack(int id, byte data, int amount, short damage, String name, List<String> lore, String owner) {
        ItemStack stack = data == 0 ? new ItemStack(id, amount, damage) : new ItemStack(id, amount, damage, Byte.valueOf(data));
        ItemMeta itemMeta = stack.getItemMeta();
        if (itemMeta == null) {
            return null;
        }
        boolean setMeta = false;
        if (name != null) {
            itemMeta.setDisplayName(name);
            setMeta = true;
        } else if (this._customNames) {
            itemMeta.setDisplayName(this.GetName(stack, true));
            setMeta = true;
        }
        if (this._lores != null && this._lores.containsKey(id) && this._lores.get(id).containsKey(data) && lore == null) {
            itemMeta.setLore(this.ArrayToList(this._lores.get(id).get(data)));
            setMeta = true;
        }
        if (owner != null) {
            String[] tokens = owner.split(" ");
            String[] ownerLore = new String[tokens.length + 2];
            ownerLore[0] = C.cGray + "Owner: " + C.cAqua + tokens[0];
            if (ownerLore.length >= 3) {
                ownerLore[1] = C.cGray + "Source: " + C.cAqua + tokens[1];
            }
            ownerLore[ownerLore.length - 2] = C.cGray + "Created: " + C.cAqua + UtilTime.date();
            ownerLore[ownerLore.length - 1] = "";
            if (itemMeta.getLore() != null) {
                itemMeta.setLore(this.CombineLore(itemMeta.getLore(), this.ArrayToList(ownerLore)));
            } else {
                itemMeta.setLore(this.ArrayToList(ownerLore));
            }
            setMeta = true;
        }
        if (lore != null) {
            if (itemMeta.getLore() != null) {
                itemMeta.setLore(this.CombineLore(itemMeta.getLore(), lore));
            } else {
                itemMeta.setLore(lore);
            }
            setMeta = true;
        }
        if (setMeta) {
            stack.setItemMeta(itemMeta);
        }
        return stack;
    }

    private List<String> CombineLore(List<String> A, List<String> B) {
        for (String b : B) {
            A.add(b);
        }
        return A;
    }

    public List<String> ArrayToList(String[] array) {
        if (array.length == 0) {
            return null;
        }
        ArrayList<String> list = new ArrayList<String>();
        for (String cur : array) {
            list.add(cur);
        }
        return list;
    }

    public String GetLoreVar(ItemStack stack, String var) {
        if (stack == null) {
            return null;
        }
        ItemMeta meta = stack.getItemMeta();
        if (meta == null) {
            return null;
        }
        if (meta.getLore() == null) {
            return null;
        }
        for (String cur : meta.getLore()) {
            if (!cur.contains(var)) continue;
            int index = var.split(" ").length;
            String[] tokens = cur.split(" ");
            String out = "";
            for (int i = index; i < tokens.length; ++i) {
                out = out + tokens[i] + " ";
            }
            if (out.length() > 0) {
                out = out.substring(0, out.length() - 1);
            }
            return out;
        }
        return null;
    }

    public int GetLoreVar(ItemStack stack, String var, int empty) {
        if (stack == null) {
            return empty;
        }
        ItemMeta meta = stack.getItemMeta();
        if (meta == null) {
            return 0;
        }
        if (meta.getLore() == null) {
            return 0;
        }
        for (String cur : meta.getLore()) {
            if (!cur.contains(var)) continue;
            String[] tokens = cur.split(" ");
            try {
                return Integer.parseInt(tokens[tokens.length - 1]);
            }
            catch (Exception e) {
                return empty;
            }
        }
        return 0;
    }

    public void SetLoreVar(ItemStack stack, String var, String value) {
        if (stack == null) {
            return;
        }
        ItemMeta meta = stack.getItemMeta();
        if (meta == null) {
            return;
        }
        ArrayList<String> newLore = new ArrayList<String>();
        boolean inserted = false;
        if (meta.getLore() != null) {
            for (String lore : meta.getLore()) {
                if (!lore.contains(var)) {
                    newLore.add(lore);
                    continue;
                }
                newLore.add(C.cGray + var + ":" + C.cGreen + " " + value);
                inserted = true;
            }
        }
        if (!inserted) {
            newLore.add(C.cGray + var + ":" + C.cGreen + " " + value);
        }
        meta.setLore(newLore);
        stack.setItemMeta(meta);
    }

    public void SetUseCustomNames(boolean var) {
        this._customNames = var;
    }

    public void SetCustomNameFormat(String format) {
        this._nameFormat = format;
    }

}
