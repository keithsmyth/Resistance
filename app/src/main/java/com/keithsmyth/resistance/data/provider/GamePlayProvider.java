package com.keithsmyth.resistance.data.provider;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.keithsmyth.resistance.data.firebase.FirebaseEventWrapper;
import com.keithsmyth.resistance.data.firebase.FirebaseFactory;
import com.keithsmyth.resistance.data.model.GamePlayDataModel;
import com.keithsmyth.resistance.data.model.GameRoundDataModel;

import java.util.HashMap;

import rx.Observable;

public class GamePlayProvider {

    public static final int STATUS_BUILD_TEAM = 0;
    public static final int STATUS_VOTE_TEAM = 1;
    public static final int STATUS_QUEST = 2;
    public static final int STATUS_COMPLETE = 3;

    private final GameInfoProvider gameInfoProvider;
    private final FirebaseFactory firebaseFactory;
    private final UserProvider userProvider;

    public GamePlayProvider(GameInfoProvider gameInfoProvider, FirebaseFactory firebaseFactory, UserProvider userProvider) {
        this.gameInfoProvider = gameInfoProvider;
        this.firebaseFactory = firebaseFactory;
        this.userProvider = userProvider;
    }

    public void createRound(String captainUserId) {
        final GamePlayDataModel gamePlayDataModel = new GamePlayDataModel();
        gamePlayDataModel.setCurrentRound(1);
        gamePlayDataModel.setVoteFails(0);
        gamePlayDataModel.setQuestFails(0);

        final GameRoundDataModel gameRoundDataModel = new GameRoundDataModel();
        gameRoundDataModel.setCaptain(captainUserId);
        gameRoundDataModel.setStatus(STATUS_BUILD_TEAM);
        final HashMap<String, GameRoundDataModel> mapRoundNumberToRound = new HashMap<>(1);
        mapRoundNumberToRound.put("01", gameRoundDataModel);
        gamePlayDataModel.setMapRoundNumberToRound(mapRoundNumberToRound);

        final Firebase ref = firebaseFactory.getGamePlayRef(gameInfoProvider.getCurrentGameId());
        ref.setValue(gamePlayDataModel);
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
}
