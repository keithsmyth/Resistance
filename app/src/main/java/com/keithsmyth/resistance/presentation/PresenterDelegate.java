package com.keithsmyth.resistance.presentation;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;

public class PresenterDelegate<V, P extends Presenter<V>> {

    private static final int LOADER_ID = 101;

    public P presenter;

    public PresenterDelegate(LoaderManager loaderManager, Context context, PresenterFactory<P> presenterFactory) {
        loaderManager.initLoader(LOADER_ID, null, new PresenterLoaderCallbacks(context, presenterFactory));
    }

    public void onResume(V view) {
        presenter.attachView(view);
    }

    public void onPause() {
        presenter.detachView();
    }

    private class PresenterLoaderCallbacks implements LoaderManager.LoaderCallbacks<P> {

        private final Context context;
        private final PresenterFactory<P> presenterFactory;

        public PresenterLoaderCallbacks(Context context, PresenterFactory<P> presenterFactory) {
            this.context = context;
            this.presenterFactory = presenterFactory;
        }

        @Override
        public Loader<P> onCreateLoader(int id, Bundle args) {
            return new PresenterLoader<>(context, presenterFactory);
        }

        @Override
        public void onLoadFinished(Loader<P> loader, P data) {
            presenter = data;
        }

        @Override
        public void onLoaderReset(Loader<P> loader) {
            presenter = null;
        }
    }
}
