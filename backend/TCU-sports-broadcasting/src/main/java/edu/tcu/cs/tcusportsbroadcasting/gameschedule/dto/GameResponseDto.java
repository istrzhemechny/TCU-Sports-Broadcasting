package edu.tcu.cs.tcusportsbroadcasting.gameschedule.dto;

import java.time.LocalDate;

public class GameResponseDto {

    private Long gameId;
    private Long scheduleId;
    private LocalDate gameDate;
    private String venue;
    private String opponent;
    private boolean isFinalized;

    public GameResponseDto(Long gameId, Long scheduleId, LocalDate gameDate, String venue, String opponent, boolean isFinalized) {
        this.gameId = gameId;
        this.scheduleId = scheduleId;
        this.gameDate = gameDate;
        this.venue = venue;
        this.opponent = opponent;
        this.isFinalized = isFinalized;
    }

    // Getters

    public Long getGameId() {
        return gameId;
    }

    public Long getScheduleId() {
        return scheduleId;
    }

    public LocalDate getGameDate() {
        return gameDate;
    }

    public String getVenue() {
        return venue;
    }

    public String getOpponent() {
        return opponent;
    }

    public boolean isFinalized() {
        return isFinalized;
    }
}