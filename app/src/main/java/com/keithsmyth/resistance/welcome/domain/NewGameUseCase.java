package com.keithsmyth.resistance.welcome.domain;

import com.keithsmyth.resistance.data.PlayerProvider;

import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.schedulers.Schedulers;

public class NewGameUseCase {

    private final PlayerProvider playerProvider;

    public NewGameUseCase(PlayerProvider playerProvider) {
        this.playerProvider = playerProvider;
    }

    public Observable<Integer> execute(String playerName) {
        playerProvider.setPlayer(playerName);
        return Observable.from(new Integer[] { 12345 })
            .delay(2, TimeUnit.SECONDS)
            .subscribeOn(Schedulers.computation());
    }
}
