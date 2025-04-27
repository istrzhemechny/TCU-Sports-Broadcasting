package edu.tcu.cs.tcusportsbroadcasting.crewmember.invite;

import jakarta.persistence.*;
import java.time.Instant;
import java.util.UUID;

@Entity
public class InviteToken {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String email;

    private Instant expiration;

    private boolean used = false;

    public InviteToken() {
    }

    public InviteToken(String email, Instant expiration) {
        this.email = email;
        this.expiration = expiration;
        this.used = false;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Instant getExpiration() {
        return expiration;
    }

    public void setExpiration(Instant expiration) {
        this.expiration = expiration;
    }

    public boolean isUsed() {
        return used;
    }

    public void setUsed(boolean used) {
        this.used = used;
    }
}
