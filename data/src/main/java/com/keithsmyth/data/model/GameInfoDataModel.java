package com.keithsmyth.data.model;

import com.keithsmyth.data.provider.GameInfoProvider;

import java.util.Map;

@SuppressWarnings({"SameParameterValue", "unused"})
public class GameInfoDataModel {

    private String version;
    private String ownerId;
    private int status;
    private Map<String, String> mapPlayerIdToName;
    private Map<String, Integer> mapPlayerIdToOrder;
    private Map<String, String> mapPlayerIdToCharacter;

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    @GameInfoProvider.GameState
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
