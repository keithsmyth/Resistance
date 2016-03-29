package com.keithsmyth.resistance.admin.gamedetail;

import android.content.Context;
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
import android.widget.TextView;
import android.widget.Toast;

import com.keithsmyth.resistance.admin.Nav;
import com.keithsmyth.resistance.admin.R;
import com.keithsmyth.resistance.admin.gameslist.GamesListFragment;

import java.util.List;

public class GameDetailFragment extends Fragment implements GameDetailView {

    private static final int LOADER_ID = 101;

    private Nav nav;
    private GameDetailPresenter gameDetailPresenter;
    private TextView gameNumberText;
    private TextView gameOwnerText;
    private TextView gameStatusText;
    private PlayerAdapter adapter;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        nav = (Nav) context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLoaderManager().initLoader(LOADER_ID, null, new LoaderCallbacks());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_game_detail, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        gameNumberText = (TextView) view.findViewById(R.id.game_number_text);
        gameOwnerText = (TextView) view.findViewById(R.id.game_owner_text);
        gameStatusText = (TextView) view.findViewById(R.id.game_status_text);
        final RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.players_recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new PlayerAdapter();
        recyclerView.setAdapter(adapter);
        view.findViewById(R.id.add_player_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onAddPlayerClick();
            }
        });
        view.findViewById(R.id.delete_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onDeleteClick();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        gameDetailPresenter.onAttach(this);
    }

    @Override
    public void onPause() {
        gameDetailPresenter.onDetach();
        super.onPause();
    }

    @Override
    public void setGameNumber(int currentGameId) {
        gameNumberText.setText("Game number: " + currentGameId);
    }

    @Override
    public void setGameOwner(String gameOwner) {
        gameOwnerText.setText("Game owner: " + gameOwner);
    }

    @Override
    public void setStatus(String statusText) {
        gameStatusText.setText("Status: " + statusText);
    }

    @Override
    public void setPlayers(List<PlayerViewModel> players) {
        adapter.setPlayers(players);
    }

    @Override
    public void showError(String s) {
        Toast.makeText(getContext(), s, Toast.LENGTH_LONG).show();
    }

    @Override
    public void navigateToGameList() {
        nav.open(new GamesListFragment());
    }

    private void onAddPlayerClick() {
        gameDetailPresenter.addNewPlayer();
    }

    private void onDeleteClick() {
        gameDetailPresenter.deleteGame();
    }

    private class LoaderCallbacks implements LoaderManager.LoaderCallbacks<GameDetailPresenter> {

        @Override
        public Loader<GameDetailPresenter> onCreateLoader(int id, Bundle args) {
            return new GameDetailLoader(getContext());
        }

        @Override
        public void onLoadFinished(Loader<GameDetailPresenter> loader, GameDetailPresenter data) {
            gameDetailPresenter = data;
        }

        @Override
        public void onLoaderReset(Loader<GameDetailPresenter> loader) {
            gameDetailPresenter = null;
        }
    }
}
