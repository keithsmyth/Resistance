package com.keithsmyth.resistance.feature.lobby.presentation;

import com.keithsmyth.resistance.Injector;
import com.keithsmyth.resistance.Presenter;
import com.keithsmyth.resistance.PresenterFactory;
import com.keithsmyth.resistance.RxUtil;
import com.keithsmyth.resistance.data.model.ModelActionWrapper;
import com.keithsmyth.resistance.data.model.PlayerDataModel;
import com.keithsmyth.resistance.feature.lobby.domain.AddPlayerUseCase;
import com.keithsmyth.resistance.feature.lobby.domain.SelectCharactersUseCase;
import com.keithsmyth.resistance.feature.lobby.domain.WatchLobbyStateUseCase;
import com.keithsmyth.resistance.feature.lobby.model.CharacterViewModel;
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
    private final WatchLobbyStateUseCase watchLobbyStateUseCase;

    private final List<PlayerDataModel> playerDataModels;
    private final List<CharacterViewModel> characterViewModels;
    private final Set<CharacterViewModel> selectedCharacterSet;

    private LobbyView lobbyView;
    private Subscription subscription;

    public LobbyPresenter(Navigation navigation, AddPlayerUseCase addPlayerUseCase, SelectCharactersUseCase selectCharactersUseCase, WatchLobbyStateUseCase watchLobbyStateUseCase) {
        this.navigation = navigation;
        this.addPlayerUseCase = addPlayerUseCase;
        this.selectCharactersUseCase = selectCharactersUseCase;
        this.watchLobbyStateUseCase = watchLobbyStateUseCase;
        playerDataModels = new ArrayList<>();
        characterViewModels=  new ArrayList<>();
        selectedCharacterSet = new HashSet<>();
    }

    @Override
    public void attachView(final LobbyView lobbyView) {
        this.lobbyView = lobbyView;
        if (playerDataModels.isEmpty()) {
            RxUtil.unsubscribe(subscription);
            subscription = addPlayerUseCase.execute()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<ModelActionWrapper<PlayerDataModel>>() {
                    @Override
                    public void call(ModelActionWrapper<PlayerDataModel> playerDataModelModelActionWrapper) {
                        if (playerDataModelModelActionWrapper.isAdded) {
                            onPlayerAdded(playerDataModelModelActionWrapper.dataModel);
                        } else {
                            onPlayerRemoved(playerDataModelModelActionWrapper.dataModel);
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        navigation.showError(new GenericDisplayThrowable(throwable));
                    }
                });
        } else {
            lobbyView.setPlayers(playerDataModels);
            if (!characterViewModels.isEmpty()) {
                lobbyView.showCharacters(characterViewModels);
            }
        }

        // watch the game state
        watchLobbyStateUseCase.execute();
    }

    @Override
    public void detachView() {
        // stop watching the game state
        watchLobbyStateUseCase.destroy();
        lobbyView = null;
    }

    @Override
    public void onDestroyed() {
        detachView();
        RxUtil.unsubscribe(subscription);
        addPlayerUseCase.destroy();
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
        selectCharactersUseCase.selectCharacters(selectedCharacters, playerDataModels);
    }

    private void onPlayerAdded(PlayerDataModel playerDataModel) {
        playerDataModels.add(playerDataModel);
        if (lobbyView == null) {
            return;
        }
        lobbyView.addPlayer(playerDataModel);
        if (playerDataModel.isMe) {
            characterViewModels.clear();
            characterViewModels.addAll(selectCharactersUseCase.getCharacters());
            lobbyView.showCharacters(characterViewModels);
        }
    }

    private void onPlayerRemoved(PlayerDataModel playerDataModel) {
        playerDataModels.remove(playerDataModel);
        if (lobbyView != null) {
            lobbyView.removePlayer(playerDataModel);
        }

    }

    public static final PresenterFactory<LobbyPresenter> FACTORY = new PresenterFactory<LobbyPresenter>() {
        @Override
        public LobbyPresenter create() {
            return new LobbyPresenter(Injector.navigation(), Injector.addPlayerUseCase(), Injector.selectCharactersUseCase(), Injector.watchLobbyStateUseCase());
        }
    };
}
