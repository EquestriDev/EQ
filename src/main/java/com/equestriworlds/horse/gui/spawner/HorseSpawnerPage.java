/*
 * Decompiled with CFR 0_133.
 * 
 * Could not load the following classes:
 *  net.minecraft.server.v1_12_R1.AttributeInstance
 *  net.minecraft.server.v1_12_R1.EntityHorseAbstract
 *  net.minecraft.server.v1_12_R1.GenericAttributes
 *  net.minecraft.server.v1_12_R1.IAttribute
 *  org.bukkit.Location
 *  org.bukkit.Material
 *  org.bukkit.World
 *  org.bukkit.craftbukkit.v1_12_R1.entity.CraftAbstractHorse
 *  org.bukkit.entity.AbstractHorse
 *  org.bukkit.entity.Entity
 *  org.bukkit.entity.EntityType
 *  org.bukkit.entity.Horse
 *  org.bukkit.entity.Horse$Color
 *  org.bukkit.entity.Horse$Style
 *  org.bukkit.entity.Horse$Variant
 *  org.bukkit.entity.Llama
 *  org.bukkit.entity.Llama$Color
 *  org.bukkit.entity.Player
 *  org.bukkit.event.inventory.ClickType
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.inventory.meta.ItemMeta
 *  org.bukkit.inventory.meta.SpawnEggMeta
 */
package com.equestriworlds.horse.gui.spawner;

import com.equestriworlds.account.CoreClientManager;
import com.equestriworlds.horse.HorseManager;
import com.equestriworlds.horse.config.CustomHorse;
import com.equestriworlds.horse.config.Gender;
import com.equestriworlds.horse.config.HorseConfig;
import com.equestriworlds.horse.gui.spawner.HorseSpawnerMenu;
import com.equestriworlds.horse.gui.spawner.HorseSpawnerPageType;
import com.equestriworlds.itemstack.ItemLayout;
import com.equestriworlds.itemstack.ItemStackFactory;
import com.equestriworlds.menu.item.IButton;
import com.equestriworlds.menu.page.MenuPageBase;
import com.equestriworlds.util.C;
import com.equestriworlds.util.UtilMath;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import net.minecraft.server.v1_12_R1.AttributeInstance;
import net.minecraft.server.v1_12_R1.EntityHorseAbstract;
import net.minecraft.server.v1_12_R1.GenericAttributes;
import net.minecraft.server.v1_12_R1.IAttribute;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftAbstractHorse;
import org.bukkit.entity.AbstractHorse;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Horse;
import org.bukkit.entity.Llama;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SpawnEggMeta;

public class HorseSpawnerPage
extends MenuPageBase<HorseManager, HorseSpawnerMenu> {
    private HorseSpawnerPageType pageType;
    private Horse.Variant horseType;
    private Horse.Style style;
    private Horse.Color color;
    private Llama.Color llamaColor;
    private Double speed;
    private Double jump;
    private Gender gender;
    private String id;

    HorseSpawnerPage(HorseManager plugin, HorseSpawnerMenu shop, CoreClientManager clientManager, Player player, String id) {
        super(plugin, shop, clientManager, "Horse Spawner", player);
        this.id = id;
        this.pageType = HorseSpawnerPageType.TYPE;
        this.buildPage();
    }

    @Override
    protected void buildPage() {
        this.displayHorseType();
        this.displayProcess();
        this.displayHorseStyle();
        this.displayHorseColor();
        this.displayLlamaColor();
        this.displaySpeed();
        this.displayJump();
        this.displayGender();
        this.displaySpawn();
    }

    private void displayHorseType() {
        if (this.pageType.equals((Object)HorseSpawnerPageType.TYPE)) {
            int slot = 29;
            this.addButton(slot, ItemStackFactory.Instance.CreateStack(Material.LEATHER, (byte)0, 1, C.cYellow + C.Bold + "Horse"), (player, clickType) -> {
                this.horseType = Horse.Variant.HORSE;
                this.pageType = HorseSpawnerPageType.STYLE;
                this.refresh();
                this.playClickSound(player);
            });
            this.addButton(slot += 2, ItemStackFactory.Instance.CreateStack(Material.CHEST, (byte)0, 1, C.cYellow + C.Bold + "Donkey"), (player, clickType) -> {
                this.horseType = Horse.Variant.DONKEY;
                this.refresh();
                this.playClickSound(player);
            });
            this.addButton(slot += 2, ItemStackFactory.Instance.CreateStack(Material.RABBIT_HIDE, (byte)0, 1, C.cYellow + C.Bold + "Mule"), (player, clickType) -> {
                this.horseType = Horse.Variant.MULE;
                this.refresh();
                this.playClickSound(player);
            });
            this.addButton(slot += 14, ItemStackFactory.Instance.CreateStack(Material.WHEAT, (byte)0, 1, C.cYellow + C.Bold + "Llama"), (player, clickType) -> {
                this.horseType = Horse.Variant.LLAMA;
                this.pageType = HorseSpawnerPageType.LLAMACOLOR;
                this.refresh();
                this.playClickSound(player);
            });
            this.addButton(slot += 2, ItemStackFactory.Instance.CreateStack(Material.BONE, (byte)0, 1, C.cYellow + C.Bold + "Skeleton Horse"), (player, clickType) -> {
                this.horseType = Horse.Variant.SKELETON_HORSE;
                this.refresh();
                this.playClickSound(player);
            });
            this.addButton(slot += 2, ItemStackFactory.Instance.CreateStack(Material.ROTTEN_FLESH, (byte)0, 1, C.cYellow + C.Bold + "Zombie Horse"), (player, clickType) -> {
                this.horseType = Horse.Variant.UNDEAD_HORSE;
                this.refresh();
                this.playClickSound(player);
            });
        }
    }

    private void displayHorseStyle() {
        if (this.pageType.equals((Object)HorseSpawnerPageType.STYLE)) {
            int slot = 28;
            this.addButton(slot, this.egg(EntityType.BAT, C.cGreen + C.Bold + "Black Dots"), (player, clickType) -> {
                this.style = Horse.Style.BLACK_DOTS;
                this.pageType = HorseSpawnerPageType.COLOR;
                this.refresh();
                this.playClickSound(player);
            });
            this.addButton(slot += 2, this.egg(EntityType.GHAST, C.cGreen + C.Bold + "White"), (player, clickType) -> {
                this.style = Horse.Style.WHITE;
                this.pageType = HorseSpawnerPageType.COLOR;
                this.refresh();
                this.playClickSound(player);
            });
            this.addButton(slot += 2, this.egg(EntityType.SKELETON, C.cGreen + C.Bold + "Whitefield"), (player, clickType) -> {
                this.style = Horse.Style.WHITEFIELD;
                this.pageType = HorseSpawnerPageType.COLOR;
                this.refresh();
                this.playClickSound(player);
            });
            this.addButton(slot += 2, this.egg(EntityType.POLAR_BEAR, C.cGreen + C.Bold + "White Dots"), (player, clickType) -> {
                this.style = Horse.Style.WHITE_DOTS;
                this.pageType = HorseSpawnerPageType.COLOR;
                this.refresh();
                this.playClickSound(player);
            });
            this.addButton(slot += 15, this.egg(null, C.cGreen + C.Bold + "None"), (player, clickType) -> {
                this.style = Horse.Style.NONE;
                this.pageType = HorseSpawnerPageType.COLOR;
                this.refresh();
                this.playClickSound(player);
            });
        }
    }

    private void displayHorseColor() {
        if (this.pageType.equals((Object)HorseSpawnerPageType.COLOR)) {
            int slot = 28;
            this.addButton(slot, this.egg(EntityType.ENDERMAN, C.cGreen + C.Bold + "Black"), (player, clickType) -> {
                this.color = Horse.Color.BLACK;
                this.refresh();
                this.playClickSound(player);
            });
            this.addButton(slot += 2, this.egg(EntityType.RABBIT, C.cGreen + C.Bold + "Brown"), (player, clickType) -> {
                this.color = Horse.Color.BROWN;
                this.refresh();
                this.playClickSound(player);
            });
            this.addButton(slot += 2, this.egg(EntityType.OCELOT, C.cGreen + C.Bold + "Chestnut"), (player, clickType) -> {
                this.color = Horse.Color.CHESTNUT;
                this.refresh();
                this.playClickSound(player);
            });
            this.addButton(slot += 2, this.egg(EntityType.WOLF, C.cGreen + C.Bold + "Creamy"), (player, clickType) -> {
                this.color = Horse.Color.CREAMY;
                this.refresh();
                this.playClickSound(player);
            });
            this.addButton(slot += 13, this.egg(EntityType.VILLAGER, C.cGreen + C.Bold + "Dark Brown"), (player, clickType) -> {
                this.color = Horse.Color.DARK_BROWN;
                this.refresh();
                this.playClickSound(player);
            });
            this.addButton(slot += 2, this.egg(EntityType.SKELETON, C.cGreen + C.Bold + "Gray"), (player, clickType) -> {
                this.color = Horse.Color.GRAY;
                this.refresh();
                this.playClickSound(player);
            });
            this.addButton(slot += 2, this.egg(EntityType.GHAST, C.cGreen + C.Bold + "White"), (player, clickType) -> {
                this.color = Horse.Color.WHITE;
                this.refresh();
                this.playClickSound(player);
            });
        }
    }

    private void displayLlamaColor() {
        if (this.pageType.equals((Object)HorseSpawnerPageType.LLAMACOLOR)) {
            int slot = 37;
            this.addButton(slot, this.egg(EntityType.POLAR_BEAR, C.cGreen + C.Bold + "White"), (player, clickType) -> {
                this.llamaColor = Llama.Color.WHITE;
                this.refresh();
                this.playClickSound(player);
            });
            this.addButton(slot += 2, this.egg(EntityType.RABBIT, C.cGreen + C.Bold + "Brown"), (player, clickType) -> {
                this.llamaColor = Llama.Color.BROWN;
                this.refresh();
                this.playClickSound(player);
            });
            this.addButton(slot += 2, this.egg(EntityType.SKELETON, C.cGreen + C.Bold + "Gray"), (player, clickType) -> {
                this.llamaColor = Llama.Color.GRAY;
                this.refresh();
                this.playClickSound(player);
            });
            this.addButton(slot += 2, this.egg(EntityType.SHEEP, C.cGreen + C.Bold + "Creamy"), (player, clickType) -> {
                this.llamaColor = Llama.Color.CREAMY;
                this.refresh();
                this.playClickSound(player);
            });
        }
    }

    private void displayGender() {
        if (this.pageType.equals((Object)HorseSpawnerPageType.GENDER)) {
            int slot = 29;
            this.addButton(slot, ItemStackFactory.Instance.CreateStack(Material.INK_SACK, (byte)12, 1, C.cGreen + C.Bold + "Stallion"), (player, clickType) -> {
                this.gender = Gender.STALLION;
                this.refresh();
                this.playClickSound(player);
            });
            this.addButton(slot += 2, ItemStackFactory.Instance.CreateStack(Material.INK_SACK, (byte)10, 1, C.cGreen + C.Bold + "Gelding"), (player, clickType) -> {
                this.gender = Gender.GELDING;
                this.refresh();
                this.playClickSound(player);
            });
            this.addButton(slot += 2, ItemStackFactory.Instance.CreateStack(Material.INK_SACK, (byte)9, 1, C.cGreen + C.Bold + "Mare"), (player, clickType) -> {
                this.gender = Gender.MARE;
                this.refresh();
                this.playClickSound(player);
            });
            this.addButton(slot += 16, ItemStackFactory.Instance.CreateStack(Material.NETHER_STAR, (byte)0, 1, C.cGreen + C.Bold + "Natural", new String[]{C.cWhite + "This option will randomly", C.cWhite + "choose the gender between", C.cWhite + "Stallion and Mare."}), (player, clickType) -> {
                this.gender = null;
                this.refresh();
                this.playClickSound(player);
            });
        }
    }

    private void displaySpawn() {
        if (this.horseType == null) {
            return;
        }
        if (!this.spawnable()) {
            this.addItem(8, ItemStackFactory.Instance.CreateStack(Material.REDSTONE_BLOCK, (byte)0, 1, C.cRed + C.Bold + "Cannot spawn entity", this.lore()));
        } else {
            this.addButton(8, ItemStackFactory.Instance.CreateStack(Material.EMERALD_BLOCK, (byte)0, 1, C.cGreen + C.Bold + "Spawn " + (this.id == null ? "" : "and claim ") + "the horse.", this.lore()), this.spawn());
        }
    }

    private void displaySpeed() {
        if (this.pageType.equals((Object)HorseSpawnerPageType.SPEED)) {
            ArrayList<Integer> layout = new ItemLayout("XXXXXXXXX", "XXXXXXXXX", "XXXXXXXXX", "OOOXXXOOO", "OOOXOXOOO", "OOOXXXOOO").getItemSlots();
            int index = 0;
            this.addButton(layout.get(index), this.numberItem(-5.0), this.speed(-5.0));
            this.addButton(layout.get(++index), this.numberItem(-0.5), this.speed(-0.5));
            this.addButton(layout.get(++index), this.numberItem(-0.05), this.speed(-0.05));
            this.addButton(layout.get(++index), this.numberItem(5.0), this.speed(5.0));
            this.addButton(layout.get(++index), this.numberItem(0.5), this.speed(0.5));
            this.addButton(layout.get(++index), this.numberItem(0.05), this.speed(0.05));
            this.addButton(layout.get(++index), this.numberItem(-2.0), this.speed(-2.0));
            this.addButton(layout.get(++index), this.numberItem(-0.2), this.speed(-0.2));
            this.addButton(layout.get(++index), this.numberItem(-0.02), this.speed(-0.02));
            this.addButton(layout.get(++index), ItemStackFactory.Instance.CreateStack(Material.NETHER_STAR, (byte)0, 1, C.cYellow + C.Bold + "Minecraft Default", new String[]{C.cWhite + "This option will use", C.cWhite + "the vanilla speed"}), (player, clickType) -> {
                this.speed = null;
                this.refresh();
                this.playClickSound(player);
            });
            this.addButton(layout.get(++index), this.numberItem(2.0), this.speed(2.0));
            this.addButton(layout.get(++index), this.numberItem(0.2), this.speed(0.2));
            this.addButton(layout.get(++index), this.numberItem(0.02), this.speed(0.02));
            this.addButton(layout.get(++index), this.numberItem(-1.0), this.speed(-1.0));
            this.addButton(layout.get(++index), this.numberItem(-0.1), this.speed(-0.1));
            this.addButton(layout.get(++index), this.numberItem(-0.01), this.speed(-0.01));
            this.addButton(layout.get(++index), this.numberItem(1.0), this.speed(1.0));
            this.addButton(layout.get(++index), this.numberItem(0.1), this.speed(0.1));
            this.addButton(layout.get(++index), this.numberItem(0.01), this.speed(0.01));
            ++index;
        }
    }

    private void displayJump() {
        if (this.pageType.equals((Object)HorseSpawnerPageType.JUMP)) {
            ArrayList<Integer> layout = new ItemLayout("XXXXXXXXX", "XXXXXXXXX", "XXXXXXXXX", "OOOXXXOOO", "OOOXOXOOO", "OOOXXXOOO").getItemSlots();
            int index = 0;
            this.addButton(layout.get(index), this.numberItem(-5.0), this.jump(-5.0));
            this.addButton(layout.get(++index), this.numberItem(-0.5), this.jump(-0.5));
            this.addButton(layout.get(++index), this.numberItem(-0.05), this.jump(-0.05));
            this.addButton(layout.get(++index), this.numberItem(5.0), this.jump(5.0));
            this.addButton(layout.get(++index), this.numberItem(0.5), this.jump(0.5));
            this.addButton(layout.get(++index), this.numberItem(0.05), this.jump(0.05));
            this.addButton(layout.get(++index), this.numberItem(-2.0), this.jump(-2.0));
            this.addButton(layout.get(++index), this.numberItem(-0.2), this.jump(-0.2));
            this.addButton(layout.get(++index), this.numberItem(-0.02), this.jump(-0.02));
            this.addButton(layout.get(++index), ItemStackFactory.Instance.CreateStack(Material.NETHER_STAR, (byte)0, 1, C.cYellow + C.Bold + "Minecraft Default", new String[]{C.cWhite + "This option will use", C.cWhite + "the vanilla jump height"}), (player, clickType) -> {
                this.jump = null;
                this.refresh();
                this.playClickSound(player);
            });
            this.addButton(layout.get(++index), this.numberItem(2.0), this.jump(2.0));
            this.addButton(layout.get(++index), this.numberItem(0.2), this.jump(0.2));
            this.addButton(layout.get(++index), this.numberItem(0.02), this.jump(0.02));
            this.addButton(layout.get(++index), this.numberItem(-1.0), this.jump(-1.0));
            this.addButton(layout.get(++index), this.numberItem(-0.1), this.jump(-0.1));
            this.addButton(layout.get(++index), this.numberItem(-0.01), this.jump(-0.01));
            this.addButton(layout.get(++index), this.numberItem(1.0), this.jump(1.0));
            this.addButton(layout.get(++index), this.numberItem(0.1), this.jump(0.1));
            this.addButton(layout.get(++index), this.numberItem(0.01), this.jump(0.01));
            ++index;
        }
    }

    private void displayProcess() {
        int index;
        ArrayList<Integer> layout;
        String[] arrstring = new String[3];
        arrstring[0] = this.horseType == null ? C.cWhite + "Not set" : C.cWhite + this.horseType.toString();
        arrstring[1] = " ";
        arrstring[2] = C.cGreen + "Click to change";
        this.addButton(4, ItemStackFactory.Instance.CreateStack(Material.STAINED_GLASS_PANE, this.data((Object)this.horseType, false), 1, C.cYellow + C.Bold + "Horse Type", arrstring), (player, clickType) -> {
            this.pageType = HorseSpawnerPageType.TYPE;
            this.refresh();
            this.playClickSound(player);
        });
        if (this.horseType == null) {
            return;
        }
        if (this.horseType.equals((Object)Horse.Variant.HORSE)) {
            layout = new ItemLayout("XXXXXXXXX", "XOOXOOXOX").getItemSlots();
            index = 0;
            String[] arrstring2 = new String[3];
            arrstring2[0] = this.style == null ? C.cWhite + "Not set" : C.cWhite + this.style.toString();
            arrstring2[1] = " ";
            arrstring2[2] = C.cGreen + "Click to change";
            this.addButton(layout.get(index), ItemStackFactory.Instance.CreateStack(Material.STAINED_GLASS_PANE, this.data((Object)this.style, false), 1, C.cYellow + C.Bold + "Horse Style", arrstring2), (player, clickType) -> {
                this.pageType = HorseSpawnerPageType.STYLE;
                this.refresh();
                this.playClickSound(player);
            });
            String[] arrstring3 = new String[3];
            arrstring3[0] = this.color == null ? C.cWhite + "Not set" : C.cWhite + this.color.toString();
            arrstring3[1] = " ";
            arrstring3[2] = C.cGreen + "Click to change";
            this.addButton(layout.get(++index), ItemStackFactory.Instance.CreateStack(Material.STAINED_GLASS_PANE, this.data((Object)this.color, false), 1, C.cYellow + C.Bold + "Horse Color", arrstring3), (player, clickType) -> {
                this.pageType = HorseSpawnerPageType.COLOR;
                this.refresh();
                this.playClickSound(player);
            });
            String[] arrstring4 = new String[3];
            arrstring4[0] = this.speed == null ? C.cWhite + "Defaulted to Minecraft" : C.cWhite + UtilMath.trim(2, this.speed);
            arrstring4[1] = " ";
            arrstring4[2] = C.cGreen + "Click to change";
            this.addButton(layout.get(++index), ItemStackFactory.Instance.CreateStack(Material.STAINED_GLASS_PANE, this.data(this.speed, true), 1, C.cYellow + C.Bold + "Horse Speed", arrstring4), (player, clickType) -> {
                this.pageType = HorseSpawnerPageType.SPEED;
                this.refresh();
                this.playClickSound(player);
            });
            String[] arrstring5 = new String[3];
            arrstring5[0] = this.jump == null ? C.cWhite + "Defaulted to Minecraft" : C.cWhite + UtilMath.trim(2, this.jump);
            arrstring5[1] = " ";
            arrstring5[2] = C.cGreen + "Click to change";
            this.addButton(layout.get(++index), ItemStackFactory.Instance.CreateStack(Material.STAINED_GLASS_PANE, this.data(this.jump, true), 1, C.cYellow + C.Bold + "Horse Jump", arrstring5), (player, clickType) -> {
                this.pageType = HorseSpawnerPageType.JUMP;
                this.refresh();
                this.playClickSound(player);
            });
            String[] arrstring6 = new String[3];
            arrstring6[0] = this.gender == null ? C.cWhite + "Random" : C.cWhite + this.gender.name;
            arrstring6[1] = " ";
            arrstring6[2] = C.cGreen + "Click to change";
            this.addButton(layout.get(++index), ItemStackFactory.Instance.CreateStack(Material.STAINED_GLASS_PANE, this.data((Object)this.gender, true), 1, C.cYellow + C.Bold + "Horse Gender", arrstring6), (player, clickType) -> {
                this.pageType = HorseSpawnerPageType.GENDER;
                this.refresh();
                this.playClickSound(player);
            });
            ++index;
        }
        if (this.horseType.equals((Object)Horse.Variant.DONKEY) || this.horseType.equals((Object)Horse.Variant.MULE) || this.horseType.equals((Object)Horse.Variant.SKELETON_HORSE) || this.horseType.equals((Object)Horse.Variant.UNDEAD_HORSE)) {
            layout = new ItemLayout("XXXXXXXXX", "XXOXOXOXX").getItemSlots();
            index = 0;
            String[] arrstring7 = new String[3];
            arrstring7[0] = this.speed == null ? C.cWhite + "Defaulted to Minecraft" : C.cWhite + UtilMath.trim(2, this.speed);
            arrstring7[1] = " ";
            arrstring7[2] = C.cGreen + "Click to change";
            this.addButton(layout.get(index), ItemStackFactory.Instance.CreateStack(Material.STAINED_GLASS_PANE, this.data(this.speed, true), 1, C.cYellow + C.Bold + "Horse Speed", arrstring7), (player, clickType) -> {
                this.pageType = HorseSpawnerPageType.SPEED;
                this.refresh();
                this.playClickSound(player);
            });
            String[] arrstring8 = new String[3];
            arrstring8[0] = this.jump == null ? C.cWhite + "Defaulted to Minecraft" : C.cWhite + UtilMath.trim(2, this.jump);
            arrstring8[1] = " ";
            arrstring8[2] = C.cGreen + "Click to change";
            this.addButton(layout.get(++index), ItemStackFactory.Instance.CreateStack(Material.STAINED_GLASS_PANE, this.data(this.jump, true), 1, C.cYellow + C.Bold + "Horse Jump", arrstring8), (player, clickType) -> {
                this.pageType = HorseSpawnerPageType.JUMP;
                this.refresh();
                this.playClickSound(player);
            });
            String[] arrstring9 = new String[3];
            arrstring9[0] = this.gender == null ? C.cWhite + "Random" : C.cWhite + this.gender.name;
            arrstring9[1] = " ";
            arrstring9[2] = C.cGreen + "Click to change";
            this.addButton(layout.get(++index), ItemStackFactory.Instance.CreateStack(Material.STAINED_GLASS_PANE, this.data((Object)this.gender, true), 1, C.cYellow + C.Bold + "Horse Gender", arrstring9), (player, clickType) -> {
                this.pageType = HorseSpawnerPageType.GENDER;
                this.refresh();
                this.playClickSound(player);
            });
            ++index;
        }
        if (this.horseType.equals((Object)Horse.Variant.LLAMA)) {
            layout = new ItemLayout("XXXXXXXXX", "XOXOXOXOX").getItemSlots();
            index = 0;
            String[] arrstring10 = new String[3];
            arrstring10[0] = this.llamaColor == null ? C.cWhite + "Not set" : C.cWhite + this.llamaColor.toString();
            arrstring10[1] = " ";
            arrstring10[2] = C.cGreen + "Click to change";
            this.addButton(layout.get(index), ItemStackFactory.Instance.CreateStack(Material.STAINED_GLASS_PANE, this.data((Object)this.llamaColor, false), 1, C.cYellow + C.Bold + "Llama Color", arrstring10), (player, clickType) -> {
                this.pageType = HorseSpawnerPageType.LLAMACOLOR;
                this.refresh();
                this.playClickSound(player);
            });
            String[] arrstring11 = new String[3];
            arrstring11[0] = this.speed == null ? C.cWhite + "Defaulted to Minecraft" : C.cWhite + UtilMath.trim(2, this.speed);
            arrstring11[1] = " ";
            arrstring11[2] = C.cGreen + "Click to change";
            this.addButton(layout.get(++index), ItemStackFactory.Instance.CreateStack(Material.STAINED_GLASS_PANE, this.data(this.speed, true), 1, C.cYellow + C.Bold + "Llama Speed", arrstring11), (player, clickType) -> {
                this.pageType = HorseSpawnerPageType.SPEED;
                this.refresh();
                this.playClickSound(player);
            });
            String[] arrstring12 = new String[3];
            arrstring12[0] = this.jump == null ? C.cWhite + "Defaulted to Minecraft" : C.cWhite + UtilMath.trim(2, this.jump);
            arrstring12[1] = " ";
            arrstring12[2] = C.cGreen + "Click to change";
            this.addButton(layout.get(++index), ItemStackFactory.Instance.CreateStack(Material.STAINED_GLASS_PANE, this.data(this.jump, true), 1, C.cYellow + C.Bold + "Llama Jump", arrstring12), (player, clickType) -> {
                this.pageType = HorseSpawnerPageType.JUMP;
                this.refresh();
                this.playClickSound(player);
            });
            String[] arrstring13 = new String[3];
            arrstring13[0] = this.gender == null ? C.cWhite + "Natural" : C.cWhite + this.gender.name;
            arrstring13[1] = " ";
            arrstring13[2] = C.cGreen + "Click to change";
            this.addButton(layout.get(++index), ItemStackFactory.Instance.CreateStack(Material.STAINED_GLASS_PANE, this.data((Object)this.gender, true), 1, C.cYellow + C.Bold + "Llama Gender", arrstring13), (player, clickType) -> {
                this.pageType = HorseSpawnerPageType.GENDER;
                this.refresh();
                this.playClickSound(player);
            });
            ++index;
        }
    }

    public byte data(Double d, boolean nullable) {
        if (d == null) {
            if (nullable) {
                return 4;
            }
            return 14;
        }
        return 5;
    }

    public byte data(Object d, boolean nullable) {
        if (d == null) {
            if (nullable) {
                return 4;
            }
            return 14;
        }
        return 5;
    }

    private ItemStack egg(EntityType entity, String title) {
        ItemStack stack = ItemStackFactory.Instance.CreateStack(Material.MONSTER_EGG, (byte)0, 1, title);
        if (entity != null) {
            SpawnEggMeta meta = (SpawnEggMeta)stack.getItemMeta();
            meta.setSpawnedType(entity);
            stack.setItemMeta((ItemMeta)meta);
        }
        return stack;
    }

    private boolean spawnable() {
        if (this.horseType.equals((Object)Horse.Variant.HORSE)) {
            if (this.color == null || this.style == null) {
                return false;
            }
        } else if (this.horseType.equals((Object)Horse.Variant.LLAMA)) {
            if (this.llamaColor == null) {
                return false;
            }
        } else {
            return true;
        }
        return true;
    }

    private List<String> lore() {
        ArrayList<String> lore = new ArrayList<String>();
        lore.add(" ");
        if (this.spawnable()) {
            if (this.id != null) {
                lore.add(C.cYellow + "ID: " + C.cWhite + this.id);
                lore.add(" ");
            }
            if (this.horseType.equals((Object)Horse.Variant.HORSE)) {
                lore.add(C.cYellow + "Style: " + C.cWhite + this.style.toString());
                lore.add(C.cYellow + "Color: " + C.cWhite + this.color.toString());
                lore.add(" ");
            }
            if (this.horseType.equals((Object)Horse.Variant.LLAMA)) {
                lore.add(C.cYellow + "Color: " + C.cWhite + this.llamaColor.toString());
                lore.add(" ");
            }
            lore.add(C.cYellow + "Speed: " + C.cWhite + (this.speed == null ? "Default" : Double.valueOf(UtilMath.trim(2, this.speed))));
            lore.add(C.cYellow + "Jump: " + C.cWhite + (this.jump == null ? "Default" : Double.valueOf(UtilMath.trim(2, this.jump))));
            lore.add(" ");
            lore.add(C.cYellow + "Gender: " + C.cWhite + (this.gender == null ? "Natural" : this.gender.name));
        } else if (this.horseType.equals((Object)Horse.Variant.HORSE)) {
            if (this.style == null) {
                lore.add(C.cWhite + "Style is not set");
            }
            if (this.color == null) {
                lore.add(C.cWhite + "Color is not set");
            }
        } else if (this.horseType.equals((Object)Horse.Variant.LLAMA) && this.llamaColor == null) {
            lore.add(C.cWhite + "Color is not set");
        }
        return lore;
    }

    public IButton spawn() {
        return (player, clickType) -> {
            AbstractHorse horse = null;
            if (this.horseType.equals((Object)Horse.Variant.HORSE)) {
                horse = (AbstractHorse)player.getWorld().spawnEntity(player.getLocation(), EntityType.HORSE);
                ((Horse)horse).setStyle(this.style);
                ((Horse)horse).setColor(this.color);
            } else if (this.horseType.equals((Object)Horse.Variant.LLAMA)) {
                horse = (AbstractHorse)player.getWorld().spawnEntity(player.getLocation(), EntityType.LLAMA);
                ((Llama)horse).setColor(this.llamaColor);
            } else if (this.horseType.equals((Object)Horse.Variant.DONKEY)) {
                horse = (AbstractHorse)player.getWorld().spawnEntity(player.getLocation(), EntityType.DONKEY);
            } else if (this.horseType.equals((Object)Horse.Variant.MULE)) {
                horse = (AbstractHorse)player.getWorld().spawnEntity(player.getLocation(), EntityType.MULE);
            } else if (this.horseType.equals((Object)Horse.Variant.SKELETON_HORSE)) {
                horse = (AbstractHorse)player.getWorld().spawnEntity(player.getLocation(), EntityType.SKELETON_HORSE);
            } else if (this.horseType.equals((Object)Horse.Variant.UNDEAD_HORSE)) {
                horse = (AbstractHorse)player.getWorld().spawnEntity(player.getLocation(), EntityType.ZOMBIE_HORSE);
            }
            if (this.speed != null) {
                ((CraftAbstractHorse)horse).getHandle().getAttributeInstance(GenericAttributes.MOVEMENT_SPEED).setValue(this.speed.doubleValue());
            }
            if (this.jump != null) {
                horse.setJumpStrength(this.jump.doubleValue());
            }
            horse.setAdult();
            if (this.id != null) {
                ((HorseManager)this.getPlugin()).claim(player, horse, this.id);
                CustomHorse customHorse = ((HorseManager)this.getPlugin()).config.getHorseById(this.id);
                customHorse.changeGender(this.gender == null ? Gender.values()[new Random().nextInt(2)] : this.gender);
                if (this.speed == null) {
                    customHorse.setSpeed(0.38);
                }
                if (this.jump == null) {
                    customHorse.setJump(0.79);
                }
            } else {
                String[] arrstring = new String[3];
                arrstring[0] = String.valueOf((Object)(this.gender == null ? Gender.values()[new Random().nextInt(2)] : this.gender));
                arrstring[1] = this.speed == null ? String.valueOf(0.38) : String.valueOf(this.speed);
                arrstring[2] = this.jump == null ? String.valueOf(0.79) : String.valueOf(this.jump);
                ((HorseManager)this.getPlugin()).newGender.put((Entity)horse, arrstring);
            }
            player.closeInventory();
        };
    }

    public IButton speed(Double difference) {
        return (player, clickType) -> {
            if (clickType == ClickType.DOUBLE_CLICK) {
                return;
            }
            if (this.speed == null) {
                this.speed = 0.0;
            }
            this.speed = this.speed + UtilMath.trim(2, difference);
            this.refresh();
            this.playClickSound(player);
        };
    }

    private ItemStack numberItem(Double difference) {
        return ItemStackFactory.Instance.CreateStack(Material.INK_SACK, (byte)(difference > 0.0 ? 10 : 1), 1, difference > 0.0 ? C.cGreen + C.Bold + "Add " + UtilMath.trim(2, difference) : C.cRed + C.Bold + "Remove " + UtilMath.trim(2, difference));
    }

    public IButton jump(Double difference) {
        return (player, clickType) -> {
            if (clickType == ClickType.DOUBLE_CLICK) {
                return;
            }
            if (this.jump == null) {
                this.jump = 0.0;
            }
            this.jump = this.jump + UtilMath.trim(2, difference);
            this.refresh();
            this.playClickSound(player);
        };
    }
}
