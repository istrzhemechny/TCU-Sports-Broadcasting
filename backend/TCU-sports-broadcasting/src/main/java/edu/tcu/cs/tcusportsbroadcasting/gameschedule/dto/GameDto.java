package edu.tcu.cs.tcusportsbroadcasting.gameschedule.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public class GameDto {

    @NotNull(message = "Game date is required")
    private LocalDate gameDate;

    @NotBlank(message = "Venue is required")
    private String venue;

    @NotBlank(message = "Opponent is required")
    private String opponent;

    @NotNull(message = "Is finalized is required")
    private Boolean isFinalized;

    // Getters and Setters
    public LocalDate getGameDate() {
        return gameDate;
    }

    public void setGameDate(LocalDate gameDate) {
        this.gameDate = gameDate;
    }

    public String getVenue() {
        return venue;
    }

    public void setVenue(String venue) {
        this.venue = venue;
    }

    public String getOpponent() {
        return opponent;
    }

    public void setOpponent(String opponent) {
        this.opponent = opponent;
    }

    public Boolean getIsFinalized() {
        return isFinalized;
    }

    public void setIsFinalized(Boolean isFinalized) {
        this.isFinalized = isFinalized;
    }
}
