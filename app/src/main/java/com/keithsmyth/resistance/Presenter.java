package com.keithsmyth.resistance;

public interface Presenter<V> {

    void attachView(V view);

    void detachView();

    void onDestroyed();
}
