package edu.tcu.cs.tcusportsbroadcasting.crewmember.invite;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface InviteTokenRepository extends JpaRepository<InviteToken, UUID> {
}