package com.keithsmyth.resistance.presentation;

import android.content.Context;
import android.support.v4.content.Loader;

public class PresenterLoader<T extends Presenter> extends Loader<T> {

    private final PresenterFactory<T> presenterFactory;
    private T cachedItem;

    public PresenterLoader(Context context, PresenterFactory<T> presenterFactory) {
        super(context);
        this.presenterFactory = presenterFactory;
    }

    @Override
    protected void onStartLoading() {
        if (cachedItem != null) {
            deliverResult(cachedItem);
            return;
        }
        forceLoad();
    }

    @Override
    protected void onForceLoad() {
        cachedItem = presenterFactory.create();
        deliverResult(cachedItem);
    }

    @Override
    protected void onReset() {
        if (cachedItem != null) {
            cachedItem.onDestroyed();
            cachedItem = null;
        }
    }
}
