package edu.tcu.cs.tcusportsbroadcasting.gameschedule;

import edu.tcu.cs.tcusportsbroadcasting.gameschedule.dto.GameResponseDto;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class GameService {

    private final GameRepository gameRepository;

    public GameService(GameRepository gameRepository) {
        this.gameRepository = gameRepository;
    }

    public List<GameResponseDto> findAllGames() {
        return gameRepository.findAll().stream()
                .map(game -> new GameResponseDto(
                        game.getGameId(),
                        game.getScheduleId(),
                        game.getGameDate(),
                        game.getVenue(),
                        game.getOpponent(),
                        game.isFinalized()
                ))
                .collect(Collectors.toList());
    }
}
