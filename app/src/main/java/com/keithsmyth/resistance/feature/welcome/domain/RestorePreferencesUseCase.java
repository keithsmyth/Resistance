package com.keithsmyth.resistance.feature.welcome.domain;

import com.keithsmyth.resistance.data.GameProvider;
import com.keithsmyth.resistance.data.UserProvider;
import com.keithsmyth.resistance.data.model.PlayerDataModel;

public class RestorePreferencesUseCase {

    public static final String NO_NAME = "";
    public static final int NO_GAME = -1;

    private final UserProvider userProvider;
    private final GameProvider gameProvider;

    public RestorePreferencesUseCase(UserProvider userProvider, GameProvider gameProvider) {
        this.userProvider = userProvider;
        this.gameProvider = gameProvider;
    }

    public void resetActiveGame() {
        gameProvider.clearActiveGameId();
    }

    public String getName() {
        final PlayerDataModel player = userProvider.getPlayer();
        return (player != UserProvider.NO_PLAYER) ? player.name : NO_NAME;
    }

    public int getGame() {
        final PlayerDataModel player = userProvider.getPlayer();
        return (player != UserProvider.NO_PLAYER) ? player.gameId : NO_GAME;
    }
}
