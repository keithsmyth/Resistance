package com.keithsmyth.resistance.feature.game.model;

import com.keithsmyth.resistance.feature.lobby.model.CharacterViewModel;

import java.util.List;

public class PlayerCharacterViewModel {

    public final String name;
    public final String characterName;
    public final boolean isBad;
    public final List<String> revealedNames;
    public final String revealedDescription;
    public final List<CharacterViewModel> characterViewModels;

    public PlayerCharacterViewModel(String name, String characterName, boolean isBad, List<String> revealedNames, String revealedDescription, List<CharacterViewModel> characterViewModels) {
        this.name = name;
        this.characterName = characterName;
        this.isBad = isBad;
        this.revealedNames = revealedNames;
        this.revealedDescription = revealedDescription;
        this.characterViewModels = characterViewModels;
    }
}
