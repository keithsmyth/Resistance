package com.keithsmyth.resistance.data.model;

public class PlayerDataModel {

    public final String uuid;
    public final String name;
    public final int gameId;

    public PlayerDataModel(String uuid, String name, int gameId) {
        this.uuid = uuid;
        this.name = name;
        this.gameId = gameId;
    }
}
