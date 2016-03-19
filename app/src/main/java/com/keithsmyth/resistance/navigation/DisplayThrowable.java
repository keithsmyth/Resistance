package com.keithsmyth.resistance.navigation;

import android.content.Context;

public abstract class DisplayThrowable extends Throwable {

    public abstract String getDisplayMessage(Context context);
}
