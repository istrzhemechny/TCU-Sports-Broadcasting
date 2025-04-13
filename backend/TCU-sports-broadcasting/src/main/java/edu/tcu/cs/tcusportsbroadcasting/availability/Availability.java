package edu.tcu.cs.tcusportsbroadcasting.availability;

import edu.tcu.cs.tcusportsbroadcasting.crewmember.CrewMember;
import edu.tcu.cs.tcusportsbroadcasting.gameschedule.Game;
import jakarta.persistence.*;

@Entity
public class Availability {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private CrewMember crewMember;

    @ManyToOne
    @JoinColumn(name = "game_id", nullable = false)
    private Game game;

    private int availability; // 1 = available, 0 = not available

    private String comment;

    // Getters and Setters

    public Long getId() {
        return id;
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

    public int getAvailability() {
        return availability;
    }

    public void setAvailability(int availability) {
        this.availability = availability;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
