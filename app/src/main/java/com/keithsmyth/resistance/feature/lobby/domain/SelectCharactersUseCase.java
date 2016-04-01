package com.keithsmyth.resistance.feature.lobby.domain;

import android.support.annotation.NonNull;

import com.keithsmyth.data.model.CharacterDataModel;
import com.keithsmyth.data.model.GameRulesDataModel;
import com.keithsmyth.data.model.PlayerDataModel;
import com.keithsmyth.data.provider.CharacterProvider;
import com.keithsmyth.data.provider.GameInfoProvider;
import com.keithsmyth.data.provider.GameRulesProvider;
import com.keithsmyth.resistance.feature.lobby.exception.NumberCharactersThrowable;
import com.keithsmyth.resistance.feature.lobby.exception.NumberPlayersThrowable;
import com.keithsmyth.resistance.feature.lobby.mapper.CharacterMapper;
import com.keithsmyth.resistance.feature.lobby.model.CharacterViewModel;
import com.keithsmyth.resistance.navigation.Navigation;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import rx.Single;
import rx.functions.Func1;

import static com.keithsmyth.data.provider.GameRulesProvider.MAX_PLAYERS;
import static com.keithsmyth.data.provider.GameRulesProvider.MIN_PLAYERS;

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

    public Single<Boolean> execute(List<CharacterViewModel> characterViewModels, List<PlayerDataModel> playerDataModels) {
        // lock down lobby
        gameInfoProvider.setGameState(GameInfoProvider.STATE_STARTING);

        // verify number of players
        int numberOfPlayers = playerDataModels.size();
        if (numberOfPlayers < MIN_PLAYERS || numberOfPlayers > MAX_PLAYERS) {
            gameInfoProvider.setGameState(GameInfoProvider.STATE_NEW);
            navigation.showError(new NumberPlayersThrowable(numberOfPlayers));
            return Single.just(false);
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
            navigation.showError(new NumberCharactersThrowable(gameRulesDataModel.totalGoodPlayers, goodCharacters, gameRulesDataModel.totalBadPlayers, badCharacters));
            return Single.just(false);
        }

        // assign characters to players
        final Map<String, Object> mapPlayerIdToCharacter;
        Calendar c = Calendar.getInstance();
        if (c.get(Calendar.MONTH) == Calendar.APRIL && c.get(Calendar.DAY_OF_MONTH) == 1) {
            mapPlayerIdToCharacter = assignCharactersAprilFools(playerDataModels);
        } else {
            mapPlayerIdToCharacter = assignCharacters(characterViewModels, playerDataModels, goodCharacters, badCharacters, gameRulesDataModel);
        }

        // save
        return gameInfoProvider.setAssignedCharacters(mapPlayerIdToCharacter)
            .map(new Func1<Object, Boolean>() {
                @Override
                public Boolean call(Object o) {
                    return true;
                }
            });
    }

    @NonNull
    private Map<String, Object> assignCharacters(List<CharacterViewModel> characterViewModels, List<PlayerDataModel> playerDataModels, int goodCharacters, int badCharacters, GameRulesDataModel gameRulesDataModel) {
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
        return mapPlayerIdToCharacter;
    }

    private Map<String, Object> assignCharactersAprilFools(List<PlayerDataModel> playerDataModels) {
        final List<String> unassignedCharacters = new ArrayList<>(playerDataModels.size());
        for (int i = 0; i < playerDataModels.size(); i++) {
            unassignedCharacters.add(i % 2 == 0 ? "Servant" : "Oberon");
        }
        final Random rand = new Random();
        final Map<String, Object> mapPlayerIdToCharacter = new HashMap<>(playerDataModels.size());
        for (PlayerDataModel playerDataModel : playerDataModels) {
            final int index = rand.nextInt(unassignedCharacters.size());
            mapPlayerIdToCharacter.put(playerDataModel.id, unassignedCharacters.get(index));
            unassignedCharacters.remove(index);
        }
        return mapPlayerIdToCharacter;
    }
}
