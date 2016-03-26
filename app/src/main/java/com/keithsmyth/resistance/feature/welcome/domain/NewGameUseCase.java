package com.keithsmyth.resistance.feature.welcome.domain;

import com.keithsmyth.resistance.RxUtil;
import com.keithsmyth.resistance.data.provider.GameInfoProvider;
import com.keithsmyth.resistance.data.provider.UserProvider;
import com.keithsmyth.resistance.navigation.GenericDisplayThrowable;
import com.keithsmyth.resistance.navigation.Navigation;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class NewGameUseCase {

    private final Navigation navigation;
    private final UserProvider userProvider;
    private final GameInfoProvider gameInfoProvider;

    private Subscription createGameSubscription;

    public NewGameUseCase(Navigation navigation, UserProvider userProvider, GameInfoProvider gameInfoProvider) {
        this.navigation = navigation;
        this.userProvider = userProvider;
        this.gameInfoProvider = gameInfoProvider;
    }

    public void execute(String playerName) {
        userProvider.setName(playerName);

        RxUtil.unsubscribe(createGameSubscription);
        createGameSubscription = gameInfoProvider.createGame()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Action1<Integer>() {
                @Override
                public void call(Integer gameId) {
                    RxUtil.unsubscribe(createGameSubscription);
                    onGameCreated(gameId);
                }
            }, new Action1<Throwable>() {
                @Override
                public void call(Throwable throwable) {
                    RxUtil.unsubscribe(createGameSubscription);
                    navigation.showError(new GenericDisplayThrowable(throwable));
                }
            });
    }

    public void destroy() {
        RxUtil.unsubscribe(createGameSubscription);
    }

    private void onGameCreated(int gameId) {
        gameInfoProvider.setCurrentGameId(gameId);
        userProvider.setGameId(gameId);
        navigation.openLobby();
    }
}
