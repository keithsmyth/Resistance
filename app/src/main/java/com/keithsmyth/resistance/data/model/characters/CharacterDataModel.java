package com.keithsmyth.resistance.data.model.characters;

public abstract class CharacterDataModel {

    public final String name;
    public final boolean isBad;

    public CharacterDataModel(String name, boolean isBad) {
        this.name = name;
        this.isBad = isBad;
    }

    public abstract boolean canSee(CharacterDataModel characterDataModel);

    public abstract String canSeeDescription();
}
