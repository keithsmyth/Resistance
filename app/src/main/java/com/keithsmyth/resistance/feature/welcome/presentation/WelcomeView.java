package com.keithsmyth.resistance.feature.welcome.presentation;

import com.keithsmyth.resistance.navigation.ErrorView;

interface WelcomeView extends ErrorView {

    void setName(String name);

    String getNameInput();

    void setGame(int gameId);

    String getGameIdInput();

    void setLoadingState(boolean isLoading);

    void showNameError();

    void showGameIdError();

    void clearErrors();
}
