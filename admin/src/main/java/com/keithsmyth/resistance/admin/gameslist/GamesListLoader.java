package com.keithsmyth.resistance.admin.gameslist;

import android.content.Context;
import android.support.v4.content.Loader;

import com.keithsmyth.resistance.admin.Injector;

public class GamesListLoader extends Loader<GamesListPresenter> {

    private GamesListPresenter presenter;

    public GamesListLoader(Context context) {
        super(context);
    }

    @Override
    protected void onStartLoading() {
        if (presenter != null) {
            deliverResult(presenter);
            return;
        }
        forceLoad();
    }

    @Override
    protected void onForceLoad() {
        presenter = new GamesListPresenter(Injector.gameInfoProvider());
        deliverResult(presenter);
    }

    @Override
    protected void onReset() {
        if (presenter != null) {
            presenter.onDestroy();
            presenter = null;
        }
    }
}
