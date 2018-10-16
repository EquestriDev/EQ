package com.equestriworlds.vet.enums;

import com.equestriworlds.vet.enums.Symptom;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

public enum Disease {
    INFLUENZA("Influenza", new ArrayList<Symptom>(Arrays.asList(new Symptom[]{Symptom.CHILLS, Symptom.DEHYDRATION, Symptom.FEVER}))),
    EIA("Equine Infection Anemia", new ArrayList<Symptom>(Arrays.asList(new Symptom[]{Symptom.COUGH, Symptom.DEHYDRATION, Symptom.FEVER}))),
    WESTNILEFEVER("West Nile Fever", new ArrayList<Symptom>(Arrays.asList(new Symptom[]{Symptom.FEVER, Symptom.PARALYSIS, Symptom.HEADPRESSING}))),
    TETANUS("Tetanus", new ArrayList<Symptom>(Arrays.asList(new Symptom[]{Symptom.FEVER, Symptom.DILATEDEYES, Symptom.SHORTNESSOFBREATH}))),
    COLIC("Colic", new ArrayList<Symptom>(Arrays.asList(new Symptom[]{Symptom.ROLLING, Symptom.KICKINGATSTOMACH, Symptom.LOSSOFAPPETITE, Symptom.LOSSOFAPPETITE, Symptom.STOMACHTIGHTENING}))),
    STRANGLES("Strangles", new ArrayList<Symptom>(Arrays.asList(new Symptom[]{Symptom.LOSSOFAPPETITE, Symptom.COUGH, Symptom.DIFFICULTYSWALLOWING, Symptom.LUMPS}))),
    EHV("EHV-4", new ArrayList<Symptom>(Arrays.asList(new Symptom[]{Symptom.FEVER, Symptom.COUGH, Symptom.HEAVYBREATHING}))),
    NAVICULAR("Navicular Disease", new ArrayList<Symptom>(Arrays.asList(new Symptom[]{Symptom.WEIGHTONHAUNCHES, Symptom.LAMENESS, Symptom.DEPRESSION}))),
    THRUSH("Thrush", new ArrayList<Symptom>(Arrays.asList(new Symptom[]{Symptom.SMELLYFEET, Symptom.LAMENESS, Symptom.DEPRESSION}))),
    MANGE("Mange", new ArrayList<Symptom>(Arrays.asList(new Symptom[]{Symptom.ROLLING, Symptom.ITCHY, Symptom.DEPRESSION}))),
    PULLEDTENDON("Pulled Tendon", new ArrayList<Symptom>(Arrays.asList(new Symptom[]{Symptom.EXTREMELAMENESS, Symptom.DEPRESSION, Symptom.HEADPRESSING}))),
    ABSCESS("Abscess", new ArrayList<Symptom>(Arrays.asList(new Symptom[]{Symptom.REDNESSINAREA, Symptom.SWELLING, Symptom.UNCOMFORT, Symptom.PAIN}))),
    BOTULISM("Botulism", new ArrayList<Symptom>(Arrays.asList(new Symptom[]{Symptom.SLOWEATING, Symptom.LITTLEMUSCLE, Symptom.STIFFGAIT, Symptom.DIFFICULTYSWALLOWING}))),
    CUSHINGS("Cushings Disease", new ArrayList<Symptom>(Arrays.asList(new Symptom[]{Symptom.EXCESSIVETHIRST, Symptom.EXCESSIVEURINATION, Symptom.WEIGHTSHIFTING}))),
    LAMINITIS("Laminitis", new ArrayList<Symptom>(Arrays.asList(new Symptom[]{Symptom.WEIGHTSHIFTING, Symptom.HIGHPULSE, Symptom.HOTHOOVES, Symptom.RINGSONHOOVES, Symptom.OBESITY}))),
    SCRAPE("Common Scrape", new ArrayList<Symptom>(Arrays.asList(new Symptom[]{Symptom.BLEEDING, Symptom.PAIN, Symptom.DEPRESSION, Symptom.INFECTIOUS})));
    
    public String name;
    public ArrayList<Symptom> symptoms;

    private Disease(String name, ArrayList<Symptom> symptoms) {
        this.name = name;
        this.symptoms = symptoms;
    }
}
