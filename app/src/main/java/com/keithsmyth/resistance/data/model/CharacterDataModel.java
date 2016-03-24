package com.keithsmyth.resistance.data.model;

public abstract class CharacterDataModel {

    public final String name;
    public final boolean isBad;

    public CharacterDataModel(String name, boolean isBad) {
        this.name = name;
        this.isBad = isBad;
    }

    public abstract boolean isRevealed(CharacterDataModel characterDataModel);

    public abstract String revealedDescription();
}
