package com.keithsmyth.resistance.lobby.domain;

import com.keithsmyth.resistance.data.CharacterProvider;
import com.keithsmyth.resistance.data.GameProvider;
import com.keithsmyth.resistance.data.GameRulesProvider;
import com.keithsmyth.resistance.data.UserProvider;
import com.keithsmyth.resistance.data.model.CharacterDataModel;
import com.keithsmyth.resistance.data.model.GameRulesDataModel;
import com.keithsmyth.resistance.lobby.exception.NumberPlayersException;
import com.keithsmyth.resistance.lobby.exception.NumberCharactersException;
import com.keithsmyth.resistance.lobby.mapper.CharacterMapper;
import com.keithsmyth.resistance.lobby.model.CharacterViewModel;
import com.keithsmyth.resistance.lobby.model.PlayerViewModel;
import com.keithsmyth.resistance.navigation.Navigation;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Subscriber;

import static com.keithsmyth.resistance.data.GameRulesProvider.MAX_PLAYERS;
import static com.keithsmyth.resistance.data.GameRulesProvider.MIN_PLAYERS;

public class SelectCharactersUseCase {

    private final Navigation navigation;
    private final UserProvider userProvider;
    private final GameProvider gameProvider;
    private final CharacterProvider characterProvider;
    private final GameRulesProvider gameRulesProvider;
    private final CharacterMapper characterMapper;

    public SelectCharactersUseCase(Navigation navigation, UserProvider userProvider, GameProvider gameProvider, CharacterProvider characterProvider, GameRulesProvider gameRulesProvider) {
        this.navigation = navigation;
        this.userProvider = userProvider;
        this.gameProvider = gameProvider;
        this.characterProvider = characterProvider;
        this.gameRulesProvider = gameRulesProvider;
        characterMapper = new CharacterMapper();
    }

    public List<CharacterViewModel> getCharacters() {
        final List<CharacterDataModel> dataModels = characterProvider.getCharacters();
        final List<CharacterViewModel> characters = new ArrayList<>(dataModels.size());
        for (CharacterDataModel dataModel : dataModels) {
            characters.add(characterMapper.toViewModel(dataModel));
        }
        return characters;
    }

    public void selectCharacters(List<CharacterViewModel> characterViewModels, List<PlayerViewModel> playerViewModels) {
        // lock down lobby
        gameProvider.setGameState(GameProvider.STATE_STARTING);

        // verify number of players
        int numberOfPlayers = playerViewModels.size();
        if (numberOfPlayers < MIN_PLAYERS || numberOfPlayers > MAX_PLAYERS) {
            gameProvider.setGameState(GameProvider.STATE_JOINING);
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
        if (badCharacters > gameRulesDataModel.badPlayers || goodCharacters > gameRulesDataModel.goodPlayers) {
            gameProvider.setGameState(GameProvider.STATE_JOINING);
            final NumberCharactersException numberCharactersException = new NumberCharactersException(gameRulesDataModel.goodPlayers, goodCharacters, gameRulesDataModel.badPlayers, badCharacters);
            navigation.showError(numberCharactersException);
            return;
        }

        // TODO: assign characters to players

        // TODO: save

        // start the game
        gameProvider.setGameState(GameProvider.STATE_STARTED);
        navigation.openGame();
    }
}
