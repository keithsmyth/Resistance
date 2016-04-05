package com.keithsmyth.data.model;

import java.util.Map;

@SuppressWarnings({"SameParameterValue", "unused"})
public class GameRoundDataModel {

    private String captain;
    private int status;
    private Map<String, Object> mapPlayerIdForTeam;
    private Map<String, Boolean> mapPlayerIdToVote;
    private Map<String, Boolean> mapPlayerIdToQuest;

    public String getCaptain() {
        return captain;
    }

    public void setCaptain(String captain) {
        this.captain = captain;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Map<String, Object> getMapPlayerIdForTeam() {
        return mapPlayerIdForTeam;
    }

    public void setMapPlayerIdForTeam(Map<String, Object> mapPlayerIdForTeam) {
        this.mapPlayerIdForTeam = mapPlayerIdForTeam;
    }

    public Map<String, Boolean> getMapPlayerIdToVote() {
        return mapPlayerIdToVote;
    }

    public void setMapPlayerIdToVote(Map<String, Boolean> mapPlayerIdToVote) {
        this.mapPlayerIdToVote = mapPlayerIdToVote;
    }

    public Map<String, Boolean> getMapPlayerIdToQuest() {
        return mapPlayerIdToQuest;
    }

    public void setMapPlayerIdToQuest(Map<String, Boolean> mapPlayerIdToQuest) {
        this.mapPlayerIdToQuest = mapPlayerIdToQuest;
    }
}
