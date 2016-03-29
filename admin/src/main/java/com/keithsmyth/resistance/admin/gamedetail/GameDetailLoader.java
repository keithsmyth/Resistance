package com.keithsmyth.resistance.admin.gamedetail;

import android.content.Context;
import android.support.v4.content.Loader;

import com.keithsmyth.resistance.admin.Injector;

public class GameDetailLoader extends Loader<GameDetailPresenter> {

    private GameDetailPresenter presenter;

    public GameDetailLoader(Context context) {
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
        presenter = new GameDetailPresenter(Injector.gameProvider(), Injector.gameInfoProvider());
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
