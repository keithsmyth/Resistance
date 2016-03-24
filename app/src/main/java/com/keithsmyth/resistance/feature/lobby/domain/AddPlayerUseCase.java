package com.keithsmyth.resistance.feature.lobby.domain;

import com.keithsmyth.resistance.RxUtil;
import com.keithsmyth.resistance.data.GameInfoProvider;
import com.keithsmyth.resistance.data.UserProvider;
import com.keithsmyth.resistance.data.model.ModelActionWrapper;
import com.keithsmyth.resistance.data.model.PlayerDataModel;
import com.keithsmyth.resistance.navigation.GenericDisplayThrowable;
import com.keithsmyth.resistance.navigation.Navigation;

import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class AddPlayerUseCase {

    private final Navigation navigation;
    private final UserProvider userProvider;
    private final GameInfoProvider gameInfoProvider;

    private Subscription inGameSubscription;

    public AddPlayerUseCase(Navigation navigation, UserProvider userProvider, GameInfoProvider gameInfoProvider) {
        this.navigation = navigation;
        this.userProvider = userProvider;
        this.gameInfoProvider = gameInfoProvider;
    }

    public Observable<ModelActionWrapper<PlayerDataModel>> execute() {
        // add current user to list if required
        ensurePlayerAdded();

        // watch players feed
        return gameInfoProvider.watchPlayers();
    }

    public void destroy() {
        RxUtil.unsubscribe(inGameSubscription);
    }

    private void ensurePlayerAdded() {
        RxUtil.unsubscribe(inGameSubscription);
        inGameSubscription = gameInfoProvider.isPlayerInGame(userProvider.getId())
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Action1<Boolean>() {
                @Override
                public void call(Boolean isAdded) {
                    RxUtil.unsubscribe(inGameSubscription);
                    if (!isAdded) {
                        addPlayerToGame();
                    }
                }
            }, new Action1<Throwable>() {
                @Override
                public void call(Throwable throwable) {
                    RxUtil.unsubscribe(inGameSubscription);
                    navigation.showError(new GenericDisplayThrowable(throwable));
                }
            });
    }

    private void addPlayerToGame() {
        gameInfoProvider.addPlayerToGame(userProvider.getId(), userProvider.getName());
    }
}
