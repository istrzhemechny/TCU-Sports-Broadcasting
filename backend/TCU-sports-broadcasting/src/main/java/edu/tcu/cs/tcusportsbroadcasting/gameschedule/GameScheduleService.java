package edu.tcu.cs.tcusportsbroadcasting.gameschedule;

import edu.tcu.cs.tcusportsbroadcasting.gameschedule.dto.GameScheduleDto;
import edu.tcu.cs.tcusportsbroadcasting.common.ApiResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class GameScheduleService {

    private final GameScheduleRepository repository;

    public GameScheduleService(GameScheduleRepository repository) {
        this.repository = repository;
    }

    public ApiResponse addGameSchedule(GameScheduleDto dto) {
        GameSchedule newSchedule = new GameSchedule(dto.getSport(), dto.getSeason());
        GameSchedule saved = repository.save(newSchedule);
        return new ApiResponse(true, 200, "Add Success", saved);
    }
}
