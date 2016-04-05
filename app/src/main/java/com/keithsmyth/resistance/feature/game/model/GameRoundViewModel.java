package com.keithsmyth.resistance.feature.game.model;

import java.util.Set;

public class GameRoundViewModel {

    public final int roundNumber;
    public final String captainId;
    public final String captainName;
    public final int status;
    public final Set<String> teamPlayerIds;

    public GameRoundViewModel(int roundNumber, String captainId, String captainName, int status, Set<String> teamPlayerIds) {
        this.roundNumber = roundNumber;
        this.captainId = captainId;
        this.captainName = captainName;
        this.status = status;
        this.teamPlayerIds = teamPlayerIds;
    }
}
