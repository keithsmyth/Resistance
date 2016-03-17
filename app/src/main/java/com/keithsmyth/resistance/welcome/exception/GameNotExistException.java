package com.keithsmyth.resistance.welcome.exception;

public class GameNotExistException extends Exception {

    public GameNotExistException() {
        super("Game does not exist");
    }

}
