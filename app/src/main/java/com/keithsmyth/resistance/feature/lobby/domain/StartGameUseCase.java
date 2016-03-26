package com.keithsmyth.resistance.feature.lobby.domain;

import com.keithsmyth.resistance.RxUtil;
import com.keithsmyth.resistance.data.provider.GameInfoProvider;
import com.keithsmyth.resistance.data.provider.GamePlayProvider;
import com.keithsmyth.resistance.navigation.GenericDisplayThrowable;
import com.keithsmyth.resistance.navigation.Navigation;

import java.util.List;
import java.util.Random;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class StartGameUseCase {

    private final Navigation navigation;
    private final GameInfoProvider gameInfoProvider;
    private final GamePlayProvider gamePlayProvider;

    private Subscription playersSubscription;

    public StartGameUseCase(Navigation navigation, GameInfoProvider gameInfoProvider, GamePlayProvider gamePlayProvider) {
        this.navigation = navigation;
        this.gameInfoProvider = gameInfoProvider;
        this.gamePlayProvider = gamePlayProvider;
    }

    public void execute() {
        // TODO: get players
        RxUtil.unsubscribe(playersSubscription);
        playersSubscription = gameInfoProvider.getPlayerIds()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Action1<List<String>>() {
                @Override
                public void call(List<String> strings) {
                    RxUtil.unsubscribe(playersSubscription);
                    onPlayersReturned(strings);
                }
            }, new Action1<Throwable>() {
                @Override
                public void call(Throwable throwable) {
                    RxUtil.unsubscribe(playersSubscription);
                    navigation.showError(new GenericDisplayThrowable(throwable));
                }
            });
    }

    private void onPlayersReturned(List<String> playerIds) {
        // pick who's first
        final Random rand = new Random();
        final int index = rand.nextInt(playerIds.size());

        // create and save round 1
        gamePlayProvider.createRound(playerIds.get(index));

        // update the game state
        gameInfoProvider.setGameState(GameInfoProvider.STATE_STARTED);
    }


}
