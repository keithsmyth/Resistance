package com.keithsmyth.resistance.feature.game.presentation;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.keithsmyth.resistance.Injector;
import com.keithsmyth.resistance.R;
import com.keithsmyth.resistance.RxUtil;
import com.keithsmyth.data.model.GameInfoDataModel;
import com.keithsmyth.data.model.GamePlayDataModel;
import com.keithsmyth.resistance.feature.game.model.GameRoundViewModel;
import com.keithsmyth.resistance.presentation.PresenterDelegate;

import java.util.List;

import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func2;
import rx.schedulers.Schedulers;

public class GameFragment extends Fragment implements GameView {

    private PresenterDelegate<GameView, GamePresenter> presenterDelegate;
    private RoundAdapter roundAdapter;

    public static GameFragment create() {
        return new GameFragment();
    }

    private TextView roundText;
    private TextView captainText;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenterDelegate = new PresenterDelegate<>(getLoaderManager(), getContext(), GamePresenter.FACTORY);
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
        roundText = (TextView) view.findViewById(R.id.round_text);
        captainText = (TextView) view.findViewById(R.id.captain_text);
        final RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.rounds_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        recyclerView.setHasFixedSize(true);
        roundAdapter = new RoundAdapter(getContext());
        recyclerView.setAdapter(roundAdapter);
    }

    @Override
    public void onResume() {
        super.onResume();
        presenterDelegate.onResume(this);
    }

    @Override
    public void onPause() {
        presenterDelegate.onPause();
        super.onPause();
    }

    @Override
    public void setRounds(List<GameRoundViewModel> rounds) {
        roundAdapter.setRounds(rounds);
    }

    @Override
    public void setRound(int roundNumber) {
        roundText.setText(getString(R.string.game_round, roundNumber));
    }

    @Override
    public void setCaptain(String captain) {
        captainText.setText(getString(R.string.game_captain, captain));
    }
}
