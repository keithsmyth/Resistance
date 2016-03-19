package com.keithsmyth.resistance.lobby.exception;

import android.content.Context;

import com.keithsmyth.resistance.R;
import com.keithsmyth.resistance.navigation.DisplayThrowable;

public class NumberPlayersException extends DisplayThrowable {

    public final int minPlayers;
    public final int maxPlayers;
    public final int currentPlayers;

    public NumberPlayersException(int minPlayers, int maxPlayers, int currentPlayers) {
        this.minPlayers = minPlayers;
        this.maxPlayers = maxPlayers;
        this.currentPlayers = currentPlayers;
    }

    @Override
    public String getDisplayMessage(Context context) {
        return context.getString(R.string.lobby_number_players_error, minPlayers, maxPlayers, currentPlayers);
    }
}
