/*
 * Decompiled with CFR 0_133.
 */
package com.equestriworlds.json;

public enum ClickEvent {
    RUN_COMMAND("run_command"),
    SUGGEST_COMMAND("suggest_command"),
    OPEN_URL("open_url"),
    CHANGE_PAGE("change_page");
    
    private String _minecraftString;

    private ClickEvent(String minecraftString) {
        this._minecraftString = minecraftString;
    }

    public String toString() {
        return this._minecraftString;
    }
}
