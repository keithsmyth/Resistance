package com.keithsmyth.resistance.data.firebase;

import com.firebase.client.Firebase;
import com.keithsmyth.resistance.BuildConfig;

public class FirebaseFactory {

    private static final String FIREBASE_URL = BuildConfig.UNIQUE_FIREBASE_ROOT_URL;

    private static final String ACTIVE_GAMES_PATH = "/activeGames";
    private static final String GAME_INFO_PATH = "/games/%s/info";

    public Firebase getActiveGamesRef() {
        return new Firebase(FIREBASE_URL + ACTIVE_GAMES_PATH);
    }

    public Firebase getGameRef(int gameId) {
        return new Firebase(FIREBASE_URL + String.format(GAME_INFO_PATH, gameId));
    }
}
