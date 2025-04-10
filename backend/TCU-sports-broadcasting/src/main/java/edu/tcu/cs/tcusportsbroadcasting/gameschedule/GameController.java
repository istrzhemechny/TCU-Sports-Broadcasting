package edu.tcu.cs.tcusportsbroadcasting.gameschedule;

import edu.tcu.cs.tcusportsbroadcasting.common.ApiResponse;
import edu.tcu.cs.tcusportsbroadcasting.gameschedule.dto.GameResponseDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/gameSchedule")
public class GameController {

    private final GameService gameService;

    public GameController(GameService gameService) {
        this.gameService = gameService;
    }

    @GetMapping("/games")
    public ResponseEntity<ApiResponse> findAllGames() {
        List<GameResponseDto> allGames = gameService.findAllGames();

        ApiResponse response = new ApiResponse(true, 200, "Find Success", allGames);
        return ResponseEntity.ok(response);
    }
}
