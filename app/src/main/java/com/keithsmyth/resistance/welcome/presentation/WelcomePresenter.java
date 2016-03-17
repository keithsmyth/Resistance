package com.keithsmyth.resistance.welcome.presentation;

import android.text.TextUtils;

import com.keithsmyth.resistance.Injector;
import com.keithsmyth.resistance.Presenter;
import com.keithsmyth.resistance.PresenterFactory;
import com.keithsmyth.resistance.RxUtil;
import com.keithsmyth.resistance.welcome.domain.JoinGameUseCase;
import com.keithsmyth.resistance.welcome.domain.NewGameUseCase;
import com.keithsmyth.resistance.welcome.domain.RestorePreferencesUseCase;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

public class WelcomePresenter implements Presenter<WelcomeView> {

    private final RestorePreferencesUseCase restorePreferencesUseCase;
    private final NewGameUseCase newGameUseCase;
    private final JoinGameUseCase joinGameUseCase;

    private WelcomeView welcomeView;
    private Subscription gameIdSubscription;

    public WelcomePresenter(RestorePreferencesUseCase restorePreferencesUseCase, NewGameUseCase newGameUseCase, JoinGameUseCase joinGameUseCase) {
        this.restorePreferencesUseCase = restorePreferencesUseCase;
        this.newGameUseCase = newGameUseCase;
        this.joinGameUseCase = joinGameUseCase;
    }

    @Override
    public void attachView(WelcomeView welcomeView) {
        this.welcomeView = welcomeView;
        setDefaultName();
    }

    @Override
    public void detachView() {
        welcomeView = null;
    }

    @Override
    public void onDestroyed() {
        RxUtil.unsubscribe(gameIdSubscription);
        detachView();
    }

    public void newGame() {
        welcomeView.clearErrors();
        final String name = welcomeView.getNameInput();
        if (!validateName(name)) {
            return;
        }
        welcomeView.setLoadingState(true);
        RxUtil.unsubscribe(gameIdSubscription);
        gameIdSubscription = newGameUseCase.execute(name)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(openGameAction, errorAction);
    }

    public void joinGame() {
        welcomeView.clearErrors();
        final String name = welcomeView.getNameInput();
        final int gameId = parseGameId(welcomeView.getGameIdInput());
        if (!validateName(name) | !validateGameId(gameId)) {
            return;
        }
        welcomeView.setLoadingState(true);
        RxUtil.unsubscribe(gameIdSubscription);
        gameIdSubscription = joinGameUseCase.execute(gameId, name)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(openGameAction, errorAction);
    }

    private void setDefaultName() {
        if (TextUtils.isEmpty(welcomeView.getNameInput())) {
            welcomeView.setName(restorePreferencesUseCase.getName());
        }
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

    private final Action1<Integer> openGameAction = new Action1<Integer>() {
        @Override
        public void call(Integer gameId) {
            RxUtil.unsubscribe(gameIdSubscription);
            welcomeView.setLoadingState(false);
            welcomeView.openLobby(gameId);
        }
    };

    private final Action1<Throwable> errorAction = new Action1<Throwable>() {
        @Override
        public void call(Throwable throwable) {
            RxUtil.unsubscribe(gameIdSubscription);
            welcomeView.setLoadingState(false);
            welcomeView.showError(throwable.getMessage());
        }
    };

    public static final PresenterFactory<WelcomePresenter> FACTORY = new PresenterFactory<WelcomePresenter>() {
        @Override
        public WelcomePresenter create() {
            return new WelcomePresenter(Injector.restorePreferencesUseCase(), Injector.newGameUseCase(), Injector.joinGameUseCase());
        }
    };
}
