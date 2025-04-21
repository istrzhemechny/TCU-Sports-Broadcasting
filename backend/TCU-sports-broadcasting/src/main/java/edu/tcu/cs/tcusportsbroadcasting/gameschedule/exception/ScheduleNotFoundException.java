package edu.tcu.cs.tcusportsbroadcasting.gameschedule.exception;

public class ScheduleNotFoundException extends RuntimeException {
    public ScheduleNotFoundException(Long id) {
        super("Could not find schedule with id " + id);
    }
}
