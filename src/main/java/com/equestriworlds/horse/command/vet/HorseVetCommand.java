package com.equestriworlds.horse.command.vet;

import com.equestriworlds.MiniPlugin;
import com.equestriworlds.command.ICommand;
import com.equestriworlds.command.MultiCommandBase;
import com.equestriworlds.common.Rank;
import com.equestriworlds.horse.HorseManager;
import com.equestriworlds.horse.command.vet.HorseVetSurgeryCommand;
import com.equestriworlds.horse.command.vet.HorseVetSymptomsCommand;
import com.equestriworlds.horse.command.vet.HorseVetXrayCommand;
import com.equestriworlds.horse.command.vet.breeding.HorseBreedingAbortCommand;
import com.equestriworlds.horse.command.vet.breeding.HorseBreedingForceBirthCommand;
import com.equestriworlds.horse.command.vet.breeding.HorseBreedingMakeBabyCommand;
import com.equestriworlds.horse.command.vet.breeding.HorseBreedingtestBreedCommand;
import com.equestriworlds.util.F;
import com.equestriworlds.util.UtilPlayer;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

/**
 * /vet
 */
public class HorseVetCommand
extends MultiCommandBase<HorseManager> {
    public HorseVetCommand(HorseManager plugin) {
        super(plugin, Rank.VET, "vet", "vh");
        this.AddCommand(new HorseBreedingtestBreedCommand((HorseManager)this.Plugin));
        this.AddCommand(new HorseBreedingMakeBabyCommand((HorseManager)this.Plugin));
        this.AddCommand(new HorseBreedingAbortCommand((HorseManager)this.Plugin));
        this.AddCommand(new HorseVetSymptomsCommand((HorseManager)this.Plugin));
        this.AddCommand(new HorseVetSurgeryCommand((HorseManager)this.Plugin));
        this.AddCommand(new HorseVetXrayCommand((HorseManager)this.Plugin));
        this.AddCommand(new HorseBreedingForceBirthCommand((HorseManager)this.Plugin));
    }

    @Override
    protected void Help(Player caller, String[] args) {
        UtilPlayer.message((Entity)caller, ChatColor.translateAlternateColorCodes((char)'&', (String)"&7&m&l\u2055--------------------------&7&l\u2055"));
        UtilPlayer.message((Entity)caller, F.main("Vet Manager", "Available commands:"));
        UtilPlayer.message((Entity)caller, " ");
        UtilPlayer.message((Entity)caller, F.help("/vet abort (ID)", "End the pregnancy of a mare", Rank.VET));
        UtilPlayer.message((Entity)caller, F.help("/vet createfoal (ID) (MateID)", "Create a foal with the desired partners", Rank.VET));
        UtilPlayer.message((Entity)caller, F.help("/vet symptoms", "View the symptoms of the horse on the leash", Rank.VET));
        UtilPlayer.message((Entity)caller, "");
        UtilPlayer.message((Entity)caller, ChatColor.translateAlternateColorCodes((char)'&', (String)"&7&m&l\u2055--------------------------&7&l\u2055"));
    }
}
