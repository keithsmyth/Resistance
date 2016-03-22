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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final PlayerDataModel that = (PlayerDataModel) o;
        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
