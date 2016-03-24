package com.keithsmyth.resistance.feature.welcome.domain;

import com.keithsmyth.resistance.RxUtil;
import com.keithsmyth.resistance.data.GameInfoProvider;
import com.keithsmyth.resistance.data.UserProvider;
import com.keithsmyth.resistance.data.model.GameInfoDataModel;
import com.keithsmyth.resistance.feature.welcome.exception.GameNotExistException;
import com.keithsmyth.resistance.feature.welcome.exception.NotYourGameException;
import com.keithsmyth.resistance.navigation.GenericDisplayThrowable;
import com.keithsmyth.resistance.navigation.Navigation;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

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
        gameInfoProvider.setCurrentGameId(gameId);

        // check game state, navigate to correct screen
        RxUtil.unsubscribe(gameStateSubscription);
        gameStateSubscription = gameInfoProvider.getGameInfo()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Action1<GameInfoDataModel>() {
                @Override
                public void call(GameInfoDataModel gameInfoDataModel) {
                    RxUtil.unsubscribe(gameStateSubscription);
                    onGameStateReturned(gameInfoDataModel);
                }
            }, new Action1<Throwable>() {
                @Override
                public void call(Throwable throwable) {
                    RxUtil.unsubscribe(gameStateSubscription);
                    navigation.showError(new GenericDisplayThrowable(throwable));
                }
            });
    }

    public void destroy() {
        RxUtil.unsubscribe(gameStateSubscription);
    }

    private void onGameStateReturned(GameInfoDataModel gameInfoDataModel) {
        switch (gameInfoDataModel.getStatus()) {
            case GameInfoProvider.STATE_NONE:
                navigation.showError(new GameNotExistException());
                break;
            case GameInfoProvider.STATE_NEW:
                navigation.openLobby();
                break;
            case GameInfoProvider.STATE_STARTING:
                if (isPlayerInGame(gameInfoDataModel)) {
                    navigation.openLobby();
                } else {
                    navigation.showError(new NotYourGameException());
                }
                break;
            case GameInfoProvider.STATE_STARTED:
                if (isPlayerInGame(gameInfoDataModel)) {
                    navigation.openGame();
                } else {
                    navigation.showError(new NotYourGameException());
                }
                break;
            case GameInfoProvider.STATE_FINISHED:
                if (isPlayerInGame(gameInfoDataModel)) {
                    navigation.openEnd();
                } else {
                    navigation.showError(new NotYourGameException());
                }
                break;
        }
    }

    private boolean isPlayerInGame(GameInfoDataModel gameInfoDataModel) {
        return gameInfoDataModel.getMapPlayerIdToName().containsKey(userProvider.getId());
    }
}
