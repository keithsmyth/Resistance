package com.keithsmyth.resistance.feature.lobby.exception;

import android.content.Context;

import com.keithsmyth.resistance.R;
import com.keithsmyth.resistance.navigation.DisplayThrowable;

import static com.keithsmyth.data.provider.GameRulesProvider.MAX_PLAYERS;
import static com.keithsmyth.data.provider.GameRulesProvider.MIN_PLAYERS;

public class NumberPlayersThrowable extends DisplayThrowable {

    private final int currentPlayers;

    public NumberPlayersThrowable(int currentPlayers) {
        this.currentPlayers = currentPlayers;
    }

    @Override
    public String getDisplayMessage(Context context) {
        return context.getString(R.string.lobby_number_players_error, MIN_PLAYERS, MAX_PLAYERS, currentPlayers);
    }
}
