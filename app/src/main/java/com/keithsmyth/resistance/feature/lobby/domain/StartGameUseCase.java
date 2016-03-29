package com.keithsmyth.resistance.feature.lobby.domain;

import com.keithsmyth.data.provider.GameInfoProvider;
import com.keithsmyth.data.provider.GamePlayProvider;

import java.util.List;
import java.util.Random;

import rx.Single;
import rx.functions.Func1;

public class StartGameUseCase {

    private final GameInfoProvider gameInfoProvider;
    private final GamePlayProvider gamePlayProvider;

    public StartGameUseCase(GameInfoProvider gameInfoProvider, GamePlayProvider gamePlayProvider) {
        this.gameInfoProvider = gameInfoProvider;
        this.gamePlayProvider = gamePlayProvider;
    }

    public Single<?> execute() {
        return gameInfoProvider.getPlayerIds()
            .flatMap(new Func1<List<String>, Single<?>>() {
                @Override
                public Single<?> call(List<String> playerIds) {
                    // pick who's first
                    final Random rand = new Random();
                    final int index = rand.nextInt(playerIds.size());
                    // create and save round 1
                    return gamePlayProvider.createRound(playerIds.get(index));
                }
            })
            .flatMap(new Func1<Object, Single<?>>() {
                @Override
                public Single<?> call(Object o) {
                    // update the game state
                    return gameInfoProvider.setGameState(GameInfoProvider.STATE_STARTED);
                }
            });
    }
}
