package com.equestriworlds.vet.enums;

import com.equestriworlds.vet.enums.Symptom;
import java.util.Random;

public enum HappySymptom {
    HAPPY(Symptom.HAPPY),
    JOYFUL(Symptom.JOYFUL),
    SLEEK(Symptom.SLEEK),
    BRIGHTEYED(Symptom.BRIGHTEYED),
    ENERGETIC(Symptom.ENERGETIC),
    CONTENT(Symptom.CONTENT),
    RELAXED(Symptom.RELAXED);
    
    public Symptom symptom;

    private HappySymptom(Symptom symptom) {
        this.symptom = symptom;
    }

    public static HappySymptom random() {
        return HappySymptom.values()[new Random().nextInt(6)];
    }
}
