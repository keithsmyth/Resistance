package com.keithsmyth.resistance.lobby.domain;

import com.keithsmyth.resistance.data.PlayerProvider;
import com.keithsmyth.resistance.data.model.PlayerDataModel;
import com.keithsmyth.resistance.lobby.model.PlayerViewModel;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.schedulers.Schedulers;

public class AddPlayerUseCase {

    private final PlayerProvider playerProvider;

    public AddPlayerUseCase(PlayerProvider playerProvider) {
        this.playerProvider = playerProvider;
    }

    public Observable<PlayerViewModel> execute() {

        final PlayerDataModel playerDataModel = playerProvider.getPlayer();

        final PlayerViewModel[] viewModels = {
            new PlayerViewModel(playerDataModel.uuid, playerDataModel.name, true, true),
            new PlayerViewModel(UUID.randomUUID().toString(), "Player 2", false, false),
            new PlayerViewModel(UUID.randomUUID().toString(), "Player 3", false, false),
            new PlayerViewModel(UUID.randomUUID().toString(), "Player 4", false, false),
            new PlayerViewModel(UUID.randomUUID().toString(), "Player 5", false, false)
        };
        return Observable.from(viewModels)
            .delay(2, TimeUnit.SECONDS)
            .subscribeOn(Schedulers.computation());
    }
}
