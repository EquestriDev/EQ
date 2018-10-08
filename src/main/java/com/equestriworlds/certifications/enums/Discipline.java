/*
 * Decompiled with CFR 0_133.
 */
package com.equestriworlds.certifications.enums;

import com.equestriworlds.certifications.enums.TestingPlayer;
import com.equestriworlds.certifications.enums.TestingPlayerToken;

public enum Discipline {
    DRESSAGE("Dressage", "certifications.dressage", "dressage", "C", "D", "D", "B", "B", "B", "A", "C", "A", "C"),
    SHOWJUMPING("Show Jumping", "certifications.showjumping", "showjumping", "A", "C", "B", "D", "A", "B", "C", "A", "C", "B"),
    CROSSCOUNTRY("Cross Country", "certifications.crosscountry", "crosscountry", "A", "B", "C", "B", "C", "B", "A", "B", "D", "B"),
    RACING("Racing", "certifications.racing", "racing", "B", "C", "C", "B", "D", "B", "A", "D", "C", "C"),
    WESTERN("Western", "certifications.western", "western", "C", "B", "A", "C", "C", "A", "D", "D", "A", "A"),
    TRAILS("Trails", "certifications.trails", "trails", "C", "D", "A", "B", "C", "B", "C", "B", "D", "D"),
    HUNTERS("Hunters", "certifications.hunters", "hunters", "A", "D", "C", "B", "C", "D", "B", "D", "A", "D"),
    EVENTING("Eventing", "certifications.eventing", "eventing", "A", "C", "B", "C", "C", "B", "C", "B", "A", "B");
    
    public String name;
    public String perm;
    public String file;
    public String answer1;
    public String answer2;
    public String answer3;
    public String answer4;
    public String answer5;
    public String answer6;
    public String answer7;
    public String answer8;
    public String answer9;
    public String answer10;

    private Discipline(String name, String perm, String file, String answer1, String answer2, String answer3, String answer4, String answer5, String answer6, String answer7, String answer8, String answer9, String answer10) {
        this.name = name;
        this.perm = perm;
        this.file = file;
        this.answer1 = answer1;
        this.answer2 = answer2;
        this.answer3 = answer3;
        this.answer4 = answer4;
        this.answer5 = answer5;
        this.answer6 = answer6;
        this.answer7 = answer7;
        this.answer8 = answer8;
        this.answer9 = answer9;
        this.answer10 = answer10;
    }

    public static String getAnswer(TestingPlayer player) {
        switch (player.token.answer) {
            case 1: {
                return player.token.discipline.answer1;
            }
            case 2: {
                return player.token.discipline.answer2;
            }
            case 3: {
                return player.token.discipline.answer3;
            }
            case 4: {
                return player.token.discipline.answer4;
            }
            case 5: {
                return player.token.discipline.answer5;
            }
            case 6: {
                return player.token.discipline.answer6;
            }
            case 7: {
                return player.token.discipline.answer7;
            }
            case 8: {
                return player.token.discipline.answer8;
            }
            case 9: {
                return player.token.discipline.answer9;
            }
            case 10: {
                return player.token.discipline.answer10;
            }
        }
        return "";
    }
}
