package com.keithsmyth.resistance.feature.game.presentation;

import com.keithsmyth.resistance.Injector;
import com.keithsmyth.resistance.RxUtil;
import com.keithsmyth.resistance.feature.game.domain.GameStateUseCase;
import com.keithsmyth.resistance.feature.game.model.GamePlayViewModel;
import com.keithsmyth.resistance.feature.game.model.GameRoundViewModel;
import com.keithsmyth.resistance.navigation.GenericDisplayThrowable;
import com.keithsmyth.resistance.navigation.Navigation;
import com.keithsmyth.resistance.presentation.Presenter;
import com.keithsmyth.resistance.presentation.PresenterFactory;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class GamePresenter implements Presenter<GameView> {

    private final Navigation navigation;
    private final GameStateUseCase gameStateUseCase;

    private GameView gameView;
    private Subscription subscription;
    private GamePlayViewModel gamePlayViewModel;

    private GamePresenter(Navigation navigation, GameStateUseCase gameStateUseCase) {
        this.navigation = navigation;
        this.gameStateUseCase = gameStateUseCase;
    }

    @Override
    public void attachView(GameView view) {
        gameView = view;
        watchData();
    }

    @Override
    public void detachView() {
        gameView = null;
    }

    @Override
    public void onDestroyed() {
        RxUtil.unsubscribe(subscription);
        detachView();
    }

    private void watchData() {
        if (subscription == null) {
            subscription = gameStateUseCase.execute()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Action1<GamePlayViewModel>() {
                        @Override
                        public void call(GamePlayViewModel gamePlayViewModel) {
                            GamePresenter.this.gamePlayViewModel = gamePlayViewModel;
                            displayCurrentRound(gamePlayViewModel);
                        }
                    }, new Action1<Throwable>() {
                        @Override
                        public void call(Throwable throwable) {
                            RxUtil.unsubscribe(subscription);
                            navigation.showError(new GenericDisplayThrowable(throwable));
                        }
                    });
        } else if (gamePlayViewModel != null) {
            displayCurrentRound(gamePlayViewModel);
        }
    }

    private void displayCurrentRound(GamePlayViewModel gamePlayViewModel) {
        final GameRoundViewModel currentRound = gamePlayViewModel.getCurrentRound();
        gameView.setRound(currentRound.roundNumber);
        gameView.setCaptain(currentRound.captainName);
    }

    public static final PresenterFactory<GamePresenter> FACTORY = new PresenterFactory<GamePresenter>() {
        @Override
        public GamePresenter create() {
            return new GamePresenter(Injector.navigation(), Injector.gameStateUseCase());
        }
    };
}
