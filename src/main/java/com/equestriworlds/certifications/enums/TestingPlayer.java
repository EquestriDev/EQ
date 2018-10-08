/*
 * Decompiled with CFR 0_133.
 * 
 * Could not load the following classes:
 *  org.bukkit.entity.Player
 */
package com.equestriworlds.certifications.enums;

import com.equestriworlds.certifications.enums.Discipline;
import com.equestriworlds.certifications.enums.TestingPlayerToken;
import org.bukkit.entity.Player;

public class TestingPlayer {
    public Player player;
    public TestingPlayerToken token;

    public TestingPlayer(Player player, Discipline discipline) {
        this.player = player;
        this.token = new TestingPlayerToken();
        this.token.discipline = discipline;
        this.token.answer = 1;
    }
}
