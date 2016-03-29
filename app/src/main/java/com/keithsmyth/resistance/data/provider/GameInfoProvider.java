package com.keithsmyth.resistance.data.provider;

import android.support.annotation.IntDef;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.keithsmyth.resistance.data.firebase.FirebaseChildEventWrapper;
import com.keithsmyth.resistance.data.firebase.FirebaseCompletionWrapper;
import com.keithsmyth.resistance.data.firebase.FirebaseEventWrapper;
import com.keithsmyth.resistance.data.firebase.FirebaseFactory;
import com.keithsmyth.resistance.data.firebase.FirebaseSingleEventWrapper;
import com.keithsmyth.resistance.data.model.GameInfoDataModel;
import com.keithsmyth.resistance.data.model.ModelActionWrapper;
import com.keithsmyth.resistance.data.model.PlayerDataModel;
import com.keithsmyth.resistance.data.prefs.SharedPreferencesWrapper;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import rx.Observable;
import rx.Single;
import rx.functions.Func1;
import rx.functions.Func2;

// TODO: Ensure a single GameInfoDataModel is cached when current game id is set.
// TODO: Ensure it can be turned off - e.g. setActive(boolean)
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

    private static final int NO_GAME_ID = -1;

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

    public int getCurrentGameId() {
        return prefs.get().getInt(KEY_CURRENT_GAME_ID, NO_GAME_ID);
    }

    public void setCurrentGameId(int gameId) {
        prefs.get().edit().putInt(KEY_CURRENT_GAME_ID, gameId).apply();
    }

    public void clearCurrentGameId() {
        prefs.get().edit().remove(KEY_CURRENT_GAME_ID).apply();
    }

    public Single<Integer> createGame() {
        return getActiveGames()
            .map(new Func1<Set<Integer>, Integer>() {
                @Override
                public Integer call(Set<Integer> activeGames) {
                    return generateUniqueGameId(activeGames);
                }
            })
            .flatMap(new Func1<Integer, Single<? extends Integer>>() {
                @Override
                public Single<? extends Integer> call(Integer gameId) {
                    return addGameToActiveGames(gameId);
                }
            })
            .flatMap(new Func1<Integer, Single<? extends Integer>>() {
                @Override
                public Single<? extends Integer> call(Integer gameId) {
                    return createGameInfoModel(gameId);
                }
            });
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

    public Single<?> setGameState(@GameState int gameState) {
        final Firebase ref = firebaseFactory.getGameStateRef(getCurrentGameId());
        return new FirebaseCompletionWrapper<>(ref, null).setValue(gameState);
    }

    public Observable<ModelActionWrapper<PlayerDataModel>> watchPlayers() {
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

        return Observable.combineLatest(getOwner().toObservable(), playersObservable, new Func2<String, ModelActionWrapper<DataSnapshot>, ModelActionWrapper<PlayerDataModel>>() {
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

    public Single<List<String>> getPlayerIds() {
        final Firebase ref = firebaseFactory.getPlayersRef(getCurrentGameId());
        return new FirebaseSingleEventWrapper<>(ref, new FirebaseSingleEventWrapper.Mapper<List<String>>() {
            @Override
            public List<String> map(DataSnapshot dataSnapshot) {
                final List<String> playerIds = new ArrayList<>();
                for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                    playerIds.add(childSnapshot.getKey());
                }
                return playerIds;
            }
        }).getSingle();
    }

    private Single<String> getOwner() {
        final Firebase ref = firebaseFactory.getOwnerRef(getCurrentGameId());
        return new FirebaseSingleEventWrapper<>(ref, new FirebaseSingleEventWrapper.Mapper<String>() {
            @Override
            public String map(DataSnapshot dataSnapshot) {
                return dataSnapshot.getValue(String.class);
            }
        }).getSingle();
    }

    public Single<Boolean> isPlayerInGame(String userId) {
        final Firebase ref = firebaseFactory.getPlayerAddedRef(getCurrentGameId(), userId);
        return new FirebaseSingleEventWrapper<>(ref, new FirebaseSingleEventWrapper.Mapper<Boolean>() {
            @Override
            public Boolean map(DataSnapshot dataSnapshot) {
                return dataSnapshot.exists();
            }
        }).getSingle();
    }

    public Single<?> addPlayerToGame(String userId, String name) {
        final Firebase ref = firebaseFactory.getPlayersRef(getCurrentGameId());
        final Map<String, Object> newPlayerIdToNameMap = new HashMap<>(1);
        newPlayerIdToNameMap.put(userId, name);
        return new FirebaseCompletionWrapper<>(ref, null).updateChildren(newPlayerIdToNameMap);
    }

    public Single<?> setAssignedCharacters(Map<String, Object> mapPlayerIdToCharacter) {
        final Firebase ref = firebaseFactory.getAssignedCharactersRef(getCurrentGameId());
        return new FirebaseCompletionWrapper<>(ref, null).setValue(mapPlayerIdToCharacter);
    }

    public Single<GameInfoDataModel> getGameInfo() {
        final Firebase ref  = firebaseFactory.getGameInfoRef(getCurrentGameId());
        return new FirebaseSingleEventWrapper<>(ref, new FirebaseSingleEventWrapper.Mapper<GameInfoDataModel>() {
            @Override
            public GameInfoDataModel map(DataSnapshot dataSnapshot) {
                return dataSnapshot.getValue(GameInfoDataModel.class);
            }
        }).getSingle();
    }

    private Single<Set<Integer>> getActiveGames() {
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
        }).getSingle();
    }

    private int generateUniqueGameId(Set<Integer> activeGames) {
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

    private Single<Integer> addGameToActiveGames(Integer gameId) {
        final Firebase ref = firebaseFactory.getActiveGamesRef();
        final Map<String, Object> newActiveGameIdMap = new HashMap<>(1);
        newActiveGameIdMap.put(gameId.toString(), "");
        return new FirebaseCompletionWrapper<>(ref, gameId).updateChildren(newActiveGameIdMap);
    }

    private Single<Integer> createGameInfoModel(Integer gameId) {
        final Firebase ref = firebaseFactory.getGameInfoRef(gameId);
        final GameInfoDataModel gameInfoDataModel = new GameInfoDataModel();
        gameInfoDataModel.setOwnerId(userProvider.getId());
        gameInfoDataModel.setStatus(STATE_NEW);
        gameInfoDataModel.setVersion(userProvider.getClientVersion());
        return new FirebaseCompletionWrapper<>(ref, gameId).setValue(gameInfoDataModel);
    }
}
