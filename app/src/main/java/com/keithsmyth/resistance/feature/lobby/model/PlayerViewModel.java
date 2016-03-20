package com.keithsmyth.resistance.feature.lobby.model;

public class PlayerViewModel {

    public final String id;
    public final String name;
    public final boolean isOwner;
    public final boolean isMe;

    public PlayerViewModel(String id, String name, boolean isOwner, boolean isMe) {
        this.id = id;
        this.name = name;
        this.isOwner = isOwner;
        this.isMe = isMe;
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        return !(o == null || !(o instanceof PlayerViewModel)) && id.equals(((PlayerViewModel) o).id);
    }
}
