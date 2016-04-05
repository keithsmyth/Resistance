package com.keithsmyth.resistance;

import android.app.Application;

import com.keithsmyth.data.provider.CharacterProvider;
import com.keithsmyth.data.provider.GameInfoProvider;
import com.keithsmyth.data.provider.GamePlayProvider;
import com.keithsmyth.data.provider.GameProvider;
import com.keithsmyth.data.provider.GameRulesProvider;
import com.keithsmyth.data.provider.UserProvider;
import com.keithsmyth.data.firebase.FirebaseFactory;
import com.keithsmyth.data.prefs.SharedPreferencesWrapper;
import com.keithsmyth.resistance.feature.game.domain.DisplayCharacterUseCase;
import com.keithsmyth.resistance.feature.game.domain.GameStateUseCase;
import com.keithsmyth.resistance.feature.lobby.domain.AddPlayerUseCase;
import com.keithsmyth.resistance.feature.lobby.domain.SelectCharactersUseCase;
import com.keithsmyth.resistance.feature.lobby.domain.StartGameUseCase;
import com.keithsmyth.resistance.feature.lobby.domain.WatchLobbyStateUseCase;
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
    private static GameProvider gameProvider;
    private static GameInfoProvider gameInfoProvider;
    private static GamePlayProvider gamePlayProvider;
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
        return new AddPlayerUseCase(navigation(), userProvider(), gameInfoProvider());
    }

    public static SelectCharactersUseCase selectCharactersUseCase() {
        return new SelectCharactersUseCase(navigation(), gameInfoProvider(), characterProvider(), gameRulesProvider());
    }

    public static DisplayCharacterUseCase displayCharacterUseCase() {
        return new DisplayCharacterUseCase(userProvider(), gameInfoProvider(), characterProvider());
    }

    public static WatchLobbyStateUseCase watchLobbyStateUseCase() {
        return new WatchLobbyStateUseCase(navigation(), gameInfoProvider());
    }

    public static StartGameUseCase startGameUseCase() {
        return new StartGameUseCase(gameInfoProvider(), gamePlayProvider());
    }

    public static GameStateUseCase gameStateUseCase() {
        return new GameStateUseCase(gameInfoProvider(), gamePlayProvider());
    }
}
