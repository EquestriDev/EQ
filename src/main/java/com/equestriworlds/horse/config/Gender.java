package com.equestriworlds.horse.config;

import java.util.Random;

public enum Gender {
    MARE("Mare"),
    STALLION("Stallion"),
    GELDING("Gelding");
    
    public String name;

    private Gender(String name) {
        this.name = name;
    }

    public static Gender random() {
        return Gender.values()[new Random().nextInt(2)];
    }
}
