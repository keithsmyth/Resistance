package com.keithsmyth.resistance.welcome.domain;

import com.keithsmyth.resistance.data.PlayerProvider;
import com.keithsmyth.resistance.data.model.PlayerDataModel;

public class RestorePreferencesUseCase {

    public static final String NO_NAME = "";

    private final PlayerProvider playerProvider;

    public RestorePreferencesUseCase(PlayerProvider playerProvider) {
        this.playerProvider = playerProvider;
    }

    public String getName() {
        final PlayerDataModel player = playerProvider.getPlayer();
        return (player != PlayerProvider.NO_PLAYER) ? player.name : NO_NAME;
    }
}
