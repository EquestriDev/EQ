/*
 * Decompiled with CFR 0_133.
 */
package com.equestriworlds.grooming.Objects;

import com.equestriworlds.grooming.Objects.CrosstiedHorseToken;
import com.equestriworlds.horse.config.CustomHorse;

public class CrosstiedHorse {
    public CustomHorse horse;
    public CrosstiedHorseToken token;

    public CrosstiedHorse(CustomHorse horse) {
        this.horse = horse;
        this.token = new CrosstiedHorseToken();
    }
}
