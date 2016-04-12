package com.keithsmyth.resistance.feature.game.model;

import java.util.List;

public class GamePlayViewModel {

    public final List<GameRoundViewModel> rounds;
    public final int currentRound;

    public GamePlayViewModel(List<GameRoundViewModel> rounds, int currentRound) {
        this.rounds = rounds;
        this.currentRound = currentRound;
    }

    public GameRoundViewModel getCurrentRound() {
        return rounds.get(currentRound - 1);
    }
}
