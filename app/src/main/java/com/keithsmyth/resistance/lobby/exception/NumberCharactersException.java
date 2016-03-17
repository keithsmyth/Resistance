package com.keithsmyth.resistance.lobby.exception;

public class NumberCharactersException extends Throwable {

    public final int expectedGood;
    public final int actualGood;
    public final int expectedBad;
    public final int actualBad;

    public NumberCharactersException(int expectedGood, int actualGood, int expectedBad, int actualBad) {
        this.expectedGood = expectedGood;
        this.actualGood = actualGood;
        this.expectedBad = expectedBad;
        this.actualBad = actualBad;
    }
}
