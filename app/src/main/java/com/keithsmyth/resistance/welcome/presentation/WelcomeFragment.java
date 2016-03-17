package com.keithsmyth.resistance.welcome.presentation;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.keithsmyth.resistance.Navigator;
import com.keithsmyth.resistance.PresenterLoader;
import com.keithsmyth.resistance.R;
import com.keithsmyth.resistance.lobby.presentation.LobbyFragment;

public class WelcomeFragment extends Fragment implements WelcomeView {

    private static final int LOADER_ID = 101;

    private WelcomePresenter welcomePresenter;
    private Navigator navigator;

    private EditText nameEditText;
    private View newGameButton;
    private View joinGameButton;
    private EditText gameIdEditText;
    private View progressBar;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        navigator = (Navigator) context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLoaderManager().initLoader(LOADER_ID, null, new WelcomeLoaderCallbacks());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_welcome, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        nameEditText = (EditText) view.findViewById(R.id.name_edit_text);
        newGameButton = view.findViewById(R.id.new_game_button);
        newGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                welcomePresenter.newGame();
            }
        });
        joinGameButton = view.findViewById(R.id.join_game_button);
        joinGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                welcomePresenter.joinGame();
            }
        });
        gameIdEditText = (EditText) view.findViewById(R.id.game_id_edit_text);
        progressBar = view.findViewById(R.id.progress_bar);
    }

    @Override
    public void onResume() {
        super.onResume();
        welcomePresenter.attachView(this);
    }

    @Override
    public void onPause() {
        welcomePresenter.detachView();
        super.onPause();
    }

    @Override
    public void setName(String name) {
        nameEditText.setText(name);
    }

    @Override
    public String getNameInput() {
        return nameEditText.getText().toString();
    }

    @Override
    public String getGameIdInput() {
        return gameIdEditText.getText().toString();
    }

    @Override
    public void setLoadingState(boolean isLoading) {
        nameEditText.setEnabled(!isLoading);
        newGameButton.setEnabled(!isLoading);
        joinGameButton.setEnabled(!isLoading);
        gameIdEditText.setEnabled(!isLoading);
        progressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE);
    }

    @Override
    public void showError(String msg) {
        if (getView() != null) {
            Snackbar.make(getView(), msg, Snackbar.LENGTH_LONG).show();
        }
    }

    @Override
    public void showNameError() {
        nameEditText.setError(getString(R.string.welcome_name_error));
    }

    @Override
    public void showGameIdError() {
        gameIdEditText.setError(getString(R.string.welcome_game_id_error));
    }

    @Override
    public void clearErrors() {
        nameEditText.setError(null);
        gameIdEditText.setError(null);
    }

    @Override
    public void openLobby(int gameId) {
        navigator.openFragment(LobbyFragment.create(gameId));
    }

    private class WelcomeLoaderCallbacks implements LoaderManager.LoaderCallbacks<WelcomePresenter> {

        @Override
        public Loader<WelcomePresenter> onCreateLoader(int id, Bundle args) {
            return new PresenterLoader<>(getContext(), WelcomePresenter.FACTORY);
        }

        @Override
        public void onLoadFinished(Loader<WelcomePresenter> loader, WelcomePresenter data) {
            welcomePresenter = data;
        }

        @Override
        public void onLoaderReset(Loader<WelcomePresenter> loader) {
            welcomePresenter = null;
        }
    }
}
