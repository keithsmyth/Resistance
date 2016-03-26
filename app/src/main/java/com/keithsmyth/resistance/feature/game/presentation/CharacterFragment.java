package com.keithsmyth.resistance.feature.game.presentation;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.keithsmyth.resistance.R;
import com.keithsmyth.resistance.feature.game.model.PlayerCharacterViewModel;
import com.keithsmyth.resistance.presentation.PresenterDelegate;

import java.util.List;

public class CharacterFragment extends Fragment implements CharacterView {

    private PresenterDelegate<CharacterView, CharacterPresenter> presenterDelegate;
    private TextView characterText;
    private TextView gameIdText;

    public static CharacterFragment create() {
        return new CharacterFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenterDelegate = new PresenterDelegate<>(getLoaderManager(), getContext(), CharacterPresenter.FACTORY);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_character, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        characterText = (TextView) view.findViewById(R.id.character_text);
        gameIdText = (TextView) view.findViewById(R.id.game_id_text);
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
    public void showGameId(int currentGameId) {
        gameIdText.setText(getString(R.string.game_id, currentGameId));
    }

    @Override
    public void displayPlayerCharacterInfo(PlayerCharacterViewModel playerCharacterViewModel) {
        characterText.setText(getString(R.string.game_character_info,
            playerCharacterViewModel.name,
            playerCharacterViewModel.characterName,
            getString(playerCharacterViewModel.isBad ? R.string.bad : R.string.good),
            playerCharacterViewModel.revealedDescription,
            join(playerCharacterViewModel.revealedNames),
            join(playerCharacterViewModel.characters)));
    }

    private String join(List<String> list) {
        final StringBuilder builder = new StringBuilder();
        for (String item : list) {
            builder.append(item);
            builder.append("\n");
        }
        return builder.toString();
    }
}
