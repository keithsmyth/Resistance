package com.keithsmyth.resistance.admin.gameslist;

import com.keithsmyth.data.model.ModelActionWrapper;
import com.keithsmyth.data.provider.GameInfoProvider;
import com.keithsmyth.resistance.admin.RxUtil;

import java.util.ArrayList;
import java.util.List;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class GamesListPresenter {

    private final GameInfoProvider gameInfoProvider;
    private final List<Integer> activeGames;

    private GamesListView gamesListView;
    private Subscription subscription;

    public GamesListPresenter(GameInfoProvider gameInfoProvider) {
        this.gameInfoProvider = gameInfoProvider;
        activeGames = new ArrayList<>();
    }

    public void onAttach(GamesListView gamesListView) {
        this.gamesListView = gamesListView;
        gameInfoProvider.clearCurrentGameId();
        watchData();
    }

    public void onDetach() {
        gamesListView = null;
    }

    public void onDestroy() {
        RxUtil.unsubscribe(subscription);
        onDetach();
    }

    public void onGameClick(int gameNumber) {
        gameInfoProvider.setCurrentGameId(gameNumber);
        gamesListView.openGameDetailView();
    }

    private void watchData() {
        if (subscription == null) {
            subscription = gameInfoProvider.watchActiveGames()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<ModelActionWrapper<Integer>>() {
                    @Override
                    public void call(ModelActionWrapper<Integer> integerModelActionWrapper) {
                        if (integerModelActionWrapper.isAdded) {
                            addItem(integerModelActionWrapper.dataModel);
                        } else {
                            removeItem(integerModelActionWrapper.dataModel);
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        // TODO: show error
                    }
                });
        } else if (gamesListView != null) {
            gamesListView.setItems(activeGames);
        }
    }

    private void addItem(int item) {
        activeGames.add(0, item);
        if (gamesListView != null) {
            gamesListView.addItem(item);
        }
    }

    private void removeItem(int item) {
        activeGames.remove(activeGames.indexOf(item));
        if (gamesListView != null) {
            gamesListView.removeItem(item);
        }
    }
}
