package edu.tcu.cs.tcusportsbroadcasting.crewmember.exception;

public class CrewMemberNotFoundException extends RuntimeException {
    public CrewMemberNotFoundException(Long id) {
        super("Could not find user with id " + id);
    }
}
