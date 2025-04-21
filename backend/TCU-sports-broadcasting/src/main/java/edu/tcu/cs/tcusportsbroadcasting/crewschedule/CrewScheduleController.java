package edu.tcu.cs.tcusportsbroadcasting.crewschedule;

import edu.tcu.cs.tcusportsbroadcasting.crewschedule.dto.CrewScheduleDto;
import edu.tcu.cs.tcusportsbroadcasting.common.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/crewSchedule")
public class CrewScheduleController {

    private final CrewScheduleService crewScheduleService;

    public CrewScheduleController(CrewScheduleService crewScheduleService) {
        this.crewScheduleService = crewScheduleService;
    }

    @PostMapping("/crewSchedule/{gameId}")
    public ResponseEntity<ApiResponse> addCrewSchedule(@PathVariable Long gameId,
                                                       @RequestBody @Valid List<CrewScheduleDto> dtoList) {
        List<CrewScheduleDto> result = crewScheduleService.addCrewSchedule(gameId, dtoList);
        ApiResponse response = new ApiResponse(true, 200, "Add Success", result);
        return ResponseEntity.ok(response);
    }
}
