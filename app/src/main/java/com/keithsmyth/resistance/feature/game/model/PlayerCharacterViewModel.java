package com.keithsmyth.resistance.feature.game.model;

import java.util.List;

public class PlayerCharacterViewModel {

    public final String name;
    public final String characterName;
    public final boolean isBad;
    public final List<String> revealedNames;
    public final String revealedDescription;
    public final List<String> characters;

    public PlayerCharacterViewModel(String name, String characterName, boolean isBad, List<String> revealedNames, String revealedDescription, List<String> characters) {
        this.name = name;
        this.characterName = characterName;
        this.isBad = isBad;
        this.revealedNames = revealedNames;
        this.revealedDescription = revealedDescription;
        this.characters = characters;
    }
}
