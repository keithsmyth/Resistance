package com.keithsmyth.resistance.lobby.presentation;

import com.keithsmyth.resistance.Injector;
import com.keithsmyth.resistance.Presenter;
import com.keithsmyth.resistance.PresenterFactory;
import com.keithsmyth.resistance.RxUtil;
import com.keithsmyth.resistance.lobby.domain.AddPlayerUseCase;
import com.keithsmyth.resistance.lobby.domain.SelectCharactersUseCase;
import com.keithsmyth.resistance.lobby.exception.NumberPlayersException;
import com.keithsmyth.resistance.lobby.exception.NumberCharactersException;
import com.keithsmyth.resistance.lobby.model.CharacterViewModel;
import com.keithsmyth.resistance.lobby.model.PlayerViewModel;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

public class LobbyPresenter implements Presenter<LobbyView> {

    private final AddPlayerUseCase addPlayerUseCase;
    private final SelectCharactersUseCase selectCharactersUseCase;
    private final Set<CharacterViewModel> selectedCharacterSet;

    private LobbyView lobbyView;
    private Subscription subscription;

    public LobbyPresenter(AddPlayerUseCase addPlayerUseCase, SelectCharactersUseCase selectCharactersUseCase) {
        this.addPlayerUseCase = addPlayerUseCase;
        this.selectCharactersUseCase = selectCharactersUseCase;
        selectedCharacterSet = new HashSet<>();
    }

    @Override
    public void attachView(final LobbyView lobbyView) {
        this.lobbyView = lobbyView;
        subscription = addPlayerUseCase.execute()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Action1<PlayerViewModel>() {
                @Override
                public void call(PlayerViewModel playerViewModel) {
                    onPlayerAdded(playerViewModel);
                }
            }, new Action1<Throwable>() {
                @Override
                public void call(Throwable throwable) {
                    lobbyView.showError(throwable.getMessage());
                }
            });
    }

    @Override
    public void detachView() {
        RxUtil.unsubscribe(subscription);
        lobbyView = null;
    }

    @Override
    public void onDestroyed() {
        detachView();
    }

    public boolean isCharacterSelected(CharacterViewModel characterViewModel) {
        return selectedCharacterSet.contains(characterViewModel);
    }

    public void selectCharacter(CharacterViewModel characterViewModel, boolean isSelected) {
        if (isSelected) {
            selectedCharacterSet.add(characterViewModel);
        } else {
            selectedCharacterSet.remove(characterViewModel);
        }
    }

    public void startGame(final int numberOfPlayers) {
        final List<CharacterViewModel> selectedCharacters = new ArrayList<>(selectedCharacterSet);
        RxUtil.unsubscribe(subscription);
        subscription = selectCharactersUseCase.selectCharacters(selectedCharacters, numberOfPlayers)
            .subscribeOn(AndroidSchedulers.mainThread())
            .subscribe(new Action1<Integer>() {
                @Override
                public void call(Integer gameId) {
                    lobbyView.openGameRoom(gameId);
                }
            }, new Action1<Throwable>() {
                @Override
                public void call(Throwable throwable) {
                    if (throwable instanceof NumberPlayersException) {
                        lobbyView.showNumberPlayersError((NumberPlayersException) throwable);
                    } else if (throwable instanceof NumberCharactersException) {
                        lobbyView.showSelectCharactersError((NumberCharactersException) throwable);
                    } else {
                        lobbyView.showError(throwable.getMessage());
                    }
                }
            });
    }

    private void onPlayerAdded(PlayerViewModel playerViewModel) {
        lobbyView.addPlayer(playerViewModel);
        if (playerViewModel.isMe) {
            lobbyView.showCharacters(selectCharactersUseCase.getCharacters());
        }
    }

    public static final PresenterFactory<LobbyPresenter> FACTORY = new PresenterFactory<LobbyPresenter>() {
        @Override
        public LobbyPresenter create() {
            return new LobbyPresenter(Injector.addPlayerUseCase(), Injector.selectCharactersUseCase());
        }
    };
}
