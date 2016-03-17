package com.keithsmyth.resistance.data.model;

public class GameRulesDataModel {

    public final int numberOfPlayers;
    public final int goodPlayers;
    public final int badPlayers;
    public final MissionRulesDataModel[] missionRulesDataModels;

    public GameRulesDataModel(int numberOfPlayers, int goodPlayers, int badPlayers, MissionRulesDataModel[] missionRulesDataModels) {
        this.numberOfPlayers = numberOfPlayers;
        this.goodPlayers = goodPlayers;
        this.badPlayers = badPlayers;
        this.missionRulesDataModels = missionRulesDataModels;
    }
}
