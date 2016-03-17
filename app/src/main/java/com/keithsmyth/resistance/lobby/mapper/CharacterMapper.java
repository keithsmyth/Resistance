package com.keithsmyth.resistance.lobby.mapper;

import com.keithsmyth.resistance.data.model.CharacterDataModel;
import com.keithsmyth.resistance.lobby.model.CharacterViewModel;

public class CharacterMapper {

    public CharacterViewModel toViewModel(CharacterDataModel dataModel) {
        return new CharacterViewModel(dataModel.name, dataModel.isBad);
    }
}
