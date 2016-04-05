package com.keithsmyth.resistance.feature.game.model;

import java.util.List;

public class GamePlayViewModel {

    public final List<GameRoundViewModel> rounds;

    public GamePlayViewModel(List<GameRoundViewModel> rounds) {
        this.rounds = rounds;
    }

    public GameRoundViewModel getCurrentRound() {
        return rounds.get(rounds.size() - 1);
    }
}
