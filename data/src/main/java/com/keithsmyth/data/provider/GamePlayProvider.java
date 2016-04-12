package com.keithsmyth.data.provider;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.keithsmyth.data.firebase.FirebaseCompletionWrapper;
import com.keithsmyth.data.firebase.FirebaseEventWrapper;
import com.keithsmyth.data.firebase.FirebaseFactory;
import com.keithsmyth.data.model.GamePlayDataModel;
import com.keithsmyth.data.model.GameRoundDataModel;

import java.util.HashMap;
import java.util.Locale;

import rx.Observable;
import rx.Single;

public class GamePlayProvider {

    public static final int STATUS_NONE = 0;
    public static final int STATUS_BUILD_TEAM = 1;
    public static final int STATUS_VOTE_TEAM = 2;
    public static final int STATUS_QUEST = 3;
    public static final int STATUS_COMPLETE = 4;

    private final GameInfoProvider gameInfoProvider;
    private final FirebaseFactory firebaseFactory;
    private final UserProvider userProvider;

    public GamePlayProvider(GameInfoProvider gameInfoProvider, FirebaseFactory firebaseFactory, UserProvider userProvider) {
        this.gameInfoProvider = gameInfoProvider;
        this.firebaseFactory = firebaseFactory;
        this.userProvider = userProvider;
    }

    public Single<?> createRound(String captainUserId, int roundNumber) {
        final GamePlayDataModel gamePlayDataModel = new GamePlayDataModel();
        gamePlayDataModel.setCurrentRound(1);
        gamePlayDataModel.setVoteFails(0);
        gamePlayDataModel.setQuestFails(0);

        final GameRoundDataModel gameRoundDataModel = new GameRoundDataModel();
        gameRoundDataModel.setCaptain(captainUserId);
        gameRoundDataModel.setStatus(STATUS_BUILD_TEAM);
        final HashMap<String, GameRoundDataModel> mapRoundNumberToRound = new HashMap<>(1);
        mapRoundNumberToRound.put(padRoundNumber(roundNumber), gameRoundDataModel);
        gamePlayDataModel.setMapRoundNumberToRound(mapRoundNumberToRound);

        final Firebase ref = firebaseFactory.getGamePlayRef(gameInfoProvider.getCurrentGameId());
        return new FirebaseCompletionWrapper<>(ref, null).setValue(gamePlayDataModel);
    }

    public Observable<GamePlayDataModel> watchGamePlay() {
        final Firebase ref = firebaseFactory.getGamePlayRef(gameInfoProvider.getCurrentGameId());
        return new FirebaseEventWrapper<>(ref, new FirebaseEventWrapper.Mapper<GamePlayDataModel>() {
            @Override
            public GamePlayDataModel map(DataSnapshot dataSnapshot) {
                return dataSnapshot.getValue(GamePlayDataModel.class);
            }
        }).getObservable();
    }

    public String padRoundNumber(int roundNumber) {
        return String.format(Locale.getDefault(), "%02d", roundNumber);
    }
}
