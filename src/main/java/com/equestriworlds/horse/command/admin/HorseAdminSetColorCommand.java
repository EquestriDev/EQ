package com.equestriworlds.horse.command.admin;

import com.equestriworlds.MiniPlugin;
import com.equestriworlds.command.CommandBase;
import com.equestriworlds.common.Rank;
import com.equestriworlds.horse.HorseManager;
import com.equestriworlds.horse.config.CustomHorse;
import com.equestriworlds.horse.config.CustomHorseToken;
import com.equestriworlds.horse.config.HorseConfig;
import com.equestriworlds.util.F;
import com.equestriworlds.util.UtilPlayer;
import java.util.List;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftAbstractHorse;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftHorse;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftLlama;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Horse;
import org.bukkit.entity.Llama;
import org.bukkit.entity.Player;

public class HorseAdminSetColorCommand
extends CommandBase<HorseManager> {
    public HorseAdminSetColorCommand(HorseManager plugin) {
        super(plugin, Rank.TJRMOD, "setcolor", "sc");
    }

    @Override
    public void Execute(Player caller, String[] args) {
        if (args == null || args.length != 2) {
            UtilPlayer.message((Entity)caller, F.help("/ha setcolor (ID) (Color)", "Changes the color of the horse.", Rank.TJRMOD));
        } else {
            String id = args[0];
            CustomHorse horse = ((HorseManager)this.Plugin).config.getHorseById(id);
            if (horse == null) {
                UtilPlayer.message((Entity)caller, F.main(((HorseManager)this.Plugin).getName(), "Horse under the ID " + F.elem(new StringBuilder().append(id).append("").toString()) + " doesn't exist."));
                return;
            }
            if (horse.horse instanceof CraftHorse) {
                Horse.Color color = null;
                try {
                    color = Horse.Color.valueOf((String)args[1]);
                }
                catch (Exception e) {
                    UtilPlayer.message((Entity)caller, F.main(((HorseManager)this.Plugin).getName(), "Invalid color. Available colors: "));
                    for (Horse.Color colors : Horse.Color.values()) {
                        UtilPlayer.message((Entity)caller, F.elem(colors.toString()));
                    }
                    return;
                }
                horse.setColor(color);
                UtilPlayer.message((Entity)caller, F.main(((HorseManager)this.Plugin).getName(), new StringBuilder().append("Successfuly changed ").append(F.elem(horse.token.name)).append("'s").toString()) + " color to " + F.elem(color.toString()) + ".");
            } else if (horse.horse instanceof CraftLlama) {
                Llama.Color color = null;
                try {
                    color = Llama.Color.valueOf((String)args[1]);
                }
                catch (Exception e) {
                    UtilPlayer.message((Entity)caller, F.main(((HorseManager)this.Plugin).getName(), "Invalid color. Available colors: "));
                    for (Llama.Color colors : Llama.Color.values()) {
                        UtilPlayer.message((Entity)caller, F.elem(colors.toString()));
                    }
                    return;
                }
                horse.setLlamaColor(color);
                UtilPlayer.message((Entity)caller, F.main(((HorseManager)this.Plugin).getName(), new StringBuilder().append("Successfuly changed ").append(F.elem(horse.token.name)).append("'s").toString()) + " color to " + F.elem(color.toString()) + ".");
            } else {
                UtilPlayer.message((Entity)caller, F.main(((HorseManager)this.Plugin).getName(), "The color of this horse cannot be changed."));
            }
        }
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, String commandLabel, String[] args) {
        if (args.length != 3) {
            return null;
        }
        String lastArg = args[2];
        return this.getMatches(lastArg, (Enum[])Horse.Color.values());
    }
}
