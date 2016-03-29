package com.keithsmyth.resistance.admin.gamedetail;

import com.keithsmyth.data.model.GameInfoDataModel;
import com.keithsmyth.data.provider.GameInfoProvider;
import com.keithsmyth.data.provider.GameProvider;
import com.keithsmyth.resistance.admin.RxUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class GameDetailPresenter {

    private final PlayerViewModel[] newPlayers = new PlayerViewModel[]{
        new PlayerViewModel("a", "alpha", null),
        new PlayerViewModel("b", "beta", null),
        new PlayerViewModel("c", "charlie", null),
        new PlayerViewModel("d", "delta", null),
        new PlayerViewModel("e", "echo", null),
        new PlayerViewModel("f", "foxtrot", null),
        new PlayerViewModel("g", "golf", null),
        new PlayerViewModel("h", "hotel", null),
        new PlayerViewModel("i", "india", null),
        new PlayerViewModel("j", "juliett", null),
    };

    private final GameProvider gameProvider;
    private final GameInfoProvider gameInfoProvider;

    private GameDetailView gameDetailView;
    private Subscription gameInfoSubscription;
    private Subscription addPlayerSubscription;
    private Subscription deleteGameSubscription;
    private GameInfoDataModel gameInfoDataModel;

    public GameDetailPresenter(GameProvider gameProvider, GameInfoProvider gameInfoProvider) {
        this.gameProvider = gameProvider;
        this.gameInfoProvider = gameInfoProvider;
    }

    public void onAttach(GameDetailView gameDetailView) {
        this.gameDetailView = gameDetailView;
        this.gameDetailView.setGameNumber(gameInfoProvider.getCurrentGameId());
        watchData();
    }

    public void onDetach() {
        gameDetailView = null;
    }

    public void onDestroy() {
        RxUtil.unsubscribe(deleteGameSubscription);
        RxUtil.unsubscribe(addPlayerSubscription);
        RxUtil.unsubscribe(gameInfoSubscription);
        onDetach();
    }

    public void addNewPlayer() {
        final Map<String, String> mapPlayerIdToName = gameInfoDataModel.getMapPlayerIdToName();
        for (PlayerViewModel newPlayer : newPlayers) {
            if (!mapPlayerIdToName.containsKey(newPlayer.id)) {
                addPlayerSubscription = gameInfoProvider.addPlayerToGame(newPlayer.id, newPlayer.name)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Action1<Object>() {
                        @Override
                        public void call(Object o) {
                            RxUtil.unsubscribe(addPlayerSubscription);
                        }
                    }, new Action1<Throwable>() {
                        @Override
                        public void call(Throwable throwable) {
                            RxUtil.unsubscribe(addPlayerSubscription);
                            if (gameDetailView != null) {
                                gameDetailView.showError(throwable.getMessage());
                            }
                        }
                    });
                return;
            }
        }
        if (gameDetailView != null) {
            gameDetailView.showError("No players left");
        }
    }

    public void deleteGame() {
        RxUtil.unsubscribe(deleteGameSubscription);
        deleteGameSubscription = gameProvider.removeGame()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Action1<Object>() {
                @Override
                public void call(Object o) {
                    RxUtil.unsubscribe(deleteGameSubscription);
                    if (gameDetailView != null) {
                        gameDetailView.navigateToGameList();
                    }
                }
            }, new Action1<Throwable>() {
                @Override
                public void call(Throwable throwable) {
                    RxUtil.unsubscribe(deleteGameSubscription);
                    if (gameDetailView != null) {
                        gameDetailView.showError(throwable.getMessage());
                    }
                }
            });
    }

    private void watchData() {
        if (gameInfoSubscription == null) {
            gameInfoSubscription = gameInfoProvider.watchGameInfo()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<GameInfoDataModel>() {
                    @Override
                    public void call(GameInfoDataModel gameInfoDataModel) {
                        onDataReturned(gameInfoDataModel);
                        populateView();
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        if (gameDetailView != null) {
                            gameDetailView.showError(throwable.getMessage());
                        }
                    }
                });
        } else if (gameInfoDataModel != null) {
            populateView();
        }
    }

    private void onDataReturned(GameInfoDataModel gameInfoDataModel) {
        this.gameInfoDataModel = gameInfoDataModel;
    }

    private void populateView() {
        if (gameInfoDataModel == null) {
            return;
        }
        final Map<String, String> mapPlayerIdToName = gameInfoDataModel.getMapPlayerIdToName();
        gameDetailView.setGameNumber(gameInfoProvider.getCurrentGameId());
        gameDetailView.setGameOwner(mapPlayerIdToName.get(gameInfoDataModel.getOwnerId()));
        gameDetailView.setStatus(getStatusText(gameInfoDataModel.getStatus()));
        gameDetailView.setPlayers(getPlayers(gameInfoDataModel));
    }

    private String getStatusText(@GameInfoProvider.GameState int status) {
        switch (status) {
            case GameInfoProvider.STATE_FINISHED:
                return "Finished";
            case GameInfoProvider.STATE_NEW:
                return "New";
            case GameInfoProvider.STATE_NONE:
                return "None";
            case GameInfoProvider.STATE_STARTED:
                return "Started";
            case GameInfoProvider.STATE_STARTING:
                return "Starting";
            default:
                return "Illegal Status " + status;
        }
    }

    private List<PlayerViewModel> getPlayers(GameInfoDataModel gameInfoDataModel) {
        final Map<String, String> mapPlayerIdToName = gameInfoDataModel.getMapPlayerIdToName();
        final List<PlayerViewModel> playerViewModels = new ArrayList<>(mapPlayerIdToName.size());
        for (Map.Entry<String, String> entry : mapPlayerIdToName.entrySet()) {
            final Map<String, String> mapPlayerIdToCharacter = gameInfoDataModel.getMapPlayerIdToCharacter();
            playerViewModels.add(
                new PlayerViewModel(entry.getKey(),
                    entry.getValue(),
                    mapPlayerIdToCharacter != null ? mapPlayerIdToCharacter.get(entry.getKey()) : null));
        }
        return playerViewModels;
    }
}
