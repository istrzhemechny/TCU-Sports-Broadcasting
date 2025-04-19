package edu.tcu.cs.tcusportsbroadcasting.gameschedule.dto;

import jakarta.validation.constraints.NotBlank;

public class GameScheduleDto {

    @NotBlank(message = "Sport is required")
    private String sport;

    @NotBlank(message = "Season is required")
    private String season;

    public String getSport() {
        return sport;
    }

    public void setSport(String sport) {
        this.sport = sport;
    }

    public String getSeason() {
        return season;
    }

    public void setSeason(String season) {
        this.season = season;
    }
}
