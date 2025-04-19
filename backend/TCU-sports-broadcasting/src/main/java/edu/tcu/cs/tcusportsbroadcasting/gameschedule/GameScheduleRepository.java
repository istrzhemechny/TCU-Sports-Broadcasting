package edu.tcu.cs.tcusportsbroadcasting.gameschedule;

import org.springframework.data.jpa.repository.JpaRepository;

public interface GameScheduleRepository extends JpaRepository<GameSchedule, Long> {
}
