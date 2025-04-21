package edu.tcu.cs.tcusportsbroadcasting.crewschedule;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CrewScheduleRepository extends JpaRepository<CrewSchedule, Long> {
    List<CrewSchedule> findByGame_GameId(Long gameId);
}
