package com.keithsmyth.resistance.data;

import android.support.annotation.IntDef;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.keithsmyth.resistance.data.firebase.FirebaseChildEventWrapper;
import com.keithsmyth.resistance.data.firebase.FirebaseEventWrapper;
import com.keithsmyth.resistance.data.firebase.FirebaseFactory;
import com.keithsmyth.resistance.data.firebase.FirebaseSingleEventWrapper;
import com.keithsmyth.resistance.data.model.GameInfoDataModel;
import com.keithsmyth.resistance.data.model.ModelActionWrapper;
import com.keithsmyth.resistance.data.model.PlayerDataModel;
import com.keithsmyth.resistance.data.prefs.SharedPreferencesWrapper;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.functions.Func2;

public class GameInfoProvider {

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({STATE_NONE, STATE_NEW, STATE_STARTING, STATE_STARTED, STATE_FINISHED})
    public @interface GameState {
    }

    public static final int STATE_NONE = 0;
    public static final int STATE_NEW = 1;
    public static final int STATE_STARTING = 2;
    public static final int STATE_STARTED = 3;
    public static final int STATE_FINISHED = 4;

    public static final int NO_GAME_ID = -1;

    private static final String KEY_CURRENT_GAME_ID = "game-current-game-id";
    private static final int GAME_ID_MIN = 10000;
    private static final int GAME_ID_MAX = 99999;

    private final SharedPreferencesWrapper prefs;
    private final FirebaseFactory firebaseFactory;
    private final UserProvider userProvider;

    public GameInfoProvider(SharedPreferencesWrapper prefs, FirebaseFactory firebaseFactory, UserProvider userProvider) {
        this.prefs = prefs;
        this.firebaseFactory = firebaseFactory;
        this.userProvider = userProvider;
    }

    private int getCurrentGameId() {
        return prefs.get().getInt(KEY_CURRENT_GAME_ID, NO_GAME_ID);
    }

    public void setCurrentGameId(int gameId) {
        prefs.get().edit().putInt(KEY_CURRENT_GAME_ID, gameId).apply();
    }

    public void clearCurrentGameId() {
        prefs.get().edit().remove(KEY_CURRENT_GAME_ID).apply();
    }

    public Observable<Integer> createGame() {
        // TODO: build this chain more elegantly
        return Observable.create(new Observable.OnSubscribe<Integer>() {
            @Override
            public void call(final Subscriber<? super Integer> subscriber) {
                getActiveGames()
                    .map(new Func1<Set<Integer>, Integer>() {
                        @Override
                        public Integer call(Set<Integer> activeGames) {
                            return getUniqueGameId(activeGames);
                        }
                    })
                    .subscribe(new Action1<Integer>() {
                        @Override
                        public void call(final Integer gameId) {
                            createGameInfoModel(gameId, subscriber);
                            addGameToActiveGames(gameId);
                        }
                    });
            }
        });
    }

    public Observable<Integer> getGameState() {
        final Firebase ref = firebaseFactory.getGameStateRef(getCurrentGameId());
        return new FirebaseSingleEventWrapper<>(ref, new FirebaseSingleEventWrapper.Mapper<Integer>() {
            @Override
            public Integer map(DataSnapshot dataSnapshot) {
                final Integer gameState = dataSnapshot.getValue(Integer.class);
                return gameState == null ? STATE_NONE : gameState;
            }
        }).getObservable();
    }

    public Observable<Integer> watchGameState() {
        final Firebase ref = firebaseFactory.getGameStateRef(getCurrentGameId());
        return new FirebaseEventWrapper<>(ref, new FirebaseEventWrapper.Mapper<Integer>() {
            @Override
            public Integer map(DataSnapshot dataSnapshot) {
                final Integer gameState = dataSnapshot.getValue(Integer.class);
                return gameState == null ? STATE_NONE : gameState;
            }
        }).getObservable();
    }

    public void setGameState(@GameState int gameState) {
        final Firebase ref = firebaseFactory.getGameStateRef(getCurrentGameId());
        ref.setValue(gameState);
    }

    public Observable<ModelActionWrapper<PlayerDataModel>> getPlayers() {
        final String userId = userProvider.getId();

        final Firebase ref = firebaseFactory.getPlayersRef(getCurrentGameId());
        final FirebaseChildEventWrapper.Mapper<DataSnapshot> mapper = new FirebaseChildEventWrapper.Mapper<DataSnapshot>() {
            @Override
            public DataSnapshot map(DataSnapshot dataSnapshot) {
                return dataSnapshot;
            }
        };
        final Observable<ModelActionWrapper<DataSnapshot>> playersObservable =
            new FirebaseChildEventWrapper<>(ref, mapper, mapper).getObservable();

        return Observable.combineLatest(getOwner(), playersObservable, new Func2<String, ModelActionWrapper<DataSnapshot>, ModelActionWrapper<PlayerDataModel>>() {
            @Override
            public ModelActionWrapper<PlayerDataModel> call(String ownerId, ModelActionWrapper<DataSnapshot> dataSnapshotModelActionWrapper) {
                final DataSnapshot dataSnapshot = dataSnapshotModelActionWrapper.dataModel;
                final String id = dataSnapshot.getKey();
                final String name = dataSnapshot.getValue().toString();
                final boolean isOwner = userId.equals(ownerId);
                final boolean isMe = userId.equals(id);
                return new ModelActionWrapper<>(new PlayerDataModel(id, name, isOwner, isMe), dataSnapshotModelActionWrapper.isAdded);
            }
        });
    }

    private Observable<String> getOwner() {
        final Firebase ref = firebaseFactory.getOwnerRef(getCurrentGameId());
        return new FirebaseSingleEventWrapper<>(ref, new FirebaseSingleEventWrapper.Mapper<String>() {
            @Override
            public String map(DataSnapshot dataSnapshot) {
                return dataSnapshot.getValue(String.class);
            }
        }).getObservable();
    }

    public Observable<Boolean> isPlayerInGame(String userId) {
        final Firebase ref = firebaseFactory.getPlayerAddedRef(getCurrentGameId(), userId);
        return new FirebaseSingleEventWrapper<>(ref, new FirebaseSingleEventWrapper.Mapper<Boolean>() {
            @Override
            public Boolean map(DataSnapshot dataSnapshot) {
                return dataSnapshot.exists();
            }
        }).getObservable();
    }

    public void addPlayerToGame(String userId, String name) {
        final Firebase ref = firebaseFactory.getPlayersRef(getCurrentGameId());
        final Map<String, Object> newPlayerIdToNameMap = new HashMap<>(1);
        newPlayerIdToNameMap.put(userId, name);
        ref.updateChildren(newPlayerIdToNameMap);
    }

    public void setAssignedCharacters(Map<String, Object> mapPlayerIdToCharacter) {
        final Firebase ref = firebaseFactory.getAssignedCharactersRef(getCurrentGameId());
        ref.setValue(mapPlayerIdToCharacter);
    }

    public Observable<GameInfoDataModel> getGameInfo() {
        final Firebase ref  = firebaseFactory.getGameInfoRef(getCurrentGameId());
        return new FirebaseSingleEventWrapper<>(ref, new FirebaseSingleEventWrapper.Mapper<GameInfoDataModel>() {
            @Override
            public GameInfoDataModel map(DataSnapshot dataSnapshot) {
                return dataSnapshot.getValue(GameInfoDataModel.class);
            }
        }).getObservable();
    }

    private Observable<Set<Integer>> getActiveGames() {
        final Firebase ref = firebaseFactory.getActiveGamesRef();
        return new FirebaseSingleEventWrapper<>(ref, new FirebaseSingleEventWrapper.Mapper<Set<Integer>>() {
            @Override
            public Set<Integer> map(DataSnapshot dataSnapshot) {
                final Set<Integer> activeGameIdSet = new HashSet<>();
                for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                    activeGameIdSet.add(Integer.valueOf(childSnapshot.getKey()));
                }
                return activeGameIdSet;
            }
        }).getObservable();
    }

    private int getUniqueGameId(Set<Integer> activeGames) {
        final Random rand = new Random();
        int gameId = generateGameId(rand);
        while (activeGames.contains(gameId)) {
            gameId = generateGameId(rand);
        }
        return gameId;
    }

    private int generateGameId(Random rand) {
        return rand.nextInt((GAME_ID_MAX - GAME_ID_MIN) + 1) + GAME_ID_MIN;
    }

    private void createGameInfoModel(final Integer gameId, final Subscriber<? super Integer> subscriber) {
        final Firebase ref = firebaseFactory.getGameInfoRef(gameId);
        final GameInfoDataModel gameInfoDataModel = new GameInfoDataModel();
        gameInfoDataModel.setOwnerId(userProvider.getId());
        gameInfoDataModel.setStatus(STATE_NEW);
        ref.setValue(gameInfoDataModel, new Firebase.CompletionListener() {
            @Override
            public void onComplete(FirebaseError firebaseError, Firebase firebase) {
                if (firebaseError == null) {
                    subscriber.onNext(gameId);
                    subscriber.onCompleted();
                } else {
                    subscriber.onError(firebaseError.toException());
                }
            }
        });
    }

    private void addGameToActiveGames(Integer gameId) {
        final Firebase ref = firebaseFactory.getActiveGamesRef();
        final Map<String, Object> newActiveGameIdMap = new HashMap<>(1);
        newActiveGameIdMap.put(gameId.toString(), "");
        ref.updateChildren(newActiveGameIdMap);
    }
}
