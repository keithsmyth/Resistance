package com.keithsmyth.data.model;

public class GameRulesDataModel {

    public final int totalGoodPlayers;
    public final int totalBadPlayers;

    private final RoundRulesDataModel[] roundRulesDataModels;

    public GameRulesDataModel(int totalGoodPlayers, int totalBadPlayers, RoundRulesDataModel[] roundRulesDataModels) {
        this.totalGoodPlayers = totalGoodPlayers;
        this.totalBadPlayers = totalBadPlayers;
        this.roundRulesDataModels = roundRulesDataModels;
    }

    public RoundRulesDataModel getRoundRulesDataModel(int missionNumber) {
        return roundRulesDataModels[missionNumber - 1];
    }

    public int getTotalRounds() {
        return roundRulesDataModels.length;
    }
}
