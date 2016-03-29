package com.keithsmyth.resistance.feature.lobby.domain;

import com.keithsmyth.resistance.RxUtil;
import com.keithsmyth.data.provider.GameInfoProvider;
import com.keithsmyth.resistance.feature.lobby.exception.InvalidStatusThrowable;
import com.keithsmyth.resistance.navigation.GenericDisplayThrowable;
import com.keithsmyth.resistance.navigation.Navigation;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class WatchLobbyStateUseCase {

    private final Navigation navigation;
    private final GameInfoProvider gameInfoProvider;

    private Subscription subscription;

    public WatchLobbyStateUseCase(Navigation navigation, GameInfoProvider gameInfoProvider) {
        this.navigation = navigation;
        this.gameInfoProvider = gameInfoProvider;
    }

    public void execute() {
        RxUtil.unsubscribe(subscription);
        subscription = gameInfoProvider.watchGameState()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Action1<Integer>() {
                @Override
                public void call(Integer gameState) {
                    onGameStateUpdated(gameState);
                }
            }, new Action1<Throwable>() {
                @Override
                public void call(Throwable throwable) {
                    navigation.showError(new GenericDisplayThrowable(throwable));
                }
            });
    }

    public void destroy() {
        RxUtil.unsubscribe(subscription);
    }

    private void onGameStateUpdated(@GameInfoProvider.GameState int gameState) {
        switch (gameState) {
            case GameInfoProvider.STATE_NONE:
                navigation.openWelcome();
                navigation.showError(new InvalidStatusThrowable());
                break;
            case GameInfoProvider.STATE_NEW:
            case GameInfoProvider.STATE_STARTING:
                // no op
                break;
            case GameInfoProvider.STATE_STARTED:
                navigation.openGame();
                break;
            case GameInfoProvider.STATE_FINISHED:
                navigation.openEnd();
                break;
        }
    }
}
