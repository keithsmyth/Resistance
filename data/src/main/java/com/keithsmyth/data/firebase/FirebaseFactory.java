package com.keithsmyth.data.firebase;

import com.firebase.client.Firebase;
import com.keithsmyth.data.BuildConfig;

public class FirebaseFactory {

    private static final String FIREBASE_URL = BuildConfig.UNIQUE_FIREBASE_ROOT_URL;

    private static final String ACTIVE_GAMES_PATH = "/activeGames";
    private static final String GAME_PATH = "/games/%s";
    private static final String GAME_INFO_PATH = "/games/%s/info";
    private static final String GAME_STATE_PATH = "/games/%s/info/status";
    private static final String GAME_PLAYERS_PATH = "/games/%s/info/mapPlayerIdToName";
    private static final String GAME_PLAYER_ADDED_PATH = "/games/%s/info/mapPlayerIdToName/%s";
    private static final String GAME_OWNER_PATH = "/games/%s/info/ownerId";
    private static final String GAME_ASSIGNED_CHARACTERS_PATH = "/games/%s/info/mapPlayerIdToCharacter";
    private static final String GAME_PLAYER_ORDER_PATH = "/games/%s/info/mapPlayerIdToOrder";
    private static final String GAME_PLAY_PATH = "/games/%s/play";

    public Firebase getActiveGamesRef() {
        return new Firebase(FIREBASE_URL + ACTIVE_GAMES_PATH);
    }

    public Firebase getGameRef(int gameId) {
        return new Firebase(FIREBASE_URL + String.format(GAME_PATH, gameId));
    }

    public Firebase getGameInfoRef(int gameId) {
        return new Firebase(FIREBASE_URL + String.format(GAME_INFO_PATH, gameId));
    }

    public Firebase getGameStateRef(int gameId) {
        return new Firebase(FIREBASE_URL + String.format(GAME_STATE_PATH, gameId));
    }

    public Firebase getPlayersRef(int gameId) {
        return new Firebase(FIREBASE_URL + String.format(GAME_PLAYERS_PATH, gameId));
    }

    public Firebase getOwnerRef(int gameId) {
        return new Firebase(FIREBASE_URL + String.format(GAME_OWNER_PATH, gameId));
    }

    public Firebase getPlayerAddedRef(int gameId, String userId) {
        return new Firebase(FIREBASE_URL + String.format(GAME_PLAYER_ADDED_PATH, gameId, userId));
    }

    public Firebase getAssignedCharactersRef(int gameId) {
        return new Firebase(FIREBASE_URL + String.format(GAME_ASSIGNED_CHARACTERS_PATH, gameId));
    }

    public Firebase getPlayerOrderRef(int gameId) {
        return new Firebase(FIREBASE_URL + String.format(GAME_PLAYER_ORDER_PATH, gameId));
    }

    public Firebase getGamePlayRef(int gameId) {
        return new Firebase(FIREBASE_URL + String.format(GAME_PLAY_PATH, gameId));
    }
}
