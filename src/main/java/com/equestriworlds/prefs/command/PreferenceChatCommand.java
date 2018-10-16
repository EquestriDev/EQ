package com.equestriworlds.prefs.command;

import com.equestriworlds.MiniPlugin;
import com.equestriworlds.command.CommandBase;
import com.equestriworlds.common.Rank;
import com.equestriworlds.prefs.Preference;
import com.equestriworlds.prefs.PreferenceManager;
import com.equestriworlds.util.F;
import com.equestriworlds.util.UtilPlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

public class PreferenceChatCommand
extends CommandBase<PreferenceManager> {
    PreferenceChatCommand(PreferenceManager plugin) {
        super(plugin, Rank.PLAYER, "chat");
    }

    @Override
    public void Execute(Player caller, String[] args) {
        boolean b = ((Preference)((PreferenceManager)this.Plugin).Get((Player)caller)).customChat;
        ((Preference)((PreferenceManager)this.Plugin).Get((Player)caller)).customChat = !((Preference)((PreferenceManager)this.Plugin).Get((Player)caller)).customChat;
        UtilPlayer.message((Entity)caller, F.main(((PreferenceManager)this.Plugin).getName(), "Custom Chat: " + F.ed(!b)));
    }
}
