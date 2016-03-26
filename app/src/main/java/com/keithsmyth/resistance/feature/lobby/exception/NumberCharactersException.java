package com.keithsmyth.resistance.feature.lobby.exception;

import android.content.Context;

import com.keithsmyth.resistance.R;
import com.keithsmyth.resistance.navigation.DisplayThrowable;

public class NumberCharactersException extends DisplayThrowable {

    private final int expectedGood;
    private final int actualGood;
    private final int expectedBad;
    private final int actualBad;

    public NumberCharactersException(int expectedGood, int actualGood, int expectedBad, int actualBad) {
        this.expectedGood = expectedGood;
        this.actualGood = actualGood;
        this.expectedBad = expectedBad;
        this.actualBad = actualBad;
    }

    @Override
    public String getDisplayMessage(Context context) {
        return context.getString(R.string.lobby_number_characters_error, expectedGood, actualGood, expectedBad, actualBad);
    }
}
