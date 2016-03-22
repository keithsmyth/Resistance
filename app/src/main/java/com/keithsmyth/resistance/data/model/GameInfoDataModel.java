package com.keithsmyth.resistance.data.model;

import java.util.Map;

public class GameInfoDataModel {

    private String ownerId;
    private int status;
    private Map<String, String> mapPlayerIdToName;
    private Map<String, Integer> mapPlayerIdToOrder;
    private Map<String, String> mapPlayerIdToCharacter;

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Map<String, String> getMapPlayerIdToName() {
        return mapPlayerIdToName;
    }

    public void setMapPlayerIdToName(Map<String, String> mapPlayerIdToName) {
        this.mapPlayerIdToName = mapPlayerIdToName;
    }

    public Map<String, Integer> getMapPlayerIdToOrder() {
        return mapPlayerIdToOrder;
    }

    public void setMapPlayerIdToOrder(Map<String, Integer> mapPlayerIdToOrder) {
        this.mapPlayerIdToOrder = mapPlayerIdToOrder;
    }

    public Map<String, String> getMapPlayerIdToCharacter() {
        return mapPlayerIdToCharacter;
    }

    public void setMapPlayerIdToCharacter(Map<String, String> mapPlayerIdToCharacter) {
        this.mapPlayerIdToCharacter = mapPlayerIdToCharacter;
    }
}
