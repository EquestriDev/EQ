package com.equestriworlds.horse.command;

import com.equestriworlds.MiniPlugin;
import com.equestriworlds.command.ICommand;
import com.equestriworlds.command.MultiCommandBase;
import com.equestriworlds.common.Rank;
import com.equestriworlds.horse.HorseManager;
import com.equestriworlds.horse.command.HorseAccessCommand;
import com.equestriworlds.horse.command.HorseCallCommand;
import com.equestriworlds.horse.command.HorseChangeIdCommand;
import com.equestriworlds.horse.command.HorseClaimCommand;
import com.equestriworlds.horse.command.HorseConditionCommand;
import com.equestriworlds.horse.command.HorseFindCommand;
import com.equestriworlds.horse.command.HorseFollowCommand;
import com.equestriworlds.horse.command.HorseFreeCommand;
import com.equestriworlds.horse.command.HorseGiveCommand;
import com.equestriworlds.horse.command.HorseInfoCommand;
import com.equestriworlds.horse.command.HorseListCommand;
import com.equestriworlds.horse.command.HorseRenameCommand;
import com.equestriworlds.horse.command.HorseTeleportCommand;
import com.equestriworlds.horse.command.brand.HorseBrandCommand;
import com.equestriworlds.horse.command.brand.HorseBrandListCommand;
import com.equestriworlds.horse.command.brand.HorseBrandRegisterCommand;
import com.equestriworlds.horse.command.brand.brandowner.HorseBrandOwnerCommand;
import com.equestriworlds.horse.command.friend.HorseFriendCommand;
import com.equestriworlds.horse.command.home.HorseAddHomeCommand;
import com.equestriworlds.horse.command.home.HorseDelHomeCommand;
import com.equestriworlds.horse.command.home.HorseHomeAllCommand;
import com.equestriworlds.horse.command.home.HorseHomeCommand;
import com.equestriworlds.horse.command.home.HorseListHomeCommand;
import com.equestriworlds.horse.command.trust.HorseTrustCommand;
import com.equestriworlds.util.F;
import com.equestriworlds.util.UtilPlayer;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

public class HorseCommand
extends MultiCommandBase<HorseManager> {
    public HorseCommand(HorseManager plugin) {
        super(plugin, Rank.PLAYER, "horse", "h");
        this.AddCommand(new HorseRenameCommand((HorseManager)this.Plugin));
        this.AddCommand(new HorseClaimCommand((HorseManager)this.Plugin));
        this.AddCommand(new HorseCallCommand((HorseManager)this.Plugin));
        this.AddCommand(new HorseListCommand((HorseManager)this.Plugin));
        this.AddCommand(new HorseTeleportCommand((HorseManager)this.Plugin));
        this.AddCommand(new HorseInfoCommand((HorseManager)this.Plugin));
        this.AddCommand(new HorseConditionCommand((HorseManager)this.Plugin));
        this.AddCommand(new HorseFindCommand((HorseManager)this.Plugin));
        this.AddCommand(new HorseGiveCommand((HorseManager)this.Plugin));
        this.AddCommand(new HorseFreeCommand((HorseManager)this.Plugin));
        this.AddCommand(new HorseAccessCommand((HorseManager)this.Plugin));
        this.AddCommand(new HorseAddHomeCommand((HorseManager)this.Plugin));
        this.AddCommand(new HorseDelHomeCommand((HorseManager)this.Plugin));
        this.AddCommand(new HorseListHomeCommand((HorseManager)this.Plugin));
        this.AddCommand(new HorseHomeAllCommand((HorseManager)this.Plugin));
        this.AddCommand(new HorseHomeCommand((HorseManager)this.Plugin));
        this.AddCommand(new HorseChangeIdCommand((HorseManager)this.Plugin));
        this.AddCommand(new HorseFollowCommand((HorseManager)this.Plugin));
        this.AddCommand(new HorseTrustCommand((HorseManager)this.Plugin));
        this.AddCommand(new HorseFriendCommand((HorseManager)this.Plugin));
        this.AddCommand(new HorseHomeCommand((HorseManager)this.Plugin));
        this.AddCommand(new HorseBrandCommand((HorseManager)this.Plugin));
        this.AddCommand(new HorseBrandRegisterCommand((HorseManager)this.Plugin));
        this.AddCommand(new HorseBrandListCommand((HorseManager)this.Plugin));
        this.AddCommand(new HorseBrandOwnerCommand((HorseManager)this.Plugin));
    }

    @Override
    protected void Help(Player caller, String[] args) {
        UtilPlayer.message((Entity)caller, ChatColor.translateAlternateColorCodes((char)'&', (String)"&7&m&l\u2055--------------------------&7&l\u2055"));
        UtilPlayer.message((Entity)caller, F.main(((HorseManager)this.Plugin).getName(), "Available commands:"));
        UtilPlayer.message((Entity)caller, " ");
        UtilPlayer.message((Entity)caller, F.help("/horse claim (Name)", "Claim the horse you are sitting on", Rank.PLAYER));
        UtilPlayer.message((Entity)caller, F.help("/horse changeid (ID) (Name)", "Change the ID of your horse", Rank.PLAYER));
        UtilPlayer.message((Entity)caller, "");
        UtilPlayer.message((Entity)caller, F.help("/horse call (ID)", "Summon your horse", Rank.PLAYER));
        UtilPlayer.message((Entity)caller, F.help("/horse tp (ID)", "Teleport to your horse", Rank.PLAYER));
        UtilPlayer.message((Entity)caller, F.help("/horse follow (ID)", "Make a horse follow you", Rank.PLAYER));
        UtilPlayer.message((Entity)caller, "");
        UtilPlayer.message((Entity)caller, F.help("/horse list [Page]", "List your horses", Rank.PLAYER));
        UtilPlayer.message((Entity)caller, F.help("/horse info", "Get information about a horse by Left-Clicking", Rank.PLAYER));
        UtilPlayer.message((Entity)caller, F.help("/horse condition", "See weight information", Rank.PLAYER));
        UtilPlayer.message((Entity)caller, F.help("/horse find (ID)", "See who owns this horse", Rank.PLAYER));
        UtilPlayer.message((Entity)caller, "");
        UtilPlayer.message((Entity)caller, F.help("/horse give (ID) (Player)", "Give the horse to player", Rank.PLAYER));
        UtilPlayer.message((Entity)caller, F.help("/horse free (ID)", "Let your horse go", Rank.PLAYER));
        UtilPlayer.message((Entity)caller, "");
        UtilPlayer.message((Entity)caller, F.help("/horse brandowner", "To see brandowner commands", Rank.PLAYER));
        UtilPlayer.message((Entity)caller, F.help("/horse friend", "To see friend commands", Rank.PLAYER));
        UtilPlayer.message((Entity)caller, F.help("/horse home", "To see home commands", Rank.PLAYER));
        UtilPlayer.message((Entity)caller, F.help("/horse brand", "To see branding commands", Rank.PLAYER));
        UtilPlayer.message((Entity)caller, ChatColor.translateAlternateColorCodes((char)'&', (String)"&7&m&l\u2055--------------------------&7&l\u2055"));
    }
}
