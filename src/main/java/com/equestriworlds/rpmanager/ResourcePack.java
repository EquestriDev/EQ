package com.equestriworlds.rpmanager;

public enum ResourcePack {
    OCD("OCD", "https://dl.dropboxusercontent.com/s/nfm5svf70lh7nqu/ocd.zip"),
    CANVAS("Canvas", "https://dl.dropboxusercontent.com/s/rx2737sblu27i9i/canvas.zip"),
    KELLOUR("KellourPack", "https://dl.dropboxusercontent.com/s/kx70mab0hafoorp/Kellourpack.zip"),
    XMAS("Christmas", "https://dl.dropboxusercontent.com/s/qgmqfw8gejkkxr8/xmas.zip"),
    CUSTOM("Custom", "https://dl.dropboxusercontent.com/s/9fjlfi3jxrmeijy/custom.zip"),
    EMPTY("Clear", "https://dl.dropboxusercontent.com/s/6wu3ej6dwp5ahy1/empty.zip"),
    GROOMING("Grooming", "http://www.equestriworlds.net/resourcepacks/Grooming.zip"),
    PLAID("Plaid", "http://www.equestriworlds.net/resourcepacks/PlaidSP%20Pack.zip");
    
    public String name;
    String link;
    byte[] hash;

    private ResourcePack(String name, String link, String hash) {
        this.name = name;
        this.link = link;
        this.hash = hash.getBytes();
    }

    private ResourcePack(String name, String link) {
        this.name = name;
        this.link = link;
    }
}
