package edu.tcu.cs.tcusportsbroadcasting.availability.exception;

public class AvailabilityAlreadyExistsException extends RuntimeException {
    public AvailabilityAlreadyExistsException(Long userId, Long gameId) {
        super("Availability already exists for user " + userId + " and game " + gameId);
    }
}
