package edu.tcu.cs.tcusportsbroadcasting.crewschedule.dto;

import java.time.LocalDate;
import java.util.List;

public class CrewListResponseDto {

    private Long gameId;
    private String gameStart; // Not implemented yet, placeholder
    private LocalDate gameDate;
    private String venue;
    private String opponent;
    private List<CrewScheduleDto> crewedMembers;

    public CrewListResponseDto(Long gameId, String gameStart, LocalDate gameDate, String venue, String opponent, List<CrewScheduleDto> crewedMembers) {
        this.gameId = gameId;
        this.gameStart = gameStart;
        this.gameDate = gameDate;
        this.venue = venue;
        this.opponent = opponent;
        this.crewedMembers = crewedMembers;
    }

    public Long getGameId() {
        return gameId;
    }

    public String getGameStart() {
        return gameStart;
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

    public List<CrewScheduleDto> getCrewedMembers() {
        return crewedMembers;
    }
}
