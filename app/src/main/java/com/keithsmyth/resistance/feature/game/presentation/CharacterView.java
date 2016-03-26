package com.keithsmyth.resistance.feature.game.presentation;

import com.keithsmyth.resistance.feature.game.model.PlayerCharacterViewModel;

interface CharacterView {

    void showGameId(int currentGameId);

    void displayPlayerCharacterInfo(PlayerCharacterViewModel playerCharacterViewModel);
}
