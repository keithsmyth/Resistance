package com.keithsmyth.resistance;

import android.app.Application;

import com.keithsmyth.resistance.data.CharacterProvider;
import com.keithsmyth.resistance.data.GameProvider;
import com.keithsmyth.resistance.data.GameRulesProvider;
import com.keithsmyth.resistance.data.UserProvider;
import com.keithsmyth.resistance.data.prefs.SharedPreferencesWrapper;
import com.keithsmyth.resistance.feature.lobby.domain.AddPlayerUseCase;
import com.keithsmyth.resistance.feature.lobby.domain.SelectCharactersUseCase;
import com.keithsmyth.resistance.navigation.Navigation;
import com.keithsmyth.resistance.feature.welcome.domain.JoinGameUseCase;
import com.keithsmyth.resistance.feature.welcome.domain.NewGameUseCase;
import com.keithsmyth.resistance.feature.welcome.domain.RestorePreferencesUseCase;

public class Injector {

    private static Application application;
    private static Navigation navigation;
    private static SharedPreferencesWrapper preferencesWrapper;
    private static UserProvider userProvider;
    private static GameProvider gameProvider;
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

    public static UserProvider userProvider() {
        if (userProvider == null) {
            userProvider = new UserProvider(sharedPreferencesWrapper());
        }
        return userProvider;
    }

    public static GameProvider gameProvider() {
        if (gameProvider == null) {
            gameProvider = new GameProvider(sharedPreferencesWrapper(), userProvider());
        }
        return gameProvider;
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
        return new RestorePreferencesUseCase(userProvider(), gameProvider());
    }

    public static NewGameUseCase newGameUseCase() {
        return new NewGameUseCase(navigation(), userProvider(), gameProvider());
    }

    public static JoinGameUseCase joinGameUseCase() {
        return new JoinGameUseCase(navigation(), userProvider(), gameProvider());
    }

    public static AddPlayerUseCase addPlayerUseCase() {
        return new AddPlayerUseCase(userProvider());
    }

    public static SelectCharactersUseCase selectCharactersUseCase() {
        return new SelectCharactersUseCase(navigation(), userProvider(), gameProvider(), characterProvider(), gameRulesProvider());
    }
}
