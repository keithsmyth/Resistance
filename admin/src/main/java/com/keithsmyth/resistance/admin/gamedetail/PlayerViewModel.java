package com.keithsmyth.resistance.admin.gamedetail;

public class PlayerViewModel {

    public final String id;
    public final String name;
    public final String characterName;

    public PlayerViewModel(String id, String name, String characterName) {
        this.id = id;
        this.name = name;
        this.characterName = characterName;
    }
}
