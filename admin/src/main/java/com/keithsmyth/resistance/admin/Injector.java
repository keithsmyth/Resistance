package com.keithsmyth.resistance.admin;

import android.app.Application;

import com.keithsmyth.data.firebase.FirebaseFactory;
import com.keithsmyth.data.prefs.SharedPreferencesWrapper;
import com.keithsmyth.data.provider.CharacterProvider;
import com.keithsmyth.data.provider.GameInfoProvider;
import com.keithsmyth.data.provider.GamePlayProvider;
import com.keithsmyth.data.provider.GameProvider;
import com.keithsmyth.data.provider.GameRulesProvider;
import com.keithsmyth.data.provider.UserProvider;

public class Injector {

    private static Application application;
    private static SharedPreferencesWrapper preferencesWrapper;
    private static FirebaseFactory firebaseFactory;
    private static UserProvider userProvider;
    private static GameProvider gameProvider;
    private static GameInfoProvider gameInfoProvider;
    private static GamePlayProvider gamePlayProvider;
    private static CharacterProvider characterProvider;
    private static GameRulesProvider gameRulesProvider;

    public static void setApplication(Application application) {
        Injector.application = application;
    }

    private static SharedPreferencesWrapper sharedPreferencesWrapper() {
        if (preferencesWrapper == null) {
            preferencesWrapper = new SharedPreferencesWrapper(application);
        }
        return preferencesWrapper;
    }

    private static FirebaseFactory firebaseFactory() {
        if (firebaseFactory == null) {
            firebaseFactory = new FirebaseFactory();
        }
        return firebaseFactory;
    }

    private static UserProvider userProvider() {
        if (userProvider == null) {
            userProvider = new UserProvider(sharedPreferencesWrapper());
        }
        return userProvider;
    }

    public static GameProvider gameProvider() {
        if (gameProvider == null) {
            gameProvider = new GameProvider(firebaseFactory(), gameInfoProvider());
        }
        return gameProvider;
    }

    public static GameInfoProvider gameInfoProvider() {
        if (gameInfoProvider == null) {
            gameInfoProvider = new GameInfoProvider(sharedPreferencesWrapper(), firebaseFactory(), userProvider());
        }
        return gameInfoProvider;
    }

    public static GamePlayProvider gamePlayProvider() {
        if (gamePlayProvider == null) {
            gamePlayProvider = new GamePlayProvider(gameInfoProvider(), firebaseFactory(), userProvider());
        }
        return gamePlayProvider;
    }

    private static CharacterProvider characterProvider() {
        if (characterProvider == null) {
            characterProvider = new CharacterProvider();
        }
        return characterProvider;
    }

    private static GameRulesProvider gameRulesProvider() {
        if (gameRulesProvider == null) {
            gameRulesProvider = new GameRulesProvider();
        }
        return gameRulesProvider;
    }

}
