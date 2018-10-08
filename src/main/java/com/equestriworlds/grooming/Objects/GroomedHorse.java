/*
 * Decompiled with CFR 0_133.
 */
package com.equestriworlds.grooming.Objects;

import com.equestriworlds.grooming.Objects.GroomedHorseToken;
import com.equestriworlds.horse.config.CustomHorse;

public class GroomedHorse {
    public CustomHorse horse;
    public GroomedHorseToken token;

    public GroomedHorse(CustomHorse horse) {
        this.horse = horse;
        this.token = new GroomedHorseToken();
    }
}
