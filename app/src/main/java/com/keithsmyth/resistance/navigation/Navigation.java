package com.keithsmyth.resistance.navigation;

public class Navigation implements Navigator {

    private Navigator navigator;

    public void setNavigator(Navigator navigator) {
        this.navigator = navigator;
    }

    @Override
    public void showError(final DisplayThrowable displayThrowable) {
        if (navigator != null) {
            navigator.showError(displayThrowable);
        }
    }

    @Override
    public void openLobby() {
        if (navigator != null) {
            navigator.openLobby();
        }
    }

    @Override
    public void openGame() {
        if (navigator != null) {
            navigator.openGame();
        }
    }

    @Override
    public void openEnd() {
        if (navigator != null) {
            navigator.openEnd();
        }
    }
}
