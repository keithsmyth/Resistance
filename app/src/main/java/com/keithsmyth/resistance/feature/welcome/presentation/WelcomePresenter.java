package com.keithsmyth.resistance.feature.welcome.presentation;

import android.text.TextUtils;

import com.keithsmyth.resistance.Injector;
import com.keithsmyth.resistance.presentation.Presenter;
import com.keithsmyth.resistance.presentation.PresenterFactory;
import com.keithsmyth.resistance.feature.welcome.domain.JoinGameUseCase;
import com.keithsmyth.resistance.feature.welcome.domain.NewGameUseCase;
import com.keithsmyth.resistance.feature.welcome.domain.RestorePreferencesUseCase;

public class WelcomePresenter implements Presenter<WelcomeView> {

    private final RestorePreferencesUseCase restorePreferencesUseCase;
    private final NewGameUseCase newGameUseCase;
    private final JoinGameUseCase joinGameUseCase;

    private WelcomeView welcomeView;

    private WelcomePresenter(RestorePreferencesUseCase restorePreferencesUseCase, NewGameUseCase newGameUseCase, JoinGameUseCase joinGameUseCase) {
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
        if (isNameInvalid(name)) {
            welcomeView.showNameError();
            return;
        }
        welcomeView.setLoadingState(true);
        newGameUseCase.execute(name);
    }

    public void joinGame() {
        welcomeView.clearErrors();
        final String name = welcomeView.getNameInput();
        final int gameId = parseGameId(welcomeView.getGameIdInput());

        if (isNameInvalid(name)) {
            welcomeView.showNameError();
            return;
        }
        if (isGameIdInvalid(gameId)) {
            welcomeView.showGameIdError();
            return;
        }

        welcomeView.setLoadingState(true);
        joinGameUseCase.execute(gameId, name);
    }

    public void onErrorShown() {
        welcomeView.setLoadingState(false);
    }

    private boolean isNameInvalid(String name) {
        return TextUtils.isEmpty(name);
    }

    private boolean isGameIdInvalid(int gameId) {
        return gameId < 1;
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
