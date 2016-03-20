package com.keithsmyth.resistance.data.model;

public class PlayerDataModel {

    public final String id;
    public final String name;
    public final boolean isOwner;
    public final boolean isMe;

    public PlayerDataModel(String id, String name, boolean isOwner, boolean isMe) {
        this.id = id;
        this.name = name;
        this.isOwner = isOwner;
        this.isMe = isMe;
    }
}
