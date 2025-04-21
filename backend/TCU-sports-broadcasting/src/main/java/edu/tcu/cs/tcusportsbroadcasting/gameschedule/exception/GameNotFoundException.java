package edu.tcu.cs.tcusportsbroadcasting.gameschedule.exception;

public class GameNotFoundException extends RuntimeException {
    public GameNotFoundException(Long gameId) {
        super("Could not find game with id " + gameId);
    }
}
