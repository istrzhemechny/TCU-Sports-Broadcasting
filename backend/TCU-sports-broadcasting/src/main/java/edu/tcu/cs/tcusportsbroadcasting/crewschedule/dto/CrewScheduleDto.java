package edu.tcu.cs.tcusportsbroadcasting.crewschedule.dto;

import jakarta.validation.constraints.NotNull;

public class CrewScheduleDto {

    private Long crewedUserId;

    @NotNull(message = "UserId is required")
    private Long userId;

    private Long gameId;

    @NotNull(message = "Position is required")
    private String position;

    private String fullName;

    private String reportTime;

    private String reportLocation;

    // Getters and Setters

    public Long getCrewedUserId() {
        return crewedUserId;
    }

    public void setCrewedUserId(Long crewedUserId) {
        this.crewedUserId = crewedUserId;
    }

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

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getReportTime() {
        return reportTime;
    }

    public void setReportTime(String reportTime) {
        this.reportTime = reportTime;
    }

    public String getReportLocation() {
        return reportLocation;
    }

    public void setReportLocation(String reportLocation) {
        this.reportLocation = reportLocation;
    }
}
