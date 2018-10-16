package com.equestriworlds.horse.config;

public enum HorseAccess {
    NOBODY,
    FRIENDS,
    ALL;
    

    private HorseAccess() {
    }

    public static HorseAccess parse(String string) {
        if (string.equalsIgnoreCase("nobody") || string.equalsIgnoreCase("none")) {
            return NOBODY;
        }
        if (string.equalsIgnoreCase("friends")) {
            return FRIENDS;
        }
        if (string.equalsIgnoreCase("all") || string.equalsIgnoreCase("everyone") || string.equalsIgnoreCase("everybody")) {
            return ALL;
        }
        return null;
    }
}
