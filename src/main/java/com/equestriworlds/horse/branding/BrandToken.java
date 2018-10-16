package com.equestriworlds.horse.branding;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class BrandToken {
    public UUID owner;
    public String format;
    public List<UUID> coowners = new ArrayList<UUID>();
}
