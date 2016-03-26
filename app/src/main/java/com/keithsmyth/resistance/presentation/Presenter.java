package com.keithsmyth.resistance.presentation;

public interface Presenter<V> {

    void attachView(V view);

    void detachView();

    void onDestroyed();
}
