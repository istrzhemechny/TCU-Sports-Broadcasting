package edu.tcu.cs.tcusportsbroadcasting.availability.exception;

public class AvailabilityUserNotFoundException extends RuntimeException {
    public AvailabilityUserNotFoundException(Long userId) {
        super("Could not find user with id " + userId);
    }
}
