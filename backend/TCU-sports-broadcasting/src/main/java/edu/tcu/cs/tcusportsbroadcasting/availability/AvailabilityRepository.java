package edu.tcu.cs.tcusportsbroadcasting.availability;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AvailabilityRepository extends JpaRepository<Availability, Long> {
    Optional<Availability> findByCrewMemberIdAndGame_GameId(Long userId, Long gameId);

}
