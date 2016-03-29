package com.keithsmyth.resistance.feature.lobby.presentation;

import com.keithsmyth.resistance.Injector;
import com.keithsmyth.resistance.RxUtil;
import com.keithsmyth.data.model.ModelActionWrapper;
import com.keithsmyth.data.model.PlayerDataModel;
import com.keithsmyth.data.provider.GameInfoProvider;
import com.keithsmyth.resistance.feature.lobby.domain.AddPlayerUseCase;
import com.keithsmyth.resistance.feature.lobby.domain.SelectCharactersUseCase;
import com.keithsmyth.resistance.feature.lobby.domain.StartGameUseCase;
import com.keithsmyth.resistance.feature.lobby.domain.WatchLobbyStateUseCase;
import com.keithsmyth.resistance.feature.lobby.model.CharacterViewModel;
import com.keithsmyth.resistance.navigation.GenericDisplayThrowable;
import com.keithsmyth.resistance.navigation.Navigation;
import com.keithsmyth.resistance.presentation.Presenter;
import com.keithsmyth.resistance.presentation.PresenterFactory;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import rx.Single;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class LobbyPresenter implements Presenter<LobbyView> {

    private final Navigation navigation;
    private final AddPlayerUseCase addPlayerUseCase;
    private final SelectCharactersUseCase selectCharactersUseCase;
    private final WatchLobbyStateUseCase watchLobbyStateUseCase;
    private final StartGameUseCase startGameUseCase;
    private final GameInfoProvider gameInfoProvider;

    private final List<PlayerDataModel> playerDataModels;
    private final List<CharacterViewModel> characterViewModels;
    private final Set<CharacterViewModel> selectedCharacterSet;

    private LobbyView lobbyView;
    private Subscription addPlayerSubscription;
    private Subscription startGameSubscription;

    private LobbyPresenter(Navigation navigation, AddPlayerUseCase addPlayerUseCase, SelectCharactersUseCase selectCharactersUseCase, WatchLobbyStateUseCase watchLobbyStateUseCase, StartGameUseCase startGameUseCase, GameInfoProvider gameInfoProvider) {
        this.navigation = navigation;
        this.addPlayerUseCase = addPlayerUseCase;
        this.selectCharactersUseCase = selectCharactersUseCase;
        this.watchLobbyStateUseCase = watchLobbyStateUseCase;
        this.startGameUseCase = startGameUseCase;
        this.gameInfoProvider = gameInfoProvider;
        playerDataModels = new ArrayList<>();
        characterViewModels = new ArrayList<>();
        selectedCharacterSet = new HashSet<>();
    }

    @Override
    public void attachView(final LobbyView lobbyView) {
        this.lobbyView = lobbyView;

        this.lobbyView.setTitle(gameInfoProvider.getCurrentGameId());

        // run add player use case
        if (playerDataModels.isEmpty()) {
            RxUtil.unsubscribe(addPlayerSubscription);
            addPlayerSubscription = addPlayerUseCase.execute()
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
        RxUtil.unsubscribe(addPlayerSubscription);
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
        RxUtil.unsubscribe(startGameSubscription);
        startGameSubscription = selectCharactersUseCase.execute(selectedCharacters, playerDataModels)
            .flatMap(new Func1<Boolean, Single<?>>() {
                @Override
                public Single<?> call(Boolean success) {
                    return success ? startGameUseCase.execute() : Single.just(null);
                }
            })
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Action1<Object>() {
                @Override
                public void call(Object o) {
                    RxUtil.unsubscribe(startGameSubscription);
                }
            }, new Action1<Throwable>() {
                @Override
                public void call(Throwable throwable) {
                    RxUtil.unsubscribe(startGameSubscription);
                    navigation.showError(new GenericDisplayThrowable(throwable));
                }
            });
    }

    private void onPlayerAdded(PlayerDataModel playerDataModel) {
        playerDataModels.add(playerDataModel);
        if (lobbyView != null) {
            lobbyView.addPlayer(playerDataModel);
        }
        if (playerDataModel.isMe && playerDataModel.isOwner) {
            characterViewModels.clear();
            characterViewModels.addAll(selectCharactersUseCase.getCharacters());
            if (lobbyView != null) {
                lobbyView.showCharacters(characterViewModels);
            }
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
            return new LobbyPresenter(Injector.navigation(), Injector.addPlayerUseCase(), Injector.selectCharactersUseCase(), Injector.watchLobbyStateUseCase(), Injector.startGameUseCase(), Injector.gameInfoProvider());
        }
    };
}
