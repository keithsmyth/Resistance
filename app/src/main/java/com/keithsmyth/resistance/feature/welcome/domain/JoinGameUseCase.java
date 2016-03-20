package com.keithsmyth.resistance.feature.welcome.domain;

import com.keithsmyth.resistance.RxUtil;
import com.keithsmyth.resistance.data.GameInfoProvider;
import com.keithsmyth.resistance.data.UserProvider;
import com.keithsmyth.resistance.feature.welcome.exception.GameNotExistException;
import com.keithsmyth.resistance.navigation.GenericDisplayThrowable;
import com.keithsmyth.resistance.navigation.Navigation;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

public class JoinGameUseCase {

    private final Navigation navigation;
    private final UserProvider userProvider;
    private final GameInfoProvider gameInfoProvider;

    private Subscription gameStateSubscription;

    public JoinGameUseCase(Navigation navigation, UserProvider userProvider, GameInfoProvider gameInfoProvider) {
        this.navigation = navigation;
        this.userProvider = userProvider;
        this.gameInfoProvider = gameInfoProvider;
    }

    public void execute(int gameId, String playerName) {
        userProvider.setName(playerName);
        userProvider.setGameId(gameId);

        // check game state, navigate to correct screen
        RxUtil.unsubscribe(gameStateSubscription);
        gameStateSubscription = gameInfoProvider.getGameState()
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

    private void onGameStateReturned(@GameInfoProvider.GameState int gameState) {
        switch (gameState) {
            case GameInfoProvider.STATE_NONE:
                navigation.showError(new GameNotExistException());
                break;
            case GameInfoProvider.STATE_NEW:
            case GameInfoProvider.STATE_STARTING:
                navigation.openLobby();
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
