package com.keithsmyth.resistance.game;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.keithsmyth.resistance.R;

public class GameFragment extends Fragment {

    private static final String KEY_GAME_ID = "KEY_GAME_ID";

    public static GameFragment create(int gameId) {
        final Bundle args = new Bundle();
        args.putInt(KEY_GAME_ID, gameId);
        final GameFragment fragment = new GameFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().setTitle(R.string.game_title);
    }
}
