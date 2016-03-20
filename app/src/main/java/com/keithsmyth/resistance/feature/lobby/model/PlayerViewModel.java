package com.keithsmyth.resistance.feature.lobby.model;

public class PlayerViewModel {

    public final String uuid;
    public final String name;
    public final boolean isOwner;
    public final boolean isMe;

    public PlayerViewModel(String uuid, String name, boolean isOwner, boolean isMe) {
        this.uuid = uuid;
        this.name = name;
        this.isOwner = isOwner;
        this.isMe = isMe;
    }

    @Override
    public int hashCode() {
        return uuid.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        return !(o == null || !(o instanceof PlayerViewModel)) && uuid.equals(((PlayerViewModel) o).uuid);
    }
}
