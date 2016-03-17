package com.keithsmyth.resistance.welcome.domain;

import com.keithsmyth.resistance.data.PlayerProvider;
import com.keithsmyth.resistance.welcome.exception.GameNotExistException;

import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscriber;
import rx.schedulers.Schedulers;

public class JoinGameUseCase {

    private final PlayerProvider playerProvider;

    public JoinGameUseCase(PlayerProvider playerProvider) {
        this.playerProvider = playerProvider;
    }

    public Observable<Integer> execute(int gameId, String playerName) {
        playerProvider.setPlayer(playerName);
        final Observable<Integer> observable;
        if (gameId % 2 == 0) {
            observable = Observable.from(new Integer[]{gameId});
        } else {
            observable = Observable.create(new Observable.OnSubscribe<Integer>() {
                @Override
                public void call(Subscriber<? super Integer> subscriber) {
                    subscriber.onError(new GameNotExistException());
                }
            });
        }
        return observable
            .delay(2, TimeUnit.SECONDS)
            .subscribeOn(Schedulers.computation());
    }
}
