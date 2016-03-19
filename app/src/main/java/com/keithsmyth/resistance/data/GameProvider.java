package com.keithsmyth.resistance.data;

import android.support.annotation.IntDef;

import com.keithsmyth.resistance.data.prefs.SharedPreferencesWrapper;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.AbstractCollection;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscriber;
import rx.schedulers.Schedulers;

public class GameProvider {

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({STATE_NONE, STATE_JOINING, STATE_STARTING, STATE_STARTED, STATE_FINISHED})
    public @interface GameState {
    }

    public static final int STATE_NONE = 0;
    public static final int STATE_JOINING = 1;
    public static final int STATE_STARTING = 2;
    public static final int STATE_STARTED = 3;
    public static final int STATE_FINISHED = 4;

    public static final int NO_GAME_ID = -1;

    private static final String KEY_ACTIVE_GAME_ID = "game-active-game-id";

    private final SharedPreferencesWrapper prefs;
    private final UserProvider userProvider;

    @GameState private int gameState;

    public GameProvider(SharedPreferencesWrapper prefs, UserProvider userProvider) {
        this.prefs = prefs;
        this.userProvider = userProvider;
    }

    public int getActiveGameId() {
        return prefs.get().getInt(KEY_ACTIVE_GAME_ID, NO_GAME_ID);
    }

    public void setActiveGameId(int gameId) {
        prefs.get().edit().putInt(KEY_ACTIVE_GAME_ID, gameId).apply();
    }

    public void clearActiveGameId() {
        prefs.get().edit().remove(KEY_ACTIVE_GAME_ID).apply();
    }

    public Observable<Integer> getGameState() {
        return Observable.from(new Integer[]{gameState})
            .subscribeOn(Schedulers.io())
            .delay(1, TimeUnit.SECONDS);
    }

    public void setGameState(@GameState int gameState) {
        this.gameState = gameState;
    }

    public Observable<List<Integer>> getActiveGames() {
        return Observable
            .create(new Observable.OnSubscribe<List<Integer>>() {
                @Override
                public void call(Subscriber<? super List<Integer>> subscriber) {
                    subscriber.onNext(Collections.singletonList(12345));
                }
            })
            .subscribeOn(Schedulers.io())
            .delay(1, TimeUnit.SECONDS);
    }
}
