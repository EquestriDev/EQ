package com.equestriworlds.vet.enums;

import java.util.Random;

/**
 * Used by CustomHorseToken
 */
public enum Symptom {
    LAMENESS("Lameness"),
    EXTREMELAMENESS("Extreme Lameness"),
    FEVER("Fever"),
    DEPRESSION("Depression"),
    HEAVYBREATHING("Heavy Breathing"),
    COUGH("Cough"),
    KICKINGATSTOMACH("Kicking at Stomach"),
    ROLLING("Rolling"),
    PARALYSIS("Paralysis in Rear legs"),
    LOSSOFAPPETITE("Loss of Appetite"),
    WEIGHTONHAUNCHES("Putting Weight on Haunches"),
    DILATEDEYES("Dilated Eyes"),
    FASTHEARTBEAT("Fast Heartbeat"),
    SLOWHEARTBEAT("Slow Heartbeat"),
    CHILLS("Chills"),
    DEHYDRATION("Dehydration"),
    CHESTPAIN("Chest Pain"),
    SHORTNESSOFBREATH("Shortness of Breath"),
    HEADPRESSING("Head Pressing"),
    DIFFICULTYSWALLOWING("Difficulty Swallowing"),
    SMELLYFEET("Smelly Feet"),
    SMELLYLEGS("Smelly Leg(s)"),
    ITCHY("Itchy"),
    STOMACHTIGHTENING("Stomach Tightening or Clenching"),
    REDNESSINAREA("Redness in Area"),
    SWELLING("Swelling"),
    UNCOMFORT("Uncomfortable"),
    PAIN("Pain"),
    SLOWEATING("Slow Eating"),
    SLOWMOVING("Slow Moving"),
    LITTLEMUSCLE("Little Muscle Strength"),
    STIFFGAIT("Stiff Gaits"),
    WEIGHTSHIFTING("Weight Shifting"),
    HIGHPULSE("High Pulse"),
    HOTHOOVES("Hot Hooves"),
    RINGSONHOOVES("Rings on Hooves"),
    OBESITY("Obesity"),
    SHORTSTRIDES("Short Strides"),
    DIARRHEA("Diarreah"),
    BLEEDING("Bleeding"),
    INFECTIOUS("Infectious"),
    LUMPS("Lumps on chin and jaw"),
    EXCESSIVETHIRST("Excessive Thirst"),
    EXCESSIVEURINATION("Excessive Urination"),
    HAPPY("Happy"),
    JOYFUL("Joyful"),
    SLEEK("Sleek"),
    BRIGHTEYED("Bright-Eyed"),
    ENERGETIC("Energetic"),
    CONTENT("Content"),
    RELAXED("Relaxed");
    
    public String name;

    private Symptom(String name) {
        this.name = name;
    }

    public static Symptom random() {
        return Symptom.values()[new Random().nextInt(2)];
    }
}
