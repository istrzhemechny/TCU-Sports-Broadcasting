package edu.tcu.cs.tcusportsbroadcasting.availability;

import edu.tcu.cs.tcusportsbroadcasting.availability.dto.AvailabilityDto;
import edu.tcu.cs.tcusportsbroadcasting.availability.dto.AvailabilityResponseDto;
import edu.tcu.cs.tcusportsbroadcasting.availability.exception.AvailabilityAlreadyExistsException;
import edu.tcu.cs.tcusportsbroadcasting.crewmember.CrewMember;
import edu.tcu.cs.tcusportsbroadcasting.crewmember.CrewMemberRepository;
import edu.tcu.cs.tcusportsbroadcasting.gameschedule.Game;
import edu.tcu.cs.tcusportsbroadcasting.gameschedule.GameRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class AvailabilityServiceTest {

    private AvailabilityRepository availabilityRepository;
    private CrewMemberRepository crewMemberRepository;
    private GameRepository gameRepository;
    private AvailabilityService availabilityService;

    @BeforeEach
    void setUp() {
        availabilityRepository = mock(AvailabilityRepository.class);
        crewMemberRepository = mock(CrewMemberRepository.class);
        gameRepository = mock(GameRepository.class);
        availabilityService = new AvailabilityService(availabilityRepository, crewMemberRepository, gameRepository);
    }

    @Test
    void shouldAddAvailabilitySuccessfully() {
        // Arrange
        AvailabilityDto dto = new AvailabilityDto();
        dto.setUserId(1L);
        dto.setGameId(1L);
        dto.setAvailability(1);
        dto.setComment("Coming from another game");

        CrewMember member = new CrewMember();
        member.setId(1L);

        Game game = new Game();
        game.setGameId(1L);
        game.setGameDate(LocalDate.now());

        when(availabilityRepository.findByCrewMemberIdAndGame_GameId(1L, 1L)).thenReturn(Optional.empty());
        when(crewMemberRepository.findById(1L)).thenReturn(Optional.of(member));
        when(gameRepository.findById(1L)).thenReturn(Optional.of(game));

        // Act
        AvailabilityResponseDto result = availabilityService.addAvailability(dto);

        // Assert
        assertThat(result.getUserId()).isEqualTo(1L);
        assertThat(result.getGameId()).isEqualTo(1L);
        assertThat(result.getAvailability()).isEqualTo(1);
        assertThat(result.getComment()).isEqualTo("Coming from another game");

        verify(availabilityRepository).save(any(Availability.class));
    }

    @Test
    void shouldThrowIfAvailabilityAlreadyExists() {
        AvailabilityDto dto = new AvailabilityDto();
        dto.setUserId(1L);
        dto.setGameId(1L);

        when(availabilityRepository.findByCrewMemberIdAndGame_GameId(1L, 1L)).thenReturn(Optional.of(new Availability()));

        assertThrows(AvailabilityAlreadyExistsException.class, () -> {
            availabilityService.addAvailability(dto);
        });
    }
}
