package com.keithsmyth.resistance.feature.welcome.domain;

import com.keithsmyth.resistance.RxUtil;
import com.keithsmyth.data.provider.GameInfoProvider;
import com.keithsmyth.data.provider.UserProvider;
import com.keithsmyth.data.model.GameInfoDataModel;
import com.keithsmyth.resistance.feature.welcome.exception.GameNotExistThrowable;
import com.keithsmyth.resistance.feature.welcome.exception.InvalidGameVersionThrowable;
import com.keithsmyth.resistance.feature.welcome.exception.NameExistsThrowable;
import com.keithsmyth.resistance.feature.welcome.exception.NotYourGameThrowable;
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
                    onGameInfoReturned(gameInfoDataModel);
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

    private void onGameInfoReturned(GameInfoDataModel gameInfoDataModel) {
        final String clientVersion = userProvider.getClientVersion();
        final String requiredVersion = gameInfoDataModel.getVersion();
        if (!clientVersion.equals(requiredVersion)) {
            navigation.showError(new InvalidGameVersionThrowable(requiredVersion, clientVersion));
            return;
        }

        switch (gameInfoDataModel.getStatus()) {
            case GameInfoProvider.STATE_NONE:
                navigation.showError(new GameNotExistThrowable());
                break;
            case GameInfoProvider.STATE_NEW:
                if (nameExistsForOtherPlayer(gameInfoDataModel)) {
                    navigation.showError(new NameExistsThrowable());
                } else {
                    navigation.openLobby();
                }
                break;
            case GameInfoProvider.STATE_STARTING:
                if (nameExistsForOtherPlayer(gameInfoDataModel)) {
                    navigation.showError(new NameExistsThrowable());
                } else if (!isPlayerInGame(gameInfoDataModel)) {
                    navigation.showError(new NotYourGameThrowable());
                } else {
                    navigation.openLobby();
                }
                break;
            case GameInfoProvider.STATE_STARTED:
                if (isPlayerInGame(gameInfoDataModel)) {
                    navigation.openGame();
                } else {
                    navigation.showError(new NotYourGameThrowable());
                }
                break;
            case GameInfoProvider.STATE_FINISHED:
                if (isPlayerInGame(gameInfoDataModel)) {
                    navigation.openEnd();
                } else {
                    navigation.showError(new NotYourGameThrowable());
                }
                break;
        }
    }

    private boolean isPlayerInGame(GameInfoDataModel gameInfoDataModel) {
        return gameInfoDataModel.getMapPlayerIdToName().containsKey(userProvider.getId());
    }

    private boolean nameExistsForOtherPlayer(GameInfoDataModel gameInfoDataModel) {
        if (!isPlayerInGame(gameInfoDataModel)) {
            final String name = userProvider.getName();
            for (String otherName : gameInfoDataModel.getMapPlayerIdToName().values()) {
                if (otherName.equalsIgnoreCase(name)) {
                    return true;
                }
            }
        }
        return false;
    }
}
