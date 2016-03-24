package com.keithsmyth.resistance.data;

import com.keithsmyth.resistance.BuildConfig;
import com.keithsmyth.resistance.data.prefs.SharedPreferencesWrapper;

import java.util.UUID;

public class UserProvider {

    public static final int NO_GAME_ID = -1;

    private static final String PLAYER_UUID = "player-uuid";
    private static final String PLAYER_NAME = "player-name";
    private static final String PLAYER_GAME = "player-game";

    private final SharedPreferencesWrapper prefs;

    public UserProvider(SharedPreferencesWrapper prefs) {
        this.prefs = prefs;
        initId();
    }

    private void initId() {
        if (!prefs.get().contains(PLAYER_UUID)) {
            prefs.get().edit().putString(PLAYER_UUID, UUID.randomUUID().toString()).apply();
        }
    }

    public String getId() {
        return prefs.get().getString(PLAYER_UUID, null);
    }

    public String getName() {
        return prefs.get().getString(PLAYER_NAME, null);
    }

    public void setName(String name) {
        prefs.get().edit().putString(PLAYER_NAME, name).apply();
    }

    public int getGameId() {
        return prefs.get().getInt(PLAYER_GAME, NO_GAME_ID);
    }

    public void setGameId(int gameId) {
        prefs.get().edit().putInt(PLAYER_GAME, gameId).apply();
    }

    public String getClientVersion() {
        return BuildConfig.VERSION_NAME;
    }
}
