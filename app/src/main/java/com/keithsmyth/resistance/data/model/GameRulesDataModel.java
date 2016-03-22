package com.keithsmyth.resistance.data.model;

public class GameRulesDataModel {

    public final int numberOfPlayers;
    public final int totalGoodPlayers;
    public final int totalBadPlayers;
    public final MissionRulesDataModel[] missionRulesDataModels;

    public GameRulesDataModel(int numberOfPlayers, int totalGoodPlayers, int totalBadPlayers, MissionRulesDataModel[] missionRulesDataModels) {
        this.numberOfPlayers = numberOfPlayers;
        this.totalGoodPlayers = totalGoodPlayers;
        this.totalBadPlayers = totalBadPlayers;
        this.missionRulesDataModels = missionRulesDataModels;
    }
}
