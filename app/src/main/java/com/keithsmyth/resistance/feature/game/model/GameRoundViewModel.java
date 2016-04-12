package com.keithsmyth.resistance.feature.game.model;

import java.util.Map;

public class GameRoundViewModel {

    public final int roundNumber;
    public final int numberOfPlayers;
    public final int numberOfFails;
    public final String captainId;
    public final String captainName;
    public final int status;
    public final Map<String, String> playerIdToNameMap;

    public GameRoundViewModel(int roundNumber, int numberOfPlayers, int numberOfFails, String captainId, String captainName, int status, Map<String, String> playerIdToNameMap) {
        this.roundNumber = roundNumber;
        this.numberOfPlayers = numberOfPlayers;
        this.numberOfFails = numberOfFails;
        this.captainId = captainId;
        this.captainName = captainName;
        this.status = status;
        this.playerIdToNameMap = playerIdToNameMap;
    }
}
