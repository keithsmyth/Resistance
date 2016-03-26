package com.keithsmyth.resistance.feature.game.presentation;

import com.keithsmyth.resistance.Injector;
import com.keithsmyth.resistance.presentation.Presenter;
import com.keithsmyth.resistance.presentation.PresenterFactory;
import com.keithsmyth.resistance.RxUtil;
import com.keithsmyth.resistance.data.GameInfoProvider;
import com.keithsmyth.resistance.feature.game.domain.DisplayCharacterUseCase;
import com.keithsmyth.resistance.feature.game.model.PlayerCharacterViewModel;
import com.keithsmyth.resistance.navigation.GenericDisplayThrowable;
import com.keithsmyth.resistance.navigation.Navigation;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

public class CharacterPresenter implements Presenter<CharacterView> {

    private final Navigation navigation;
    private final DisplayCharacterUseCase displayCharacterUseCase;
    private final GameInfoProvider gameInfoProvider;

    private CharacterView characterView;
    private Subscription playerCharactersSubscription;
    private PlayerCharacterViewModel playerCharacterViewModel;

    private CharacterPresenter(Navigation navigation, DisplayCharacterUseCase displayCharacterUseCase, GameInfoProvider gameInfoProvider) {
        this.navigation = navigation;
        this.displayCharacterUseCase = displayCharacterUseCase;
        this.gameInfoProvider = gameInfoProvider;
    }

    @Override
    public void attachView(CharacterView characterView) {
        this.characterView = characterView;

        // set game #
        this.characterView.showGameId(gameInfoProvider.getCurrentGameId());

        if (playerCharacterViewModel == null) {
            getPlayerCharacters();
        } else {
            if (characterView != null) {
                characterView.displayPlayerCharacterInfo(playerCharacterViewModel);
            }
        }
    }

    @Override
    public void detachView() {
        characterView = null;
    }

    @Override
    public void onDestroyed() {
        RxUtil.unsubscribe(playerCharactersSubscription);
        detachView();
    }

    private void getPlayerCharacters() {
        RxUtil.unsubscribe(playerCharactersSubscription);
        playerCharactersSubscription = displayCharacterUseCase.execute()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Action1<PlayerCharacterViewModel>() {
                @Override
                public void call(PlayerCharacterViewModel playerCharacterViewModel) {
                    onPlayerCharactersReturned(playerCharacterViewModel);
                }
            }, new Action1<Throwable>() {
                @Override
                public void call(Throwable throwable) {
                    navigation.showError(new GenericDisplayThrowable(throwable));
                }
            });
    }

    private void onPlayerCharactersReturned(PlayerCharacterViewModel playerCharacterViewModel) {
        this.playerCharacterViewModel = playerCharacterViewModel;
        if (characterView != null) {
            characterView.displayPlayerCharacterInfo(playerCharacterViewModel);
        }
    }

    public static final PresenterFactory<CharacterPresenter> FACTORY = new PresenterFactory<CharacterPresenter>() {
        @Override
        public CharacterPresenter create() {
            return new CharacterPresenter(Injector.navigation(), Injector.displayCharacterUseCase(), Injector.gameInfoProvider());
        }
    };
}
