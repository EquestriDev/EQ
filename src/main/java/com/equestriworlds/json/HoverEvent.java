package com.equestriworlds.json;

public enum HoverEvent {
    SHOW_TEXT("show_text"),
    SHOW_ITEM("show_item"),
    SHOW_ACHIEVEMENT("show_achievement");
    
    private String _minecraftString;

    private HoverEvent(String minecraftString) {
        this._minecraftString = minecraftString;
    }

    public String toString() {
        return this._minecraftString;
    }
}
