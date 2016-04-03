package com.keithsmyth.resistance.feature.lobby.exception;

import android.content.Context;

import com.keithsmyth.resistance.R;
import com.keithsmyth.resistance.navigation.DisplayThrowable;

public class NoSuicideThrowable extends DisplayThrowable {

    @Override
    public String getDisplayMessage(Context context) {
        return context.getString(R.string.lobby_no_suicide_error);
    }
}
