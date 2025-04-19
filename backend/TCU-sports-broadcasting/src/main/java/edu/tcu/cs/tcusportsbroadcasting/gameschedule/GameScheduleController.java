package edu.tcu.cs.tcusportsbroadcasting.gameschedule;

import edu.tcu.cs.tcusportsbroadcasting.common.ApiResponse;
import edu.tcu.cs.tcusportsbroadcasting.gameschedule.dto.GameScheduleDto;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/schedule")
public class GameScheduleController {

    private final GameScheduleService gameScheduleService;

    public GameScheduleController(GameScheduleService gameScheduleService) {
        this.gameScheduleService = gameScheduleService;
    }

    @PostMapping("/gameSchedule")
    public ResponseEntity<ApiResponse> addSchedule(@Valid @RequestBody GameScheduleDto dto) {
        ApiResponse response = gameScheduleService.addGameSchedule(dto);
        return ResponseEntity.ok(response);
    }
}
