package com.keithsmyth.resistance.feature.lobby.domain;

import com.keithsmyth.resistance.data.UserProvider;
import com.keithsmyth.resistance.feature.lobby.model.PlayerViewModel;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.schedulers.Schedulers;

public class AddPlayerUseCase {

    private final UserProvider userProvider;

    public AddPlayerUseCase(UserProvider userProvider) {
        this.userProvider = userProvider;
    }

    public Observable<PlayerViewModel> execute() {

        final String id = userProvider.getId();
        final String name = userProvider.getName();

        final PlayerViewModel[] viewModels = {
            new PlayerViewModel(id, name, true, true),
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
