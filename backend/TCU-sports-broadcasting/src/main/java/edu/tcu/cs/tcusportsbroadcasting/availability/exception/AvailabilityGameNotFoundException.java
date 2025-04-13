package edu.tcu.cs.tcusportsbroadcasting.availability.exception;

public class AvailabilityGameNotFoundException extends RuntimeException {
    public AvailabilityGameNotFoundException(Long gameId) {
        super("Could not find game with id " + gameId);
    }
}
