package edu.tcu.cs.tcusportsbroadcasting.availability.dto;

import jakarta.validation.constraints.*;

public class AvailabilityDto {

    @NotNull(message = "User id is required")
    private Long userId;

    @NotNull(message = "Game id is required")
    private Long gameId;

    @NotNull(message = "Availability is required")
    @Min(0)
    @Max(1)
    private Integer availability;

    private String comment;

    // Getters and Setters

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getGameId() {
        return gameId;
    }

    public void setGameId(Long gameId) {
        this.gameId = gameId;
    }

    public Integer getAvailability() {
        return availability;
    }

    public void setAvailability(Integer availability) {
        this.availability = availability;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
