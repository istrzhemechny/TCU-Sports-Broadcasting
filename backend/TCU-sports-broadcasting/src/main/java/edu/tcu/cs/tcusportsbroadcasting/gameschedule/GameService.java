package edu.tcu.cs.tcusportsbroadcasting.gameschedule;

import edu.tcu.cs.tcusportsbroadcasting.gameschedule.dto.GameResponseDto;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import edu.tcu.cs.tcusportsbroadcasting.gameschedule.dto.GameDto;
import edu.tcu.cs.tcusportsbroadcasting.gameschedule.exception.ScheduleNotFoundException;

@Service
public class GameService {

    private final GameRepository gameRepository;

    private final GameScheduleRepository gameScheduleRepository;

    public GameService(GameRepository gameRepository, GameScheduleRepository gameScheduleRepository) {
        this.gameRepository = gameRepository;
        this.gameScheduleRepository = gameScheduleRepository;
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

    public GameResponseDto addGameToSchedule(Long scheduleId, GameDto dto) {
        GameSchedule schedule = gameScheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new ScheduleNotFoundException(scheduleId));

        Game newGame = new Game();
        newGame.setScheduleId(schedule.getId());
        newGame.setGameDate(dto.getGameDate());
        newGame.setVenue(dto.getVenue());
        newGame.setOpponent(dto.getOpponent());
        newGame.setFinalized(dto.getIsFinalized());

        Game saved = gameRepository.save(newGame);

        return new GameResponseDto(
                saved.getGameId(),
                saved.getScheduleId(),
                saved.getGameDate(),
                saved.getVenue(),
                saved.getOpponent(),
                saved.isFinalized()
        );
    }
}