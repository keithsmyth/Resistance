package com.keithsmyth.data.model;

public class GameRulesDataModel {

    public final int totalGoodPlayers;
    public final int totalBadPlayers;

    private final MissionRulesDataModel[] missionRulesDataModels;

    public GameRulesDataModel(int totalGoodPlayers, int totalBadPlayers, MissionRulesDataModel[] missionRulesDataModels) {
        this.totalGoodPlayers = totalGoodPlayers;
        this.totalBadPlayers = totalBadPlayers;
        this.missionRulesDataModels = missionRulesDataModels;
    }

    public MissionRulesDataModel getMissionRulesDataModel(int missionNumber) {
        return missionRulesDataModels[missionNumber - 1];
    }
}
