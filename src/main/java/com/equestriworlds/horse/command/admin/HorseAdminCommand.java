package com.equestriworlds.horse.command.admin;

import com.equestriworlds.MiniPlugin;
import com.equestriworlds.command.ICommand;
import com.equestriworlds.command.MultiCommandBase;
import com.equestriworlds.common.Rank;
import com.equestriworlds.horse.HorseManager;
import com.equestriworlds.horse.command.admin.HorseAdminAgeCommand;
import com.equestriworlds.horse.command.admin.HorseAdminBypassCommand;
import com.equestriworlds.horse.command.admin.HorseAdminCallCommand;
import com.equestriworlds.horse.command.admin.HorseAdminDeleteCommand;
import com.equestriworlds.horse.command.admin.HorseAdminDespawnCommand;
import com.equestriworlds.horse.command.admin.HorseAdminDismountAllCommand;
import com.equestriworlds.horse.command.admin.HorseAdminFreeCommand;
import com.equestriworlds.horse.command.admin.HorseAdminGenderCommand;
import com.equestriworlds.horse.command.admin.HorseAdminRemoveCommand;
import com.equestriworlds.horse.command.admin.HorseAdminSetColorCommand;
import com.equestriworlds.horse.command.admin.HorseAdminSetJumpCommand;
import com.equestriworlds.horse.command.admin.HorseAdminSetSpeedCommand;
import com.equestriworlds.horse.command.admin.HorseAdminSetStyleCommand;
import com.equestriworlds.horse.command.admin.HorseAdminSpawnCommand;
import com.equestriworlds.horse.command.admin.HorseAdminTestCommand;
import com.equestriworlds.horse.command.admin.HorseAdminToggleSaleCommand;
import com.equestriworlds.horse.command.brand.admin.HorseBrandAdminCommand;
import com.equestriworlds.horse.command.brand.admin.HorseDeleteBrandCommand;
import com.equestriworlds.horse.command.brand.admin.HorseUnBrandCommand;
import com.equestriworlds.util.F;
import com.equestriworlds.util.UtilPlayer;
import com.equestriworlds.util.UtilServer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

public class HorseAdminCommand
extends MultiCommandBase<HorseManager> {
    public HorseAdminCommand(HorseManager plugin) {
        super(plugin, Rank.TJRMOD, "horseadmin", "ha");
        this.AddCommand(new HorseAdminBypassCommand((HorseManager)this.Plugin));
        this.AddCommand(new HorseAdminToggleSaleCommand((HorseManager)this.Plugin));
        this.AddCommand(new HorseAdminFreeCommand((HorseManager)this.Plugin));
        this.AddCommand(new HorseAdminSetSpeedCommand((HorseManager)this.Plugin));
        this.AddCommand(new HorseAdminSetJumpCommand((HorseManager)this.Plugin));
        this.AddCommand(new HorseAdminSetStyleCommand((HorseManager)this.Plugin));
        this.AddCommand(new HorseAdminSetColorCommand((HorseManager)this.Plugin));
        this.AddCommand(new HorseAdminRemoveCommand((HorseManager)this.Plugin));
        this.AddCommand(new HorseAdminCallCommand((HorseManager)this.Plugin));
        this.AddCommand(new HorseAdminAgeCommand((HorseManager)this.Plugin));
        this.AddCommand(new HorseAdminGenderCommand((HorseManager)this.Plugin));
        this.AddCommand(new HorseAdminSpawnCommand((HorseManager)this.Plugin));
        this.AddCommand(new HorseAdminDeleteCommand((HorseManager)this.Plugin));
        this.AddCommand(new HorseAdminDismountAllCommand((HorseManager)this.Plugin));
        this.AddCommand(new HorseAdminDespawnCommand((HorseManager)this.Plugin));
        this.AddCommand(new HorseBrandAdminCommand((HorseManager)this.Plugin));
        this.AddCommand(new HorseUnBrandCommand((HorseManager)this.Plugin));
        this.AddCommand(new HorseDeleteBrandCommand((HorseManager)this.Plugin));
        if (UtilServer.getServer().getPort() == 51478) {
            this.AddCommand(new HorseAdminTestCommand((HorseManager)this.Plugin));
        }
    }

    @Override
    protected void Help(Player caller, String[] args) {
        UtilPlayer.message((Entity)caller, " ");
        UtilPlayer.message((Entity)caller, F.main(((HorseManager)this.Plugin).getName(), "Available commands:"));
        UtilPlayer.message((Entity)caller, F.help("/ha spawn [ID]", "Horse Spawning Menu", Rank.TJRMOD));
        UtilPlayer.message((Entity)caller, F.help("/ha setspeed (ID) (Speed)", "Changes the speed", Rank.TJRMOD));
        UtilPlayer.message((Entity)caller, F.help("/ha setjump (ID) (Jump)", "Changes the jump height", Rank.TJRMOD));
        UtilPlayer.message((Entity)caller, F.help("/ha setstyle (ID) (Style)", "Changes the style", Rank.TJRMOD));
        UtilPlayer.message((Entity)caller, F.help("/ha setcolor (ID) (Color)", "Changes the color", Rank.TJRMOD));
        UtilPlayer.message((Entity)caller, F.help("/ha setgender (ID) (Gender)", "Set the gender", Rank.TJRMOD));
        UtilPlayer.message((Entity)caller, F.help("/ha free", "Free a horse", Rank.JRMOD));
        UtilPlayer.message((Entity)caller, F.help("/ha bypass", "Toggle Horse Bypass Mode", Rank.JRMOD));
        UtilPlayer.message((Entity)caller, F.help("/ha togglesale (ID)", "Toggle horse's sale mode", Rank.JRMOD));
        UtilPlayer.message((Entity)caller, F.help("/ha remove", "Go into Remove Mode. Crouch + Left-Click to despawn", Rank.JRMOD));
        UtilPlayer.message((Entity)caller, F.help("/ha age (ID)", "Toggle age", Rank.JRMOD));
        UtilPlayer.message((Entity)caller, F.help("/ha delete (ID)", "Deletes a horse completely", Rank.MOD));
        UtilPlayer.message((Entity)caller, F.help("/ha brand", "Display the staff brand commands", Rank.MOD));
        UtilPlayer.message((Entity)caller, F.help("/ha dismount (Player)", "Force a player to dismount", Rank.DEV));
    }
}
