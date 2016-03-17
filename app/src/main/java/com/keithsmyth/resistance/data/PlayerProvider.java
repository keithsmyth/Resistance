package com.keithsmyth.resistance.data;

import android.app.Application;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.keithsmyth.resistance.data.model.PlayerDataModel;

import java.util.UUID;

public class PlayerProvider {

    public static final int NO_GAME_ID = -1;
    public static final PlayerDataModel NO_PLAYER = new PlayerDataModel(null, null, NO_GAME_ID);

    private static final String PREFS_NAME = "AvalonSharedPrefs";
    private static final String PLAYER_UUID = "player-uuid";
    private static final String PLAYER_NAME = "player-name";
    private static final String PLAYER_GAME = "player-game";

    private final SharedPreferences prefs;

    public PlayerProvider(Application application) {
        prefs = application.getSharedPreferences(PREFS_NAME, 0);
        initPlayerUuid();
    }

    private void initPlayerUuid() {
        if (!prefs.contains(PLAYER_UUID)) {
            prefs.edit().putString(PLAYER_UUID, UUID.randomUUID().toString()).apply();
        }
    }

    public PlayerDataModel getPlayer() {
        final String uuid = prefs.getString(PLAYER_UUID, null);
        final String name = prefs.getString(PLAYER_NAME, null);
        final int gameId = prefs.getInt(PLAYER_GAME, NO_GAME_ID);
        if (TextUtils.isEmpty(uuid) || TextUtils.isEmpty(name)) {
            return NO_PLAYER;
        }
        return new PlayerDataModel(uuid, name, gameId);
    }

    public void setPlayer(String name) {
        prefs.edit().putString(PLAYER_NAME, name).apply();
    }
}
