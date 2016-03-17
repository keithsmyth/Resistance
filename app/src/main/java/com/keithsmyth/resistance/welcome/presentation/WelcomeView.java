package com.keithsmyth.resistance.welcome.presentation;

public interface WelcomeView {

    void setName(String name);

    String getNameInput();

    String getGameIdInput();

    void setLoadingState(boolean isLoading);

    void showError(String msg);

    void showNameError();

    void showGameIdError();

    void clearErrors();

    void openLobby(int gameId);
}
