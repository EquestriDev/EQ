package com.equestriworlds.certifications.enums;

import com.equestriworlds.certifications.enums.Discipline;
import com.equestriworlds.certifications.enums.TestingPlayerToken;
import org.bukkit.entity.Player;

/**
 * A player who is taking a quiz.
 * Referenced by:
 * CertificationsManager, CertificationsStartCommand
 * Discipline
 */
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
