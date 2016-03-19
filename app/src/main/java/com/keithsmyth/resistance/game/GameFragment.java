package com.keithsmyth.resistance.game;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.keithsmyth.resistance.R;

public class GameFragment extends Fragment {

    public static GameFragment create() {
        return new GameFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().setTitle(R.string.game_title);
    }
}
