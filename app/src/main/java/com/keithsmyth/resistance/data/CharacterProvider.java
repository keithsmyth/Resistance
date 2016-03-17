package com.keithsmyth.resistance.data;

import com.keithsmyth.resistance.data.model.CharacterDataModel;

import java.util.Arrays;
import java.util.List;

public class CharacterProvider {

    public List<CharacterDataModel> getCharacters() {
        return Arrays.asList(
            new CharacterDataModel("Merlin", false),
            new CharacterDataModel("Percival", false),
            new CharacterDataModel("Mordred", true),
            new CharacterDataModel("Assassin", true),
            new CharacterDataModel("Morgana", true),
            new CharacterDataModel("Oberon", true)
        );
    }
}
