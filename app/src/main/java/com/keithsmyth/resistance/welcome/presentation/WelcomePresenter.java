package com.keithsmyth.resistance.welcome.presentation;

import android.text.TextUtils;

import com.keithsmyth.resistance.Injector;
import com.keithsmyth.resistance.Presenter;
import com.keithsmyth.resistance.PresenterFactory;
import com.keithsmyth.resistance.welcome.domain.JoinGameUseCase;
import com.keithsmyth.resistance.welcome.domain.NewGameUseCase;
import com.keithsmyth.resistance.welcome.domain.RestorePreferencesUseCase;

public class WelcomePresenter implements Presenter<WelcomeView> {

    private final RestorePreferencesUseCase restorePreferencesUseCase;
    private final NewGameUseCase newGameUseCase;
    private final JoinGameUseCase joinGameUseCase;

    private WelcomeView welcomeView;

    public WelcomePresenter(RestorePreferencesUseCase restorePreferencesUseCase, NewGameUseCase newGameUseCase, JoinGameUseCase joinGameUseCase) {
        this.restorePreferencesUseCase = restorePreferencesUseCase;
        this.newGameUseCase = newGameUseCase;
        this.joinGameUseCase = joinGameUseCase;
    }

    @Override
    public void attachView(WelcomeView welcomeView) {
        this.welcomeView = welcomeView;

        restorePreferencesUseCase.resetActiveGame();

        if (TextUtils.isEmpty(welcomeView.getNameInput())) {
            welcomeView.setName(restorePreferencesUseCase.getName());
        }

        if (TextUtils.isEmpty(welcomeView.getGameIdInput())) {
            final int gameId = restorePreferencesUseCase.getGame();
            if (gameId != RestorePreferencesUseCase.NO_GAME) {
                welcomeView.setGame(gameId);
            }
        }
    }

    @Override
    public void detachView() {
        welcomeView = null;
    }

    @Override
    public void onDestroyed() {
        joinGameUseCase.destroy();
        newGameUseCase.destroy();
        detachView();
    }

    public void newGame() {
        welcomeView.clearErrors();
        final String name = welcomeView.getNameInput();
        if (!validateName(name)) {
            return;
        }
        welcomeView.setLoadingState(true);
        newGameUseCase.execute(name);
    }

    public void joinGame() {
        welcomeView.clearErrors();
        final String name = welcomeView.getNameInput();
        final int gameId = parseGameId(welcomeView.getGameIdInput());
        if (!validateName(name) | !validateGameId(gameId)) {
            return;
        }
        welcomeView.setLoadingState(true);
        joinGameUseCase.execute(gameId, name);
    }

    public void onErrorShown() {
        welcomeView.setLoadingState(false);
    }

    private boolean validateName(String name) {
        final boolean isValid = !TextUtils.isEmpty(name);
        if (!isValid) {
            welcomeView.showNameError();
        }
        return isValid;
    }

    private boolean validateGameId(int gameId) {
        final boolean isValid = gameId > 0;
        if (!isValid) {
            welcomeView.showGameIdError();
        }
        return isValid;
    }

    private int parseGameId(String input) {
        return !TextUtils.isEmpty(input) && TextUtils.isDigitsOnly(input) ? Integer.parseInt(input) : 0;
    }

    public static final PresenterFactory<WelcomePresenter> FACTORY = new PresenterFactory<WelcomePresenter>() {
        @Override
        public WelcomePresenter create() {
            return new WelcomePresenter(Injector.restorePreferencesUseCase(), Injector.newGameUseCase(), Injector.joinGameUseCase());
        }
    };
}
