package com.keithsmyth.resistance.admin.gameslist;

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

import com.keithsmyth.resistance.admin.Nav;
import com.keithsmyth.resistance.admin.R;
import com.keithsmyth.resistance.admin.gamedetail.GameDetailFragment;

import java.util.List;

public class GamesListFragment extends Fragment implements GamesListView {

    private static final int LOADER_ID = 101;

    private Nav nav;
    private GamesListPresenter gameListPresenter;
    private GamesListAdapter adapter;

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
        return inflater.inflate(R.layout.fragment_games_list, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.games_recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new GamesListAdapter(new OnGameNumberClick());
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onResume() {
        super.onResume();
        gameListPresenter.onAttach(this);
    }

    @Override
    public void onPause() {
        gameListPresenter.onDetach();
        super.onPause();
    }

    @Override
    public void setItems(List<Integer> items) {
        if (adapter != null) {
            adapter.setGameNumbers(items);
        }
    }

    @Override
    public void addItem(int item) {
        if (adapter != null) {
            adapter.addGameNumber(item);
        }
    }

    @Override
    public void removeItem(int item) {
        if (adapter != null) {
            adapter.removeGameNumber(item);
        }
    }

    @Override
    public void openGameDetailView() {
        nav.open(new GameDetailFragment());
    }

    private class LoaderCallbacks implements LoaderManager.LoaderCallbacks<GamesListPresenter> {

        @Override
        public Loader<GamesListPresenter> onCreateLoader(int id, Bundle args) {
            return new GamesListLoader(getContext());
        }

        @Override
        public void onLoadFinished(Loader<GamesListPresenter> loader, GamesListPresenter data) {
            gameListPresenter = data;
        }

        @Override
        public void onLoaderReset(Loader<GamesListPresenter> loader) {
            gameListPresenter = null;
        }
    }

    private class OnGameNumberClick implements GamesListAdapter.Listener {

        @Override
        public void onGameNumberClick(int gameNumber) {
            gameListPresenter.onGameClick(gameNumber);
        }
    }
}
