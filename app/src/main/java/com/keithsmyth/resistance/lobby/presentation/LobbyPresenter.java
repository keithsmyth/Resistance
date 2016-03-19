package com.keithsmyth.resistance.lobby.presentation;

import com.keithsmyth.resistance.Injector;
import com.keithsmyth.resistance.Presenter;
import com.keithsmyth.resistance.PresenterFactory;
import com.keithsmyth.resistance.RxUtil;
import com.keithsmyth.resistance.lobby.domain.AddPlayerUseCase;
import com.keithsmyth.resistance.lobby.domain.SelectCharactersUseCase;
import com.keithsmyth.resistance.lobby.model.CharacterViewModel;
import com.keithsmyth.resistance.lobby.model.PlayerViewModel;
import com.keithsmyth.resistance.navigation.GenericDisplayThrowable;
import com.keithsmyth.resistance.navigation.Navigation;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

public class LobbyPresenter implements Presenter<LobbyView> {

    private final Navigation navigation;
    private final AddPlayerUseCase addPlayerUseCase;
    private final SelectCharactersUseCase selectCharactersUseCase;

    private final List<PlayerViewModel> playerViewModels;
    private final List<CharacterViewModel> characterViewModels;
    private final Set<CharacterViewModel> selectedCharacterSet;

    private LobbyView lobbyView;
    private Subscription subscription;

    public LobbyPresenter(Navigation navigation, AddPlayerUseCase addPlayerUseCase, SelectCharactersUseCase selectCharactersUseCase) {
        this.navigation = navigation;
        this.addPlayerUseCase = addPlayerUseCase;
        this.selectCharactersUseCase = selectCharactersUseCase;
        playerViewModels = new ArrayList<>();
        characterViewModels=  new ArrayList<>();
        selectedCharacterSet = new HashSet<>();
    }

    @Override
    public void attachView(final LobbyView lobbyView) {
        this.lobbyView = lobbyView;
        if (playerViewModels.isEmpty()) {
            RxUtil.unsubscribe(subscription);
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
                        navigation.showError(new GenericDisplayThrowable(throwable));
                    }
                });
        } else {
            lobbyView.setPlayers(playerViewModels);
            if (!characterViewModels.isEmpty()) {
                lobbyView.showCharacters(characterViewModels);
            }
        }
    }

    @Override
    public void detachView() {
        lobbyView = null;
    }

    @Override
    public void onDestroyed() {
        detachView();
        RxUtil.unsubscribe(subscription);
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

    public void startGame() {
        final List<CharacterViewModel> selectedCharacters = new ArrayList<>(selectedCharacterSet);
        selectCharactersUseCase.selectCharacters(selectedCharacters, playerViewModels);
    }

    private void onPlayerAdded(PlayerViewModel playerViewModel) {
        playerViewModels.add(playerViewModel);
        if (lobbyView == null) {
            return;
        }
        lobbyView.addPlayer(playerViewModel);
        if (playerViewModel.isMe) {
            characterViewModels.clear();
            characterViewModels.addAll(selectCharactersUseCase.getCharacters());
            lobbyView.showCharacters(characterViewModels);
        }
    }

    public static final PresenterFactory<LobbyPresenter> FACTORY = new PresenterFactory<LobbyPresenter>() {
        @Override
        public LobbyPresenter create() {
            return new LobbyPresenter(Injector.navigation(), Injector.addPlayerUseCase(), Injector.selectCharactersUseCase());
        }
    };
}
