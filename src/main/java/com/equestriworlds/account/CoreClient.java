package com.equestriworlds.account;

import org.bukkit.entity.Player;

public class CoreClient {
    private String _name;
    private Player _player;

    public CoreClient(Player player) {
        this._player = player;
        this._name = player.getName();
    }

    public CoreClient(String name) {
        this._name = name;
    }

    public String GetPlayerName() {
        return this._name;
    }

    public Player GetPlayer() {
        return this._player;
    }

    public void SetPlayer(Player player) {
        this._player = player;
    }

    public void Delete() {
        this._name = null;
        this._player = null;
    }
}
