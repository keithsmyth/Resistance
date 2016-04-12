package com.keithsmyth.resistance.feature.game.presentation;

import com.keithsmyth.resistance.feature.game.model.GameRoundViewModel;

import java.util.List;

public interface GameView {

    void setRound(int roundNumber);

    void setCaptain(String captain);

    void setRounds(List<GameRoundViewModel> rounds);
}
