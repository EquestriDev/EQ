/*
 * Decompiled with CFR 0_133.
 * 
 * Could not load the following classes:
 *  org.bukkit.Material
 *  org.bukkit.entity.Player
 *  org.bukkit.event.inventory.ClickType
 *  org.bukkit.inventory.ItemStack
 */
package com.equestriworlds.vet.surgery;

import com.equestriworlds.account.CoreClientManager;
import com.equestriworlds.horse.HorseManager;
import com.equestriworlds.itemstack.ItemLayout;
import com.equestriworlds.itemstack.ItemStackFactory;
import com.equestriworlds.menu.item.IButton;
import com.equestriworlds.menu.page.MenuPageBase;
import com.equestriworlds.util.C;
import com.equestriworlds.vet.surgery.SurgeryMenu;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

public class SurgeryPage
extends MenuPageBase<HorseManager, SurgeryMenu> {
    private Question menuType = Question.One;
    private int question = 1;
    private List<Integer> layout = new ItemLayout("xxxxoxxxx", "xoxxoxxox").getItemSlots();

    SurgeryPage(HorseManager plugin, SurgeryMenu shop, CoreClientManager clientManager, String name, Player player, int i) {
        super(plugin, shop, clientManager, name, player, i);
        this.buildPage();
    }

    @Override
    protected void buildPage() {
        this.buildSectionChoice();
        switch (this.menuType) {
            case One: {
                this.buildPage1();
                break;
            }
            case Two: {
                this.buildPage2();
                break;
            }
            case Three: {
                this.buildPage3();
                break;
            }
            case Four: {
                this.buildPage4();
                break;
            }
            case Five: {
                this.buildPage5();
                break;
            }
            case Six: {
                this.buildPage6();
                break;
            }
            case Seven: {
                this.buildPage7();
                break;
            }
            case Eight: {
                this.buildPage8();
                break;
            }
            case Nine: {
                this.buildPage9();
                break;
            }
            case Ten: {
                this.buildPage10();
                break;
            }
            case Eleven: {
                this.buildPage11();
                break;
            }
            case Twelve: {
                this.buildPage12();
                break;
            }
            case Thirteen: {
                this.buildPage13();
                break;
            }
            case Fourteen: {
                this.buildPage14();
                break;
            }
            case Fifteen: {
                this.buildPage15();
            }
        }
    }

    private void buildSectionChoice() {
        this.addItem(this.layout.get(0), ItemStackFactory.Instance.CreateStack(Material.BOOK, (byte)0, 1, C.cAqua + C.Bold + "Question " + this.question + " \u00bb", new String[0]));
    }

    private void buildPage1() {
        this.addButton(this.layout.get(1), this.icon("Option 1 \u00bb", "Make an incision.", 1), (player, clickType) -> {
            this.playClickSound(player);
            this.menuType = Question.Two;
            ++this.question;
            this.refresh();
        });
        this.addButton(this.layout.get(2), this.icon("Option 2 \u00bb", "Clip hair around area.", 2), (player, clickType) -> {
            this.playClickSound(player);
            this.menuType = Question.Two;
            ++this.question;
            this.refresh();
        });
        this.addButton(this.layout.get(3), this.icon("Option 3 \u00bb", "Disinfect area.", 3), (player, clickType) -> {
            this.playClickSound(player);
            this.menuType = Question.Two;
            ++this.question;
            this.refresh();
        });
    }

    private void buildPage2() {
        this.addButton(this.layout.get(1), this.icon("Option 1 \u00bb", "Disinfect area.", 1), (player, clickType) -> {
            this.playClickSound(player);
            this.menuType = Question.Three;
            ++this.question;
            this.refresh();
        });
        this.addButton(this.layout.get(2), this.icon("Option 2 \u00bb", "Make an incision.", 2), (player, clickType) -> {
            this.playClickSound(player);
            this.menuType = Question.Three;
            ++this.question;
            this.refresh();
        });
        this.addButton(this.layout.get(3), this.icon("Option 3 \u00bb", "Keep pressure on area.", 3), (player, clickType) -> {
            this.playClickSound(player);
            this.menuType = Question.Three;
            ++this.question;
            this.refresh();
        });
    }

    private void buildPage3() {
        this.addButton(this.layout.get(1), this.icon("Option 1 \u00bb", "Mark where to make the incision.", 1), (player, clickType) -> {
            this.playClickSound(player);
            this.menuType = Question.Four;
            ++this.question;
            this.refresh();
        });
        this.addButton(this.layout.get(2), this.icon("Option 2 \u00bb", "Check heart rate.", 2), (player, clickType) -> {
            this.playClickSound(player);
            this.menuType = Question.Four;
            ++this.question;
            this.refresh();
        });
        this.addButton(this.layout.get(3), this.icon("Option 3 \u00bb", "Insert titanium plate.", 3), (player, clickType) -> {
            this.playClickSound(player);
            this.menuType = Question.Four;
            ++this.question;
            this.refresh();
        });
    }

    private void buildPage4() {
        this.addButton(this.layout.get(1), this.icon("Option 1 \u00bb", "Insert titanium plate.", 1), (player, clickType) -> {
            this.playClickSound(player);
            this.menuType = Question.Five;
            ++this.question;
            this.refresh();
        });
        this.addButton(this.layout.get(2), this.icon("Option 2 \u00bb", "Check heart rate.", 2), (player, clickType) -> {
            this.playClickSound(player);
            this.menuType = Question.Five;
            ++this.question;
            this.refresh();
        });
        this.addButton(this.layout.get(3), this.icon("Option 3 \u00bb", "Make an incision.", 3), (player, clickType) -> {
            this.playClickSound(player);
            this.menuType = Question.Five;
            ++this.question;
            this.refresh();
        });
    }

    private void buildPage5() {
        this.addButton(this.layout.get(1), this.icon("Option 1 \u00bb", "Make an incision.", 1), (player, clickType) -> {
            this.playClickSound(player);
            this.menuType = Question.Six;
            ++this.question;
            this.refresh();
        });
        this.addButton(this.layout.get(2), this.icon("Option 2 \u00bb", "Remove bone fragments.", 2), (player, clickType) -> {
            this.playClickSound(player);
            this.menuType = Question.Six;
            ++this.question;
            this.refresh();
        });
        this.addButton(this.layout.get(3), this.icon("Option 3 \u00bb", "X-Ray the area.", 3), (player, clickType) -> {
            this.playClickSound(player);
            this.menuType = Question.Six;
            ++this.question;
            this.refresh();
        });
    }

    private void buildPage6() {
        this.addButton(this.layout.get(1), this.icon("Option 1 \u00bb", "Stitch the incision.", 1), (player, clickType) -> {
            this.playClickSound(player);
            this.menuType = Question.Seven;
            ++this.question;
            this.refresh();
        });
        this.addButton(this.layout.get(2), this.icon("Option 2 \u00bb", "Insert titanium plates.", 2), (player, clickType) -> {
            this.playClickSound(player);
            this.menuType = Question.Seven;
            ++this.question;
            this.refresh();
        });
        this.addButton(this.layout.get(3), this.icon("Option 3 \u00bb", "Add a tourniquet to stop bleeding.", 3), (player, clickType) -> {
            this.playClickSound(player);
            this.menuType = Question.Seven;
            ++this.question;
            this.refresh();
        });
    }

    private void buildPage7() {
        this.addButton(this.layout.get(1), this.icon("Option 1 \u00bb", "Remove bone fragments.", 1), (player, clickType) -> {
            this.playClickSound(player);
            this.menuType = Question.Eight;
            ++this.question;
            this.refresh();
        });
        this.addButton(this.layout.get(2), this.icon("Option 2 \u00bb", "X-Ray the area.", 2), (player, clickType) -> {
            this.playClickSound(player);
            this.menuType = Question.Eight;
            ++this.question;
            this.refresh();
        });
        this.addButton(this.layout.get(3), this.icon("Option 3 \u00bb", "Drill holes in the bone.", 3), (player, clickType) -> {
            this.playClickSound(player);
            this.menuType = Question.Eight;
            ++this.question;
            this.refresh();
        });
    }

    private void buildPage8() {
        this.addButton(this.layout.get(1), this.icon("Option 1 \u00bb", "X-Ray the area.", 1), (player, clickType) -> {
            this.playClickSound(player);
            this.menuType = Question.Nine;
            ++this.question;
            this.refresh();
        });
        this.addButton(this.layout.get(2), this.icon("Option 2 \u00bb", "Stitch the incision.", 2), (player, clickType) -> {
            this.playClickSound(player);
            this.menuType = Question.Nine;
            ++this.question;
            this.refresh();
        });
        this.addButton(this.layout.get(3), this.icon("Option 3 \u00bb", "Reposition the bone.", 3), (player, clickType) -> {
            this.playClickSound(player);
            this.menuType = Question.Nine;
            ++this.question;
            this.refresh();
        });
    }

    private void buildPage9() {
        this.addButton(this.layout.get(1), this.icon("Option 1 \u00bb", "Insert titanium plates.", 1), (player, clickType) -> {
            this.playClickSound(player);
            this.menuType = Question.Ten;
            ++this.question;
            this.refresh();
        });
        this.addButton(this.layout.get(2), this.icon("Option 2 \u00bb", "Disinfect open wound.", 2), (player, clickType) -> {
            this.playClickSound(player);
            this.menuType = Question.Ten;
            ++this.question;
            this.refresh();
        });
        this.addButton(this.layout.get(3), this.icon("Option 3 \u00bb", "Stitch the incision.", 3), (player, clickType) -> {
            this.playClickSound(player);
            this.menuType = Question.Ten;
            ++this.question;
            this.refresh();
        });
    }

    private void buildPage10() {
        this.addButton(this.layout.get(1), this.icon("Option 1 \u00bb", "Stitch the incision.", 1), (player, clickType) -> {
            this.playClickSound(player);
            this.menuType = Question.Eleven;
            ++this.question;
            this.refresh();
        });
        this.addButton(this.layout.get(2), this.icon("Option 2 \u00bb", "Fasten titanium plate with screws.", 2), (player, clickType) -> {
            this.playClickSound(player);
            this.menuType = Question.Eleven;
            ++this.question;
            this.refresh();
        });
        this.addButton(this.layout.get(3), this.icon("Option 3 \u00bb", "X-Ray the area.", 3), (player, clickType) -> {
            this.playClickSound(player);
            this.menuType = Question.Eleven;
            ++this.question;
            this.refresh();
        });
    }

    private void buildPage11() {
        this.addButton(this.layout.get(1), this.icon("Option 1 \u00bb", "Flush area with Saline Solution.", 1), (player, clickType) -> {
            this.playClickSound(player);
            this.menuType = Question.Twelve;
            ++this.question;
            this.refresh();
        });
        this.addButton(this.layout.get(2), this.icon("Option 2 \u00bb", "Add another tourniquet.", 2), (player, clickType) -> {
            this.playClickSound(player);
            this.menuType = Question.Twelve;
            ++this.question;
            this.refresh();
        });
        this.addButton(this.layout.get(3), this.icon("Option 3 \u00bb", "Stitch the incision.", 3), (player, clickType) -> {
            this.playClickSound(player);
            this.menuType = Question.Twelve;
            ++this.question;
            this.refresh();
        });
    }

    private void buildPage12() {
        this.addButton(this.layout.get(1), this.icon("Option 1 \u00bb", "Flush area with betadine.", 1), (player, clickType) -> {
            this.playClickSound(player);
            this.menuType = Question.Thirteen;
            ++this.question;
            this.refresh();
        });
        this.addButton(this.layout.get(2), this.icon("Option 2 \u00bb", "Inject more ketamine.", 2), (player, clickType) -> {
            this.playClickSound(player);
            this.menuType = Question.Thirteen;
            ++this.question;
            this.refresh();
        });
        this.addButton(this.layout.get(3), this.icon("Option 3 \u00bb", "Stitch the incision.", 3), (player, clickType) -> {
            this.playClickSound(player);
            this.menuType = Question.Thirteen;
            ++this.question;
            this.refresh();
        });
    }

    private void buildPage13() {
        this.addButton(this.layout.get(1), this.icon("Option 1 \u00bb", "Reopen incision.", 1), (player, clickType) -> {
            this.playClickSound(player);
            this.menuType = Question.Fourteen;
            ++this.question;
            this.refresh();
        });
        this.addButton(this.layout.get(2), this.icon("Option 2 \u00bb", "Flush area with Betadine.", 2), (player, clickType) -> {
            this.playClickSound(player);
            this.menuType = Question.Fourteen;
            ++this.question;
            this.refresh();
        });
        this.addButton(this.layout.get(3), this.icon("Option 3 \u00bb", "Remove stitches.", 3), (player, clickType) -> {
            this.playClickSound(player);
            this.menuType = Question.Fourteen;
            ++this.question;
            this.refresh();
        });
    }

    private void buildPage14() {
        this.addButton(this.layout.get(1), this.icon("Option 1 \u00bb", "Wrap cannon bone with gauze and vet wrap.", 1), (player, clickType) -> {
            this.playClickSound(player);
            this.menuType = Question.Fifteen;
            ++this.question;
            this.refresh();
        });
        this.addButton(this.layout.get(2), this.icon("Option 2 \u00bb", "Stitch wound again.", 2), (player, clickType) -> {
            this.playClickSound(player);
            this.menuType = Question.Fifteen;
            ++this.question;
            this.refresh();
        });
        this.addButton(this.layout.get(3), this.icon("Option 3 \u00bb", "Cut off anesthetic.", 3), (player, clickType) -> {
            this.playClickSound(player);
            this.menuType = Question.Fifteen;
            ++this.question;
            this.refresh();
        });
    }

    private void buildPage15() {
        this.addButton(this.layout.get(1), this.icon("Option 1 \u00bb", "Remove IV.", 1), (player, clickType) -> {
            this.playClickSound(player);
            player.closeInventory();
        });
        this.addButton(this.layout.get(2), this.icon("Option 2 \u00bb", "Dress the stitches, and cast the cannon bone.", 2), (player, clickType) -> {
            this.playClickSound(player);
            player.closeInventory();
        });
        this.addButton(this.layout.get(3), this.icon("Option 3 \u00bb", "Give more anesthetic.", 3), (player, clickType) -> {
            this.playClickSound(player);
            player.closeInventory();
        });
    }

    private ItemStack icon(String name, String lore, int amount) {
        return ItemStackFactory.Instance.CreateStack(Material.STAINED_GLASS_PANE, (byte)0, amount, C.cAqua + C.Bold + name, new String[]{C.cDAqua + lore});
    }

    public static enum Question {
        One,
        Two,
        Three,
        Four,
        Five,
        Six,
        Seven,
        Eight,
        Nine,
        Ten,
        Eleven,
        Twelve,
        Thirteen,
        Fourteen,
        Fifteen;
        

        private Question() {
        }
    }

}
