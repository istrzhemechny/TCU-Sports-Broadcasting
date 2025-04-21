package edu.tcu.cs.tcusportsbroadcasting.gameschedule;

import edu.tcu.cs.tcusportsbroadcasting.common.ApiResponse;
import edu.tcu.cs.tcusportsbroadcasting.gameschedule.dto.GameResponseDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import edu.tcu.cs.tcusportsbroadcasting.gameschedule.dto.GameDto;
import edu.tcu.cs.tcusportsbroadcasting.gameschedule.dto.GameResponseDto;
import jakarta.validation.Valid;

import java.util.List;

@RestController
@RequestMapping("/game")
public class GameController {

    private final GameService gameService;

    public GameController(GameService gameService) {
        this.gameService = gameService;
    }

    @GetMapping("/gameSchedule/games")
    public ResponseEntity<ApiResponse> findAllGames() {
        List<GameResponseDto> allGames = gameService.findAllGames();

        ApiResponse response = new ApiResponse(true, 200, "Find Success", allGames);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/schedule/gameSchedule/{scheduleId}/games")
    public ResponseEntity<ApiResponse> addGameToSchedule(@PathVariable Long scheduleId,
                                                         @Valid @RequestBody GameDto dto) {
        GameResponseDto savedGame = gameService.addGameToSchedule(scheduleId, dto);
        ApiResponse response = new ApiResponse(true, 200, "Add Success", savedGame);
        return ResponseEntity.ok(response);
    }
}
