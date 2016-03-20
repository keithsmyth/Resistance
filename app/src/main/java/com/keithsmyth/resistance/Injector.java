package com.keithsmyth.resistance;

import android.app.Application;

import com.keithsmyth.resistance.data.CharacterProvider;
import com.keithsmyth.resistance.data.GameInfoProvider;
import com.keithsmyth.resistance.data.GameRulesProvider;
import com.keithsmyth.resistance.data.UserProvider;
import com.keithsmyth.resistance.data.firebase.FirebaseFactory;
import com.keithsmyth.resistance.data.prefs.SharedPreferencesWrapper;
import com.keithsmyth.resistance.feature.lobby.domain.AddPlayerUseCase;
import com.keithsmyth.resistance.feature.lobby.domain.SelectCharactersUseCase;
import com.keithsmyth.resistance.feature.welcome.domain.JoinGameUseCase;
import com.keithsmyth.resistance.feature.welcome.domain.NewGameUseCase;
import com.keithsmyth.resistance.feature.welcome.domain.RestorePreferencesUseCase;
import com.keithsmyth.resistance.navigation.Navigation;

public class Injector {

    private static Application application;
    private static Navigation navigation;
    private static SharedPreferencesWrapper preferencesWrapper;
    private static FirebaseFactory firebaseFactory;
    private static UserProvider userProvider;
    private static GameInfoProvider gameInfoProvider;
    private static CharacterProvider characterProvider;
    private static GameRulesProvider gameRulesProvider;

    public static void setApplication(Application application) {
        Injector.application = application;
    }

    public static Navigation navigation() {
        if (navigation == null) {
            navigation = new Navigation();
        }
        return navigation;
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

    public static UserProvider userProvider() {
        if (userProvider == null) {
            userProvider = new UserProvider(sharedPreferencesWrapper());
        }
        return userProvider;
    }

    public static GameInfoProvider gameInfoProvider() {
        if (gameInfoProvider == null) {
            gameInfoProvider = new GameInfoProvider(sharedPreferencesWrapper(), firebaseFactory(), userProvider());
        }
        return gameInfoProvider;
    }

    public static CharacterProvider characterProvider() {
        if (characterProvider == null) {
            characterProvider = new CharacterProvider();
        }
        return characterProvider;
    }

    public static GameRulesProvider gameRulesProvider() {
        if (gameRulesProvider == null) {
            gameRulesProvider = new GameRulesProvider();
        }
        return gameRulesProvider;
    }

    public static RestorePreferencesUseCase restorePreferencesUseCase() {
        return new RestorePreferencesUseCase(userProvider(), gameInfoProvider());
    }

    public static NewGameUseCase newGameUseCase() {
        return new NewGameUseCase(navigation(), userProvider(), gameInfoProvider());
    }

    public static JoinGameUseCase joinGameUseCase() {
        return new JoinGameUseCase(navigation(), userProvider(), gameInfoProvider());
    }

    public static AddPlayerUseCase addPlayerUseCase() {
        return new AddPlayerUseCase(userProvider(), gameInfoProvider());
    }

    public static SelectCharactersUseCase selectCharactersUseCase() {
        return new SelectCharactersUseCase(navigation(), userProvider(), gameInfoProvider(), characterProvider(), gameRulesProvider());
    }
}
