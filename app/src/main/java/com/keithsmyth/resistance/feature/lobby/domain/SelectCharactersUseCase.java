package com.keithsmyth.resistance.feature.lobby.domain;

import com.keithsmyth.resistance.data.CharacterProvider;
import com.keithsmyth.resistance.data.GameInfoProvider;
import com.keithsmyth.resistance.data.GameRulesProvider;
import com.keithsmyth.resistance.data.model.GameRulesDataModel;
import com.keithsmyth.resistance.data.model.PlayerDataModel;
import com.keithsmyth.resistance.data.model.CharacterDataModel;
import com.keithsmyth.resistance.feature.lobby.exception.NumberCharactersException;
import com.keithsmyth.resistance.feature.lobby.exception.NumberPlayersException;
import com.keithsmyth.resistance.feature.lobby.mapper.CharacterMapper;
import com.keithsmyth.resistance.feature.lobby.model.CharacterViewModel;
import com.keithsmyth.resistance.navigation.Navigation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import static com.keithsmyth.resistance.data.GameRulesProvider.MAX_PLAYERS;
import static com.keithsmyth.resistance.data.GameRulesProvider.MIN_PLAYERS;

public class SelectCharactersUseCase {

    private final Navigation navigation;
    private final GameInfoProvider gameInfoProvider;
    private final CharacterProvider characterProvider;
    private final GameRulesProvider gameRulesProvider;
    private final CharacterMapper characterMapper;

    public SelectCharactersUseCase(Navigation navigation, GameInfoProvider gameInfoProvider, CharacterProvider characterProvider, GameRulesProvider gameRulesProvider) {
        this.navigation = navigation;
        this.gameInfoProvider = gameInfoProvider;
        this.characterProvider = characterProvider;
        this.gameRulesProvider = gameRulesProvider;
        characterMapper = new CharacterMapper();
    }

    public List<CharacterViewModel> getCharacters() {
        final List<CharacterDataModel> dataModels = characterProvider.getSelectableCharacters();
        final List<CharacterViewModel> characters = new ArrayList<>(dataModels.size());
        for (CharacterDataModel dataModel : dataModels) {
            characters.add(characterMapper.toViewModel(dataModel));
        }
        return characters;
    }

    public void selectCharacters(List<CharacterViewModel> characterViewModels, List<PlayerDataModel> playerDataModels) {
        // lock down lobby
        gameInfoProvider.setGameState(GameInfoProvider.STATE_STARTING);

        // verify number of players
        int numberOfPlayers = playerDataModels.size();
        if (numberOfPlayers < MIN_PLAYERS || numberOfPlayers > MAX_PLAYERS) {
            gameInfoProvider.setGameState(GameInfoProvider.STATE_NEW);
            final NumberPlayersException numberPlayersException = new NumberPlayersException(MIN_PLAYERS, MAX_PLAYERS, numberOfPlayers);
            navigation.showError(numberPlayersException);
            return;
        }

        // verify good / bad ratio
        int goodCharacters = 0;
        int badCharacters = 0;
        for (CharacterViewModel characterViewModel : characterViewModels) {
            if (characterViewModel.isBad) {
                badCharacters++;
            } else {
                goodCharacters++;
            }
        }
        final GameRulesDataModel gameRulesDataModel = gameRulesProvider.getGameRules(numberOfPlayers);
        if (badCharacters > gameRulesDataModel.totalBadPlayers || goodCharacters > gameRulesDataModel.totalGoodPlayers) {
            gameInfoProvider.setGameState(GameInfoProvider.STATE_NEW);
            final NumberCharactersException numberCharactersException = new NumberCharactersException(gameRulesDataModel.totalGoodPlayers, goodCharacters, gameRulesDataModel.totalBadPlayers, badCharacters);
            navigation.showError(numberCharactersException);
            return;
        }

        // assign characters to players
        final List<String> unassignedCharacters = new ArrayList<>(playerDataModels.size());
        for (CharacterViewModel characterViewModel : characterViewModels) {
            unassignedCharacters.add(characterViewModel.name);
        }
        for (int i = goodCharacters; i < gameRulesDataModel.totalGoodPlayers; i++) {
            unassignedCharacters.add(characterProvider.getServant().name);
        }
        for (int i = badCharacters; i < gameRulesDataModel.totalBadPlayers; i++) {
            unassignedCharacters.add(characterProvider.getMinion().name);
        }
        final Random rand = new Random();
        final Map<String, Object> mapPlayerIdToCharacter = new HashMap<>(playerDataModels.size());
        for (PlayerDataModel playerDataModel : playerDataModels) {
            final int index = rand.nextInt(unassignedCharacters.size());
            mapPlayerIdToCharacter.put(playerDataModel.id, unassignedCharacters.get(index));
            unassignedCharacters.remove(index);
        }

        // save
        gameInfoProvider.setAssignedCharacters(mapPlayerIdToCharacter);

        // start the game
        gameInfoProvider.setGameState(GameInfoProvider.STATE_STARTED);
    }
}
