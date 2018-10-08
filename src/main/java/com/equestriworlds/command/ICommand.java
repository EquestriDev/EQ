/*
 * Decompiled with CFR 0_133.
 * 
 * Could not load the following classes:
 *  org.bukkit.command.CommandSender
 *  org.bukkit.entity.Player
 */
package com.equestriworlds.command;

import com.equestriworlds.command.CommandCenter;
import com.equestriworlds.common.Rank;
import java.util.Collection;
import java.util.List;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public interface ICommand {
    public void SetCommandCenter(CommandCenter var1);

    public void Execute(Player var1, String[] var2);

    public Collection<String> Aliases();

    public void SetAliasUsed(String var1);

    public Rank GetRequiredRank();

    public Rank[] GetSpecificRanks();

    public List<String> onTabComplete(CommandSender var1, String var2, String[] var3);
}
