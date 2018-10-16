package com.equestriworlds.horse.branding;

import com.equestriworlds.horse.branding.BrandToken;
import java.util.UUID;

public class Brand {
    public UUID id; // Player ID, key
    public BrandToken token; // Data

    public Brand(UUID id, String brand) {
        this.id = id;
        this.token = new BrandToken();
        this.token.owner = id;
        this.token.format = brand;
    }

    public Brand(UUID id, BrandToken brandToken) {
        this.id = id;
        this.token = brandToken;
    }
}
