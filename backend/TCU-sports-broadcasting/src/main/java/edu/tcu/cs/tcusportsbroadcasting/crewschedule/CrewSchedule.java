package edu.tcu.cs.tcusportsbroadcasting.crewschedule;

import edu.tcu.cs.tcusportsbroadcasting.crewmember.CrewMember;
import edu.tcu.cs.tcusportsbroadcasting.gameschedule.Game;
import jakarta.persistence.*;

@Entity
public class CrewSchedule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long crewedUserId;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private CrewMember crewMember;

    @ManyToOne
    @JoinColumn(name = "game_id", nullable = false)
    private Game game;

    private String position;

    private String reportTime;

    private String reportLocation;

    // Getters and Setters

    public Long getCrewedUserId() {
        return crewedUserId;
    }

    public void setCrewedUserId(Long crewedUserId) {
        this.crewedUserId = crewedUserId;
    }

    public CrewMember getCrewMember() {
        return crewMember;
    }

    public void setCrewMember(CrewMember crewMember) {
        this.crewMember = crewMember;
    }

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
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
