package com.keithsmyth.resistance.data;

import android.text.TextUtils;

import com.keithsmyth.resistance.data.model.PlayerDataModel;
import com.keithsmyth.resistance.data.prefs.SharedPreferencesWrapper;

import java.util.UUID;

public class UserProvider {

    public static final int NO_GAME_ID = -1;
    public static final PlayerDataModel NO_PLAYER = new PlayerDataModel(null, null, NO_GAME_ID);

    private static final String PLAYER_UUID = "player-uuid";
    private static final String PLAYER_NAME = "player-name";
    private static final String PLAYER_GAME = "player-game";

    private final SharedPreferencesWrapper prefs;

    public UserProvider(SharedPreferencesWrapper prefs) {
        this.prefs = prefs;
        initPlayerUuid();
    }

    private void initPlayerUuid() {
        if (!prefs.get().contains(PLAYER_UUID)) {
            prefs.get().edit().putString(PLAYER_UUID, UUID.randomUUID().toString()).apply();
        }
    }

    public PlayerDataModel getPlayer() {
        final String uuid = prefs.get().getString(PLAYER_UUID, null);
        final String name = prefs.get().getString(PLAYER_NAME, null);
        final int gameId = prefs.get().getInt(PLAYER_GAME, NO_GAME_ID);
        if (TextUtils.isEmpty(uuid) || TextUtils.isEmpty(name)) {
            return NO_PLAYER;
        }
        return new PlayerDataModel(uuid, name, gameId);
    }

    public void setPlayer(String name) {
        prefs.get().edit().putString(PLAYER_NAME, name).apply();
    }

    public void setGameId(int gameId) {
        prefs.get().edit().putInt(PLAYER_GAME, gameId).apply();
    }
}
