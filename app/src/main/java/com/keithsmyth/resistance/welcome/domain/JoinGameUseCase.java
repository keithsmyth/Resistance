package com.keithsmyth.resistance.welcome.domain;

import com.keithsmyth.resistance.RxUtil;
import com.keithsmyth.resistance.data.GameProvider;
import com.keithsmyth.resistance.data.UserProvider;
import com.keithsmyth.resistance.navigation.GenericDisplayThrowable;
import com.keithsmyth.resistance.navigation.Navigation;
import com.keithsmyth.resistance.welcome.exception.GameNotExistException;

import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class JoinGameUseCase {

    private final Navigation navigation;
    private final UserProvider userProvider;
    private final GameProvider gameProvider;

    private Subscription gameStateSubscription;

    public JoinGameUseCase(Navigation navigation, UserProvider userProvider, GameProvider gameProvider) {
        this.navigation = navigation;
        this.userProvider = userProvider;
        this.gameProvider = gameProvider;
    }

    public void execute(int gameId, String playerName) {
        userProvider.setPlayer(playerName);
        userProvider.setGameId(gameId);

        // check game state, navigate to correct screen
        RxUtil.unsubscribe(gameStateSubscription);
        gameStateSubscription = gameProvider.getGameState()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Action1<Integer>() {
                @Override
                public void call(Integer integer) {
                    RxUtil.unsubscribe(gameStateSubscription);
                    onGameStateReturned(integer);
                }
            }, new Action1<Throwable>() {
                @Override
                public void call(Throwable throwable) {
                    navigation.showError(new GenericDisplayThrowable(throwable));
                }
            });
    }

    public void destroy() {
        RxUtil.unsubscribe(gameStateSubscription);
    }

    private void onGameStateReturned(@GameProvider.GameState int gameState) {
        switch (gameState) {
            case GameProvider.STATE_NONE:
                navigation.showError(new GameNotExistException());
                break;
            case GameProvider.STATE_JOINING:
            case GameProvider.STATE_STARTING:
                navigation.openLobby();
                break;
            case GameProvider.STATE_STARTED:
                navigation.openGame();
                break;
            case GameProvider.STATE_FINISHED:
                navigation.openEnd();
                break;
        }
    }
}
