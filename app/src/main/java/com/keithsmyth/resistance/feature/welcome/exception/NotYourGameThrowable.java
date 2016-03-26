package com.keithsmyth.resistance.feature.welcome.exception;

import android.content.Context;

import com.keithsmyth.resistance.R;
import com.keithsmyth.resistance.navigation.DisplayThrowable;

public class NotYourGameThrowable extends DisplayThrowable {

    @Override
    public String getDisplayMessage(Context context) {
        return context.getString(R.string.welcome_not_your_game_error);
    }
}
