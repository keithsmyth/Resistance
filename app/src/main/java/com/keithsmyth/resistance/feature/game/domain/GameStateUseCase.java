package com.keithsmyth.resistance.feature.game.domain;

import com.keithsmyth.data.model.GameInfoDataModel;
import com.keithsmyth.data.model.GamePlayDataModel;
import com.keithsmyth.data.model.GameRoundDataModel;
import com.keithsmyth.data.provider.GameInfoProvider;
import com.keithsmyth.data.provider.GamePlayProvider;
import com.keithsmyth.resistance.feature.game.model.GamePlayViewModel;
import com.keithsmyth.resistance.feature.game.model.GameRoundViewModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Locale;
import java.util.Map;

import rx.Observable;
import rx.functions.Func2;

public class GameStateUseCase {

    private final GameInfoProvider gameInfoProvider;
    private final GamePlayProvider gamePlayProvider;

    public GameStateUseCase(GameInfoProvider gameInfoProvider, GamePlayProvider gamePlayProvider) {
        this.gameInfoProvider = gameInfoProvider;
        this.gamePlayProvider = gamePlayProvider;
    }

    public Observable<GamePlayViewModel> execute() {
        final Observable<GameInfoDataModel> gameInfoObservable = gameInfoProvider.getGameInfo().toObservable();
        final Observable<GamePlayDataModel> gamePlayObservable = gamePlayProvider.watchGamePlay();
        return gameInfoObservable
            .zipWith(gamePlayObservable, new Func2<GameInfoDataModel, GamePlayDataModel, GamePlayViewModel>() {
                @Override
                public GamePlayViewModel call(GameInfoDataModel gameInfoDataModel, GamePlayDataModel gamePlayDataModel) {
                    return mapToViewModel(gameInfoDataModel, gamePlayDataModel);
                }
            });
    }

    private GamePlayViewModel mapToViewModel(GameInfoDataModel gameInfoDataModel, GamePlayDataModel gamePlayDataModel) {
        final GamePlayViewModel gamePlayViewModel = new GamePlayViewModel(new ArrayList<GameRoundViewModel>());
        final Map<String, GameRoundDataModel> mapRoundNumberToRound = gamePlayDataModel.getMapRoundNumberToRound();

        for (int roundNumber = 1; roundNumber <= gamePlayDataModel.getCurrentRound(); roundNumber++) {

            final GameRoundDataModel gameRoundDataModel = mapRoundNumberToRound.get(padRoundNumber(roundNumber));

            gamePlayViewModel.rounds.add(
                new GameRoundViewModel(roundNumber,
                    gameRoundDataModel.getCaptain(),
                    gameInfoDataModel.getMapPlayerIdToName().get(gameRoundDataModel.getCaptain()),
                    gameRoundDataModel.getStatus(),
                    gameRoundDataModel.getMapPlayerIdForTeam() != null
                        ? gameRoundDataModel.getMapPlayerIdForTeam().keySet()
                        : Collections.<String>emptySet()));

        }
        return gamePlayViewModel;
    }

    private String padRoundNumber(int roundNumber) {
        return String.format(Locale.getDefault(), "%02d", roundNumber);
    }
}
