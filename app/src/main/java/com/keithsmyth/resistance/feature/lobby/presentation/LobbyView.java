package com.keithsmyth.resistance.feature.lobby.presentation;

import com.keithsmyth.data.model.PlayerDataModel;
import com.keithsmyth.resistance.feature.lobby.model.CharacterViewModel;

import java.util.List;

interface LobbyView {

    void setTitle(int currentGameId);

    void setPlayers(List<PlayerDataModel> playerDataModels);

    void addPlayer(PlayerDataModel playerDataModel);

    void removePlayer(PlayerDataModel playerDataModel);

    void showCharacters(List<CharacterViewModel> characters);
}
