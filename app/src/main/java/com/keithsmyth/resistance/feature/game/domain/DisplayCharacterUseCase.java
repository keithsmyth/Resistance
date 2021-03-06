package com.keithsmyth.resistance.feature.game.domain;

import com.keithsmyth.data.provider.CharacterProvider;
import com.keithsmyth.data.provider.GameInfoProvider;
import com.keithsmyth.data.provider.UserProvider;
import com.keithsmyth.data.model.GameInfoDataModel;
import com.keithsmyth.data.model.CharacterDataModel;
import com.keithsmyth.resistance.DateUtil;
import com.keithsmyth.resistance.feature.game.model.PlayerCharacterViewModel;
import com.keithsmyth.resistance.feature.lobby.model.CharacterViewModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import rx.Single;
import rx.functions.Func1;

public class DisplayCharacterUseCase {

    private final UserProvider userProvider;
    private final GameInfoProvider gameInfoProvider;
    private final CharacterProvider characterProvider;

    public DisplayCharacterUseCase(UserProvider userProvider, GameInfoProvider gameInfoProvider, CharacterProvider characterProvider) {
        this.userProvider = userProvider;
        this.gameInfoProvider = gameInfoProvider;
        this.characterProvider = characterProvider;
    }

    public Single<PlayerCharacterViewModel> execute() {
        return gameInfoProvider.getGameInfo()
            .map(new Func1<GameInfoDataModel, PlayerCharacterViewModel>() {
                @Override
                public PlayerCharacterViewModel call(GameInfoDataModel gameInfoDataModel) {
                    return mapToViewModel(gameInfoDataModel);
                }
            });
    }

    private PlayerCharacterViewModel mapToViewModel(GameInfoDataModel gameInfoDataModel) {
        final String userId = userProvider.getId();
        final String name = userProvider.getName();
        final CharacterDataModel userCharacter = characterProvider.getCharacter(gameInfoDataModel.getMapPlayerIdToCharacter().get(userId));

        final List<String> characters = new ArrayList<>();
        final List<String> revealedNames = new ArrayList<>();
        for (Map.Entry<String, String> entry : gameInfoDataModel.getMapPlayerIdToCharacter().entrySet()) {
            final String characterName = entry.getValue();
            characters.add(characterName);
            final String revealedUserId = entry.getKey();
            if (revealedUserId.equals(userId)) {
                continue;
            }
            if (userCharacter.isRevealed(characterProvider.getCharacter(characterName))) {
                final String revealedName = gameInfoDataModel.getMapPlayerIdToName().get(revealedUserId);
                revealedNames.add(revealedName);
            }
        }
        Collections.sort(revealedNames);

        Collections.sort(characters);
        final List<CharacterViewModel> characterViewModels = new ArrayList<>(characters.size());
        for (String characterName : characters) {
            final String givenName = DateUtil.isAprilFools() ? "?" : characterName;
            characterViewModels.add(new CharacterViewModel(givenName, characterProvider.getCharacter(characterName).isBad));
        }

        final List<String> players = new ArrayList<>(gameInfoDataModel.getMapPlayerIdToName().values());
        Collections.sort(players);

        return new PlayerCharacterViewModel(name, userCharacter.name, userCharacter.isBad, revealedNames, userCharacter.revealedDescription(), characterViewModels, players);
    }
}
