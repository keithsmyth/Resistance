package com.keithsmyth.resistance.data.prefs;

import android.app.Application;
import android.content.SharedPreferences;

public class SharedPreferencesWrapper {

    private static final String PREFS_NAME = "AvalonSharedPrefs";

    private final SharedPreferences prefs;

    public SharedPreferencesWrapper(Application application) {
        prefs = application.getSharedPreferences(PREFS_NAME, 0);
    }

    public SharedPreferences get() {
        return prefs;
    }
}
