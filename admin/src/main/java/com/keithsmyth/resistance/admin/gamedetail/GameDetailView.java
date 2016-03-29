package com.keithsmyth.resistance.admin.gamedetail;

import java.util.List;

public interface GameDetailView {

    void setGameNumber(int currentGameId);

    void setGameOwner(String gameOwner);

    void setStatus(String statusText);

    void setPlayers(List<PlayerViewModel> players);

    void showError(String s);

    void navigateToGameList();
}
