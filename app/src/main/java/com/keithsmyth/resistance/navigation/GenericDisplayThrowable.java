package com.keithsmyth.resistance.navigation;

import android.content.Context;
import android.util.Log;

import com.keithsmyth.resistance.R;

public class GenericDisplayThrowable extends DisplayThrowable {

    private static final String TAG = "GenericDisplayThrowable";

    public GenericDisplayThrowable(Throwable throwable) {
        Log.e(TAG, throwable.getMessage(), throwable);
    }

    @Override
    public String getDisplayMessage(Context context) {
        return context.getString(R.string.unexpected_error);
    }
}
