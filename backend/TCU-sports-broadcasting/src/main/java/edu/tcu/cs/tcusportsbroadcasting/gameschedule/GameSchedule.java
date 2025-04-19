package edu.tcu.cs.tcusportsbroadcasting.gameschedule;

import jakarta.persistence.*;
import java.io.Serializable;

@Entity
public class GameSchedule implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String sport;

    @Column(nullable = false)
    private String season;

    public GameSchedule() {}

    public GameSchedule(String sport, String season) {
        this.sport = sport;
        this.season = season;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

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
