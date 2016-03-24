package com.keithsmyth.resistance.data.model;

import java.util.Map;

public class GamePlayDataModel {

    private int currentRound;
    private int voteFails;
    private int questFails;
    private Map<String, GameRoundDataModel> mapRoundNumberToRound;

    public int getCurrentRound() {
        return currentRound;
    }

    public void setCurrentRound(int currentRound) {
        this.currentRound = currentRound;
    }

    public int getVoteFails() {
        return voteFails;
    }

    public void setVoteFails(int voteFails) {
        this.voteFails = voteFails;
    }

    public int getQuestFails() {
        return questFails;
    }

    public void setQuestFails(int questFails) {
        this.questFails = questFails;
    }

    public Map<String, GameRoundDataModel> getMapRoundNumberToRound() {
        return mapRoundNumberToRound;
    }

    public void setMapRoundNumberToRound(Map<String, GameRoundDataModel> mapRoundNumberToRound) {
        this.mapRoundNumberToRound = mapRoundNumberToRound;
    }
}
