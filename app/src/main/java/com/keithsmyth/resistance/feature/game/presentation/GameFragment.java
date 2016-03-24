package com.keithsmyth.resistance.feature.game.presentation;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.keithsmyth.resistance.Injector;
import com.keithsmyth.resistance.R;
import com.keithsmyth.resistance.RxUtil;
import com.keithsmyth.resistance.data.model.GameInfoDataModel;
import com.keithsmyth.resistance.data.model.GamePlayDataModel;

import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func2;
import rx.schedulers.Schedulers;

public class GameFragment extends Fragment {

    public static GameFragment create() {
        return new GameFragment();
    }

    private TextView captainText;
    private Subscription subscription;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().setTitle(R.string.game_title);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_game, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        captainText = (TextView) view.findViewById(R.id.captain_text);
    }

    @Override
    public void onResume() {
        super.onResume();
        // TODO: kill this hack / move it into Presenters/UseCases etc
        RxUtil.unsubscribe(subscription);
        final Observable<GameInfoDataModel> gameInfoObservable = Injector.gameInfoProvider().getGameInfo();
        final Observable<GamePlayDataModel> gamePlayObservable = Injector.gamePlayProvider().watchGamePlay();
        subscription = gameInfoObservable
            .zipWith(gamePlayObservable, new Func2<GameInfoDataModel, GamePlayDataModel, Void>() {
                @Override
                public Void call(GameInfoDataModel gameInfoDataModel, GamePlayDataModel gamePlayDataModel) {
                    onInfoAndPlayModelsReturned(gameInfoDataModel, gamePlayDataModel);
                    return null;
                }
            })
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe();
    }

    @Override
    public void onPause() {
        RxUtil.unsubscribe(subscription);
        super.onPause();
    }

    private void onInfoAndPlayModelsReturned(GameInfoDataModel gameInfoDataModel, GamePlayDataModel gamePlayDataModel) {
        final String captainUserId = gamePlayDataModel.getMapRoundNumberToRound().get("01").getCaptain();
        captainText.setText(gameInfoDataModel.getMapPlayerIdToName().get(captainUserId) + " goes first");
    }
}
