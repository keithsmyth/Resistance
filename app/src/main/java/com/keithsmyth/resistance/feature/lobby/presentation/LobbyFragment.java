package com.keithsmyth.resistance.feature.lobby.presentation;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.keithsmyth.resistance.R;
import com.keithsmyth.resistance.data.model.PlayerDataModel;
import com.keithsmyth.resistance.feature.lobby.model.CharacterViewModel;
import com.keithsmyth.resistance.presentation.PresenterDelegate;

import java.util.List;

public class LobbyFragment extends Fragment implements LobbyView {

    private PresenterDelegate<LobbyView, LobbyPresenter> presenterDelegate;
    private PlayerAdapter playerAdapter;
    private RecyclerView charactersRecycler;
    private View startGameFab;

    public static LobbyFragment create() {
        return new LobbyFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenterDelegate = new PresenterDelegate<>(getLoaderManager(), getContext(), LobbyPresenter.FACTORY);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_lobby, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final RecyclerView playersRecycler = (RecyclerView) view.findViewById(R.id.players_recycler_view);
        playersRecycler.setHasFixedSize(true);
        playersRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        playerAdapter = new PlayerAdapter();
        playersRecycler.setAdapter(playerAdapter);

        charactersRecycler = (RecyclerView) view.findViewById(R.id.characters_recycler_view);
        startGameFab = view.findViewById(R.id.start_game_fab);
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
    public void setTitle(int currentGameId) {
        getActivity().setTitle(getString(R.string.lobby_title, currentGameId));
    }

    @Override
    public void setPlayers(List<PlayerDataModel> playerDataModels) {
        playerAdapter.setItems(playerDataModels);
    }

    @Override
    public void addPlayer(PlayerDataModel playerDataModel) {
        playerAdapter.addItem(playerDataModel);
    }

    @Override
    public void removePlayer(PlayerDataModel playerDataModel) {
        playerAdapter.removeItem(playerDataModel);
    }

    @Override
    public void showCharacters(List<CharacterViewModel> characters) {
        charactersRecycler.setHasFixedSize(true);
        charactersRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        final CharacterAdapter characterAdapter = new CharacterAdapter(presenterDelegate.presenter);
        characterAdapter.setItems(characters);
        charactersRecycler.setAdapter(characterAdapter);
        charactersRecycler.setVisibility(View.VISIBLE);

        startGameFab.setVisibility(View.VISIBLE);
        startGameFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenterDelegate.presenter.startGame();
            }
        });
    }
}
