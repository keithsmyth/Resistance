package com.keithsmyth.resistance.feature.lobby.mapper;

import com.keithsmyth.resistance.data.model.CharacterDataModel;
import com.keithsmyth.resistance.feature.lobby.model.CharacterViewModel;

public class CharacterMapper {

    public CharacterViewModel toViewModel(CharacterDataModel dataModel) {
        return new CharacterViewModel(dataModel.name, dataModel.isBad);
    }
}
