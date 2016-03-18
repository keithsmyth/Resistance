package com.keithsmyth.resistance.lobby.presentation;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.keithsmyth.resistance.Navigator;
import com.keithsmyth.resistance.PresenterLoader;
import com.keithsmyth.resistance.R;
import com.keithsmyth.resistance.game.GameFragment;
import com.keithsmyth.resistance.lobby.exception.NumberPlayersException;
import com.keithsmyth.resistance.lobby.exception.NumberCharactersException;
import com.keithsmyth.resistance.lobby.model.CharacterViewModel;
import com.keithsmyth.resistance.lobby.model.PlayerViewModel;

import java.util.List;

public class LobbyFragment extends Fragment implements LobbyView {

    private static final String KEY_GAME_ID = "KEY_GAME_ID";
    private static final int LOADER_ID = 101;

    private LobbyPresenter lobbyPresenter;
    private Navigator navigator;
    private PlayerAdapter playerAdapter;
    private RecyclerView charactersRecycler;

    public static LobbyFragment create(int gameId) {
        final Bundle args = new Bundle();
        args.putInt(KEY_GAME_ID, gameId);
        final LobbyFragment fragment = new LobbyFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        navigator = (Navigator) context;
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
                lobbyPresenter.startGame(playerAdapter.getItemCount());
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
    public void setPlayers(List<PlayerViewModel> playerViewModels) {
        playerAdapter.setItems(playerViewModels);
    }

    @Override
    public void addPlayer(PlayerViewModel playerViewModel) {
        playerAdapter.addItem(playerViewModel);
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

    @Override
    public void showError(String msg) {
        if (getView() != null) {
            Snackbar.make(getView(), msg, Snackbar.LENGTH_LONG).show();
        }
    }

    @Override
    public void showNumberPlayersError(NumberPlayersException numberPlayersException) {
        showError(getString(R.string.lobby_number_players_error,
            numberPlayersException.minPlayers,
            numberPlayersException.maxPlayers,
            numberPlayersException.currentPlayers));
    }

    @Override
    public void showSelectCharactersError(NumberCharactersException numberCharactersException) {
        showError(getString(R.string.lobby_number_characters_error,
            numberCharactersException.expectedGood,
            numberCharactersException.actualGood,
            numberCharactersException.expectedBad,
            numberCharactersException.actualBad));
    }

    @Override
    public void openGameRoom(int gameId) {
        navigator.openFragment(GameFragment.create(gameId));
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
