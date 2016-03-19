package com.keithsmyth.resistance.welcome.presentation;

import com.keithsmyth.resistance.navigation.ErrorView;

public interface WelcomeView extends ErrorView {

    void setName(String name);

    String getNameInput();

    void setGame(int gameId);

    String getGameIdInput();

    void setLoadingState(boolean isLoading);

    void showNameError();

    void showGameIdError();

    void clearErrors();
}
