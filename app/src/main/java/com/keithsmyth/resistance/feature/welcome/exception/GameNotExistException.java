package com.keithsmyth.resistance.feature.welcome.exception;

import android.content.Context;

import com.keithsmyth.resistance.navigation.DisplayThrowable;
import com.keithsmyth.resistance.R;

public class GameNotExistException extends DisplayThrowable {

    @Override
    public String getDisplayMessage(Context context) {
        return context.getString(R.string.welcome_game_exist_error);
    }
}
