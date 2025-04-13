package edu.tcu.cs.tcusportsbroadcasting.availability.dto;

public class AvailabilityResponseDto {

    private Long userId;
    private Long gameId;
    private int availability;
    private String comment;

    public AvailabilityResponseDto(Long userId, Long gameId, int availability, String comment) {
        this.userId = userId;
        this.gameId = gameId;
        this.availability = availability;
        this.comment = comment;
    }

    public Long getUserId() {
        return userId;
    }

    public Long getGameId() {
        return gameId;
    }

    public int getAvailability() {
        return availability;
    }

    public String getComment() {
        return comment;
    }
}
