package com.equestriworlds.horse.breeding;

import java.util.Random;

public enum breedingStages {
    PREGNANT("Pregnancy"),
    MARERECOVERY("Mare Recovery"),
    STALLIONRECOVERY("Stallion Recovery"),
    FOAL("Aged"),
    FOALSTAGE2("Rideable"),
    ABORT("Recovery"),
    NONE("None");
    
    public String name;

    private breedingStages(String name) {
        this.name = name;
    }

    public static breedingStages random() {
        return breedingStages.values()[new Random().nextInt(2)];
    }
}
