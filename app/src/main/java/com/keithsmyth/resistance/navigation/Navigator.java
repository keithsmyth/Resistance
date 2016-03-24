package com.keithsmyth.resistance.navigation;

public interface Navigator {

    void showError(DisplayThrowable displayThrowable);

    void openWelcome();

    void openLobby();

    void openGame();

    void openEnd();
}
