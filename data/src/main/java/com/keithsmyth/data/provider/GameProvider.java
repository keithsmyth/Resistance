package com.keithsmyth.data.provider;

import com.firebase.client.Firebase;
import com.keithsmyth.data.firebase.FirebaseCompletionWrapper;
import com.keithsmyth.data.firebase.FirebaseFactory;

import java.util.HashMap;
import java.util.Map;

import rx.Single;
import rx.functions.Func1;

public class GameProvider {

    private final FirebaseFactory firebaseFactory;
    private final GameInfoProvider gameInfoProvider;

    public GameProvider(FirebaseFactory firebaseFactory, GameInfoProvider gameInfoProvider) {
        this.firebaseFactory = firebaseFactory;
        this.gameInfoProvider = gameInfoProvider;
    }

    public Single<?> removeGame() {
        return removeGameObject()
            .flatMap(new Func1<Object, Single<?>>() {
                @Override
                public Single<?> call(Object o) {
                    return removeActiveGame();
                }
            });
    }

    private Single<?> removeGameObject() {
        final Firebase ref = firebaseFactory.getGameRef(gameInfoProvider.getCurrentGameId());
        return new FirebaseCompletionWrapper<>(ref, null).setValue(null);
    }

    private Single<?> removeActiveGame() {
        final Firebase ref = firebaseFactory.getActiveGamesRef();
        final Map<String, Object> children = new HashMap<>(1);
        children.put(String.valueOf(gameInfoProvider.getCurrentGameId()), null);
        return new FirebaseCompletionWrapper<>(ref, null).updateChildren(children);
    }
}
