package com.keithsmyth.resistance.lobby.domain;

import com.keithsmyth.resistance.data.CharacterProvider;
import com.keithsmyth.resistance.data.GameRulesProvider;
import com.keithsmyth.resistance.data.model.CharacterDataModel;
import com.keithsmyth.resistance.data.model.GameRulesDataModel;
import com.keithsmyth.resistance.lobby.exception.NumberPlayersException;
import com.keithsmyth.resistance.lobby.exception.NumberCharactersException;
import com.keithsmyth.resistance.lobby.mapper.CharacterMapper;
import com.keithsmyth.resistance.lobby.model.CharacterViewModel;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Subscriber;

import static com.keithsmyth.resistance.data.GameRulesProvider.MAX_PLAYERS;
import static com.keithsmyth.resistance.data.GameRulesProvider.MIN_PLAYERS;

public class SelectCharactersUseCase {

    private final CharacterProvider characterProvider;
    private final GameRulesProvider gameRulesProvider;
    private final CharacterMapper characterMapper;

    public SelectCharactersUseCase(CharacterProvider characterProvider, GameRulesProvider gameRulesProvider) {
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

    public Observable<Integer> selectCharacters(List<CharacterViewModel> characterViewModels, int numberOfPlayers) {

        // TODO: lock down lobby

        // verify number of players
        if (numberOfPlayers < MIN_PLAYERS || numberOfPlayers > MAX_PLAYERS) {
            final NumberPlayersException numberPlayersException = new NumberPlayersException(MIN_PLAYERS, MAX_PLAYERS, numberOfPlayers);
            return createExceptionObservable(numberPlayersException);
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
            final NumberCharactersException numberCharactersException = new NumberCharactersException(gameRulesDataModel.goodPlayers, goodCharacters, gameRulesDataModel.badPlayers, badCharacters);
            return createExceptionObservable(numberCharactersException);
        }

        // TODO: assign characters to players

        // save
        return Observable.from(new Integer[] {1});
    }

    private Observable<Integer> createExceptionObservable(final Throwable throwable) {
        return Observable.create(new Observable.OnSubscribe<Integer>() {
            @Override
            public void call(Subscriber<? super Integer> subscriber) {
                // TODO: unlock lobby
                subscriber.onError(throwable);
            }
        });
    }
}
