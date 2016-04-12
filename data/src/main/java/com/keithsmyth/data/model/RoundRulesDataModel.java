package com.keithsmyth.data.model;

public class RoundRulesDataModel {

    public final int missionNumber;
    public final int numberOfPlayers;
    public final int numberOfFails;

    public RoundRulesDataModel(int missionNumber, int numberOfPlayers, int numberOfFails) {
        this.missionNumber = missionNumber;
        this.numberOfPlayers = numberOfPlayers;
        this.numberOfFails = numberOfFails;
    }
}
