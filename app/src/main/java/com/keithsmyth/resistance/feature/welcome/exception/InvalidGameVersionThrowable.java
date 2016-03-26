package com.keithsmyth.resistance.feature.welcome.exception;

import android.content.Context;

import com.keithsmyth.resistance.R;
import com.keithsmyth.resistance.navigation.DisplayThrowable;

public class InvalidGameVersionThrowable extends DisplayThrowable {

    private final String requiredVersion;
    private final String clientVersion;

    public InvalidGameVersionThrowable(String requiredVersion, String clientVersion) {
        this.requiredVersion = requiredVersion;
        this.clientVersion = clientVersion;
    }

    @Override
    public String getDisplayMessage(Context context) {
        return context.getString(R.string.welcome_invalid_version_error, requiredVersion, clientVersion);
    }
}
