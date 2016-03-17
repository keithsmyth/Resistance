package com.keithsmyth.resistance.lobby.exception;

public class NumberPlayersException extends Throwable {

    public final int minPlayers;
    public final int maxPlayers;
    public final int currentPlayers;

    public NumberPlayersException(int minPlayers, int maxPlayers, int currentPlayers) {
        this.minPlayers = minPlayers;
        this.maxPlayers = maxPlayers;
        this.currentPlayers = currentPlayers;
    }

}
