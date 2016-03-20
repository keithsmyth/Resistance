package com.keithsmyth.resistance.feature.lobby.presentation;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.keithsmyth.resistance.PresenterLoader;
import com.keithsmyth.resistance.R;
import com.keithsmyth.resistance.data.model.PlayerDataModel;
import com.keithsmyth.resistance.feature.lobby.model.CharacterViewModel;

import java.util.List;

public class LobbyFragment extends Fragment implements LobbyView {

    private static final int LOADER_ID = 101;

    private LobbyPresenter lobbyPresenter;
    private PlayerAdapter playerAdapter;
    private RecyclerView charactersRecycler;

    public static LobbyFragment create() {
        return new LobbyFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLoaderManager().initLoader(LOADER_ID, null, new LobbyLoaderCallbacks());
        getActivity().setTitle(R.string.lobby_title);
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

        view.findViewById(R.id.start_game_fab).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lobbyPresenter.startGame();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        lobbyPresenter.attachView(this);
    }

    @Override
    public void onPause() {
        lobbyPresenter.detachView();
        super.onPause();
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
        final CharacterAdapter characterAdapter = new CharacterAdapter(lobbyPresenter);
        characterAdapter.setItems(characters);
        charactersRecycler.setAdapter(characterAdapter);
        charactersRecycler.setVisibility(View.VISIBLE);
    }

    private class LobbyLoaderCallbacks implements LoaderManager.LoaderCallbacks<LobbyPresenter> {

        @Override
        public Loader<LobbyPresenter> onCreateLoader(int id, Bundle args) {
            return new PresenterLoader<>(getContext(), LobbyPresenter.FACTORY);
        }

        @Override
        public void onLoadFinished(Loader<LobbyPresenter> loader, LobbyPresenter data) {
            lobbyPresenter = data;
        }

        @Override
        public void onLoaderReset(Loader<LobbyPresenter> loader) {
            lobbyPresenter = null;
        }
    }
}
