package com.keithsmyth.resistance.feature.lobby.domain;

import com.keithsmyth.resistance.RxUtil;
import com.keithsmyth.data.model.ModelActionWrapper;
import com.keithsmyth.data.model.PlayerDataModel;
import com.keithsmyth.data.provider.GameInfoProvider;
import com.keithsmyth.data.provider.UserProvider;
import com.keithsmyth.resistance.navigation.GenericDisplayThrowable;
import com.keithsmyth.resistance.navigation.Navigation;

import rx.Observable;
import rx.Single;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
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
            .flatMap(new Func1<Boolean, Single<?>>() {
                @Override
                public Single<?> call(Boolean isAdded) {
                    if (!isAdded) {
                        return gameInfoProvider.addPlayerToGame(userProvider.getId(), userProvider.getName());
                    }
                    return Single.just(null);
                }
            })
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Action1<Object>() {
                @Override
                public void call(Object o) {
                    RxUtil.unsubscribe(inGameSubscription);
                }
            }, new Action1<Throwable>() {
                @Override
                public void call(Throwable throwable) {
                    RxUtil.unsubscribe(inGameSubscription);
                    navigation.showError(new GenericDisplayThrowable(throwable));
                }
            });
    }
}
