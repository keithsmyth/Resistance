package com.keithsmyth.resistance;

import android.app.Application;

import com.keithsmyth.resistance.data.CharacterProvider;
import com.keithsmyth.resistance.data.GameRulesProvider;
import com.keithsmyth.resistance.data.PlayerProvider;
import com.keithsmyth.resistance.lobby.domain.AddPlayerUseCase;
import com.keithsmyth.resistance.lobby.domain.SelectCharactersUseCase;
import com.keithsmyth.resistance.welcome.domain.JoinGameUseCase;
import com.keithsmyth.resistance.welcome.domain.NewGameUseCase;
import com.keithsmyth.resistance.welcome.domain.RestorePreferencesUseCase;

public class Injector {

    private static Application application;
    private static PlayerProvider playerProvider;
    private static CharacterProvider characterProvider;
    private static GameRulesProvider gameRulesProvider;

    public static void setApplication(Application application) {
        Injector.application = application;
    }

    public static PlayerProvider playerProvider() {
        if (playerProvider == null) {
            playerProvider = new PlayerProvider(application);
        }
        return playerProvider;
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
        return new RestorePreferencesUseCase(playerProvider());
    }

    public static NewGameUseCase newGameUseCase() {
        return new NewGameUseCase(playerProvider());
    }

    public static JoinGameUseCase joinGameUseCase() {
        return new JoinGameUseCase(playerProvider());
    }

    public static AddPlayerUseCase addPlayerUseCase() {
        return new AddPlayerUseCase(playerProvider());
    }

    public static SelectCharactersUseCase selectCharactersUseCase() {
        return new SelectCharactersUseCase(characterProvider(), gameRulesProvider());
    }
}
