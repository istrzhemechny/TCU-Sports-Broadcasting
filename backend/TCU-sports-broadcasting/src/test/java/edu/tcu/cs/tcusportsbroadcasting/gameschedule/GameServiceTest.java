package edu.tcu.cs.tcusportsbroadcasting.gameschedule;

import edu.tcu.cs.tcusportsbroadcasting.gameschedule.dto.GameDto;
import edu.tcu.cs.tcusportsbroadcasting.gameschedule.dto.GameResponseDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class GameServiceTest {

    private GameRepository gameRepository;
    private GameService gameService;
    private GameScheduleRepository gameScheduleRepository;

    @BeforeEach
    void setUp() {
        gameRepository = mock(GameRepository.class);
        gameScheduleRepository = mock(GameScheduleRepository.class);
        gameService = new GameService(gameRepository, gameScheduleRepository);
    }

    @Test
    void shouldReturnAllGames() {
        // Arrange
        Game g1 = new Game();
        g1.setGameId(1L);
        g1.setScheduleId(1L);
        g1.setGameDate(LocalDate.of(2024, 9, 7));
        g1.setVenue("Carter");
        g1.setOpponent("LIU");
        g1.setFinalized(false);

        Game g2 = new Game();
        g2.setGameId(2L);
        g2.setScheduleId(1L);
        g2.setGameDate(LocalDate.of(2024, 9, 14));
        g2.setVenue("Carter");
        g2.setOpponent("UCF");
        g2.setFinalized(false);

        when(gameRepository.findAll()).thenReturn(Arrays.asList(g1, g2));

        // Act
        List<GameResponseDto> result = gameService.findAllGames();

        // Assert
        assertThat(result).hasSize(2);
        assertThat(result.get(0).getOpponent()).isEqualTo("LIU");
        assertThat(result.get(1).getVenue()).isEqualTo("Carter");
    }

    @Test
    void shouldAddGameToScheduleSuccessfully() {
        GameSchedule schedule = new GameSchedule("Basketball", "2024-2025");
        schedule.setId(1L);

        GameDto dto = new GameDto();
        dto.setGameDate(LocalDate.of(2025, 10, 10));
        dto.setVenue("Amon G. Carter Stadium");
        dto.setOpponent("Texas Longhorns");
        dto.setIsFinalized(true);

        Game saved = new Game();
        saved.setGameId(5L);
        saved.setScheduleId(1L);
        saved.setGameDate(dto.getGameDate());
        saved.setVenue(dto.getVenue());
        saved.setOpponent(dto.getOpponent());
        saved.setFinalized(true);

        when(gameScheduleRepository.findById(1L)).thenReturn(Optional.of(schedule));
        when(gameRepository.save(any(Game.class))).thenReturn(saved);

        GameResponseDto result = gameService.addGameToSchedule(1L, dto);

        assertThat(result.getScheduleId()).isEqualTo(1L);
        assertThat(result.getOpponent()).isEqualTo("Texas Longhorns");
    }

}
