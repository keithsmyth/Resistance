package com.keithsmyth.resistance.lobby.presentation;

import com.keithsmyth.resistance.lobby.exception.NumberPlayersException;
import com.keithsmyth.resistance.lobby.exception.NumberCharactersException;
import com.keithsmyth.resistance.lobby.model.CharacterViewModel;
import com.keithsmyth.resistance.lobby.model.PlayerViewModel;

import java.util.List;

public interface LobbyView {
    void addPlayer(PlayerViewModel playerViewModel);

    void showCharacters(List<CharacterViewModel> characters);

    void showError(String msg);

    void showNumberPlayersError(NumberPlayersException numberPlayersException);

    void showSelectCharactersError(NumberCharactersException numberCharactersException);

    void openGameRoom(int gameId);
}
