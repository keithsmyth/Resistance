package com.keithsmyth.resistance;

import android.app.Application;

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Injector.setApplication(this);
    }
}
