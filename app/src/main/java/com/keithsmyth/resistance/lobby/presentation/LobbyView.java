package com.keithsmyth.resistance.lobby.presentation;

import com.keithsmyth.resistance.lobby.model.CharacterViewModel;
import com.keithsmyth.resistance.lobby.model.PlayerViewModel;

import java.util.List;

public interface LobbyView {
    void setPlayers(List<PlayerViewModel> playerViewModels);

    void addPlayer(PlayerViewModel playerViewModel);

    void showCharacters(List<CharacterViewModel> characters);
}
