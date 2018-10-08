/*
 * Decompiled with CFR 0_133.
 * 
 * Could not load the following classes:
 *  org.bukkit.entity.Player
 */
package com.equestriworlds.prefs.command;

import com.equestriworlds.command.ICommand;
import com.equestriworlds.command.MultiCommandBase;
import com.equestriworlds.common.Rank;
import com.equestriworlds.prefs.PreferenceManager;
import com.equestriworlds.prefs.command.PreferenceChatCommand;
import org.bukkit.entity.Player;

public class PreferenceCommand
extends MultiCommandBase<PreferenceManager> {
    public PreferenceCommand(PreferenceManager plugin) {
        super(plugin, Rank.PLAYER, "preference", "prefs");
        this.AddCommand(new PreferenceChatCommand(plugin));
    }

    @Override
    protected void Help(Player caller, String[] args) {
    }
}
