package com.keithsmyth.resistance.feature.welcome.presentation;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.keithsmyth.resistance.R;
import com.keithsmyth.resistance.presentation.PresenterDelegate;

public class WelcomeFragment extends Fragment implements WelcomeView {

    private PresenterDelegate<WelcomeView, WelcomePresenter> presenterDelegate;
    private EditText nameEditText;
    private View newGameButton;
    private View joinGameButton;
    private EditText gameIdEditText;
    private View progressBar;

    public static WelcomeFragment create() {
        return new WelcomeFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenterDelegate = new PresenterDelegate<>(getLoaderManager(), getContext(), WelcomePresenter.FACTORY);
        getActivity().setTitle(R.string.app_name);
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
                presenterDelegate.presenter.newGame();
            }
        });
        joinGameButton = view.findViewById(R.id.join_game_button);
        joinGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenterDelegate.presenter.joinGame();
            }
        });
        gameIdEditText = (EditText) view.findViewById(R.id.game_id_edit_text);
        progressBar = view.findViewById(R.id.progress_bar);
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
    public void setName(String name) {
        nameEditText.setText(name);
    }

    @Override
    public String getNameInput() {
        return nameEditText.getText().toString();
    }

    @Override
    public void setGame(int gameId) {
        gameIdEditText.setText(String.valueOf(gameId));
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
    public void onErrorShown() {
        presenterDelegate.presenter.onErrorShown();
    }
}
