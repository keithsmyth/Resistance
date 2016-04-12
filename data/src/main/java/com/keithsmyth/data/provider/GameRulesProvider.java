package com.keithsmyth.data.provider;

import com.keithsmyth.data.model.GameRulesDataModel;
import com.keithsmyth.data.model.RoundRulesDataModel;

public class GameRulesProvider {

    public static final int MIN_PLAYERS = 5;
    public static final int MAX_PLAYERS = 10;

    private static final GameRulesDataModel INVALID_GAME = new GameRulesDataModel(0, 0, new RoundRulesDataModel[0]);

    private final GameRulesDataModel gameFive = new GameRulesDataModel(3, 2, new RoundRulesDataModel[]{
        new RoundRulesDataModel(1, 2, 1),
            new RoundRulesDataModel(2, 3, 1),
            new RoundRulesDataModel(3, 2, 1),
            new RoundRulesDataModel(4, 3, 1),
            new RoundRulesDataModel(5, 3, 1)
    });

    private final GameRulesDataModel gameSix = new GameRulesDataModel(4, 2, new RoundRulesDataModel[]{
        new RoundRulesDataModel(1, 2, 1),
        new RoundRulesDataModel(2, 3, 1),
        new RoundRulesDataModel(3, 3, 1),
        new RoundRulesDataModel(4, 3, 1),
        new RoundRulesDataModel(5, 4, 1)
    });

    private final GameRulesDataModel gameSeven = new GameRulesDataModel(4, 3, new RoundRulesDataModel[]{
        new RoundRulesDataModel(1, 2, 1),
        new RoundRulesDataModel(2, 3, 1),
        new RoundRulesDataModel(3, 3, 1),
        new RoundRulesDataModel(4, 4, 2),
        new RoundRulesDataModel(5, 4, 1)
    });

    private final GameRulesDataModel gameEight = new GameRulesDataModel(5, 3, new RoundRulesDataModel[]{
        new RoundRulesDataModel(1, 3, 1),
        new RoundRulesDataModel(2, 4, 1),
        new RoundRulesDataModel(3, 4, 1),
        new RoundRulesDataModel(4, 5, 2),
        new RoundRulesDataModel(5, 5, 1)
    });

    private final GameRulesDataModel gameNine = new GameRulesDataModel(6, 3, new RoundRulesDataModel[]{
        new RoundRulesDataModel(1, 3, 1),
        new RoundRulesDataModel(2, 4, 1),
        new RoundRulesDataModel(3, 4, 1),
        new RoundRulesDataModel(4, 5, 2),
        new RoundRulesDataModel(5, 5, 1)
    });

    private final GameRulesDataModel gameTen = new GameRulesDataModel(6, 4, new RoundRulesDataModel[]{
        new RoundRulesDataModel(1, 3, 1),
        new RoundRulesDataModel(2, 4, 1),
        new RoundRulesDataModel(3, 4, 1),
        new RoundRulesDataModel(4, 5, 2),
        new RoundRulesDataModel(5, 5, 1)
    });

    public GameRulesDataModel getGameRules(int numberOfPlayers) {
        switch (numberOfPlayers) {
            case 5:
                return gameFive;
            case 6:
                return gameSix;
            case 7:
                return gameSeven;
            case 8:
                return gameEight;
            case 9:
                return gameNine;
            case 10:
                return gameTen;
            default:
                return INVALID_GAME;
        }
    }
}
