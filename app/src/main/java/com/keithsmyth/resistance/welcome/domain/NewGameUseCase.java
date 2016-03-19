package com.keithsmyth.resistance.welcome.domain;

import com.keithsmyth.resistance.RxUtil;
import com.keithsmyth.resistance.data.GameProvider;
import com.keithsmyth.resistance.data.UserProvider;
import com.keithsmyth.resistance.navigation.GenericDisplayThrowable;
import com.keithsmyth.resistance.navigation.Navigation;

import java.util.List;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class NewGameUseCase {

    private final Navigation navigation;
    private final UserProvider userProvider;
    private final GameProvider gameProvider;

    private Subscription activeGamesSubscription;

    public NewGameUseCase(Navigation navigation, UserProvider userProvider, GameProvider gameProvider) {
        this.navigation = navigation;
        this.userProvider = userProvider;
        this.gameProvider = gameProvider;
    }

    public void execute(String playerName) {
        userProvider.setPlayer(playerName);

        RxUtil.unsubscribe(activeGamesSubscription);
        activeGamesSubscription = gameProvider.getActiveGames()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Action1<List<Integer>>() {
                @Override
                public void call(List<Integer> integers) {
                    RxUtil.unsubscribe(activeGamesSubscription);
                    onActiveGamesReceived(integers);
                }
            }, new Action1<Throwable>() {
                @Override
                public void call(Throwable throwable) {
                    navigation.showError(new GenericDisplayThrowable(throwable));
                }
            });
    }

    public void destroy() {
        RxUtil.unsubscribe(activeGamesSubscription);
    }

    private void onActiveGamesReceived(List<Integer> activeGames) {
        // TODO: generate random and check against active games
        gameProvider.setActiveGameId(12345);
        navigation.openLobby();
    }
}
