package com.keithsmyth.resistance.feature.lobby.exception;

import android.content.Context;

import com.keithsmyth.resistance.R;
import com.keithsmyth.resistance.navigation.DisplayThrowable;

public class InvalidStatusException extends DisplayThrowable {

    @Override
    public String getDisplayMessage(Context context) {
        return context.getString(R.string.lobby_invalid_status_error);
    }
}
