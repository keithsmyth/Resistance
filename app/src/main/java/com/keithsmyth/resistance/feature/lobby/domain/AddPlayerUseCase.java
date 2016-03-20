package com.keithsmyth.resistance.feature.lobby.domain;

import com.keithsmyth.resistance.data.GameInfoProvider;
import com.keithsmyth.resistance.data.UserProvider;
import com.keithsmyth.resistance.data.model.ModelActionWrapper;
import com.keithsmyth.resistance.data.model.PlayerDataModel;

import rx.Observable;
import rx.schedulers.Schedulers;

public class AddPlayerUseCase {

    private final UserProvider userProvider;
    private final GameInfoProvider gameInfoProvider;

    public AddPlayerUseCase(UserProvider userProvider, GameInfoProvider gameInfoProvider) {
        this.userProvider = userProvider;
        this.gameInfoProvider = gameInfoProvider;
    }

    public Observable<ModelActionWrapper<PlayerDataModel>> execute() {
        return gameInfoProvider.getPlayers()
            .subscribeOn(Schedulers.io());
    }
}
