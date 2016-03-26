package com.keithsmyth.resistance.feature.welcome.domain;

import com.keithsmyth.resistance.data.GameInfoProvider;
import com.keithsmyth.resistance.data.UserProvider;

public class RestorePreferencesUseCase {

    public static final int NO_GAME = -1;

    private static final String NO_NAME = "";

    private final UserProvider userProvider;
    private final GameInfoProvider gameInfoProvider;

    public RestorePreferencesUseCase(UserProvider userProvider, GameInfoProvider gameInfoProvider) {
        this.userProvider = userProvider;
        this.gameInfoProvider = gameInfoProvider;
    }

    public void resetActiveGame() {
        gameInfoProvider.clearCurrentGameId();
    }

    public String getName() {
        final String name = userProvider.getName();
        return name == null ? NO_NAME : name;
    }

    public int getGame() {
        final int gameId = userProvider.getGameId();
        return gameId == UserProvider.NO_GAME_ID ? NO_GAME : gameId;
    }
}
