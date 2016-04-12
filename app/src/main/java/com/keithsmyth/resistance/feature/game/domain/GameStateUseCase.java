package com.keithsmyth.resistance.feature.game.domain;

import com.keithsmyth.data.model.GameInfoDataModel;
import com.keithsmyth.data.model.GamePlayDataModel;
import com.keithsmyth.data.model.GameRoundDataModel;
import com.keithsmyth.data.model.GameRulesDataModel;
import com.keithsmyth.data.model.RoundRulesDataModel;
import com.keithsmyth.data.provider.GameInfoProvider;
import com.keithsmyth.data.provider.GamePlayProvider;
import com.keithsmyth.data.provider.GameRulesProvider;
import com.keithsmyth.resistance.feature.game.model.GamePlayViewModel;
import com.keithsmyth.resistance.feature.game.model.GameRoundViewModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rx.Observable;
import rx.functions.Func2;

public class GameStateUseCase {

    private final GameInfoProvider gameInfoProvider;
    private final GamePlayProvider gamePlayProvider;
    private final GameRulesProvider gameRulesProvider;

    public GameStateUseCase(GameInfoProvider gameInfoProvider, GamePlayProvider gamePlayProvider, GameRulesProvider gameRulesProvider) {
        this.gameInfoProvider = gameInfoProvider;
        this.gamePlayProvider = gamePlayProvider;
        this.gameRulesProvider = gameRulesProvider;
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
        final GameRulesDataModel gameRules = gameRulesProvider.getGameRules(gameInfoDataModel.getMapPlayerIdToName().size());
        final Map<String, GameRoundDataModel> mapRoundNumberToRound = gamePlayDataModel.getMapRoundNumberToRound();

        final List<GameRoundViewModel> rounds = new ArrayList<>(gameRules.getTotalRounds());
        // played rounds from db
        for (int roundNumber = 1; roundNumber <= gamePlayDataModel.getCurrentRound(); roundNumber++) {
            final RoundRulesDataModel rulesDataModel = gameRules.getRoundRulesDataModel(roundNumber);
            final GameRoundDataModel gameRoundDataModel = mapRoundNumberToRound.get(gamePlayProvider.padRoundNumber(roundNumber));
            rounds.add(new GameRoundViewModel(roundNumber,
                rulesDataModel.numberOfPlayers,
                rulesDataModel.numberOfFails,
                gameRoundDataModel.getCaptain(),
                gameInfoDataModel.getMapPlayerIdToName().get(gameRoundDataModel.getCaptain()),
                gameRoundDataModel.getStatus(),
                createPlayerIdToNameForTeamMap(gameRoundDataModel, gameInfoDataModel)));
        }
        // generate empty remaining rounds
        for (int roundNumber = gamePlayDataModel.getCurrentRound() + 1; roundNumber < gameRules.getTotalRounds(); roundNumber++) {
            final RoundRulesDataModel rulesDataModel = gameRules.getRoundRulesDataModel(roundNumber);
            rounds.add(new GameRoundViewModel(roundNumber,
                rulesDataModel.numberOfPlayers,
                rulesDataModel.numberOfFails,
                "",
                "",
                GamePlayProvider.STATUS_NONE,
                Collections.<String, String>emptyMap()));
        }
        return new GamePlayViewModel(rounds, gamePlayDataModel.getCurrentRound());
    }

    private Map<String, String> createPlayerIdToNameForTeamMap(GameRoundDataModel gameRoundDataModel, GameInfoDataModel gameInfoDataModel) {
        final Map<String, Object> mapPlayerIdForTeam = gameRoundDataModel.getMapPlayerIdForTeam();
        if (mapPlayerIdForTeam == null || mapPlayerIdForTeam.isEmpty()) {
            return Collections.emptyMap();
        }
        final Map<String, String> playerIdToNameForTeamMap = new HashMap<>(mapPlayerIdForTeam.size());
        for (String playerId : mapPlayerIdForTeam.keySet()) {
            playerIdToNameForTeamMap.put(playerId, gameInfoDataModel.getMapPlayerIdToName().get(playerId));
        }
        return playerIdToNameForTeamMap;
    }
}
