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
import java.util.List;
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
        AvailabilityDto dto = new AvailabilityDto();
        dto.setUserId(1L);
        dto.setGameId(1L);
        dto.setAvailability(1);
        dto.setComment("Coming");

        CrewMember member = new CrewMember();
        member.setId(1L);

        Game game = new Game();
        game.setGameId(1L);
        game.setScheduleId(10L);
        game.setGameDate(LocalDate.now());

        when(availabilityRepository.findByCrewMemberIdAndGame_GameId(1L, 1L)).thenReturn(Optional.empty());
        when(crewMemberRepository.findById(1L)).thenReturn(Optional.of(member));
        when(gameRepository.findById(1L)).thenReturn(Optional.of(game));

        AvailabilityResponseDto result = availabilityService.addAvailability(dto);

        assertThat(result.getUserId()).isEqualTo(1L);
        assertThat(result.getGameId()).isEqualTo(1L);
        assertThat(result.getAvailability()).isEqualTo(1);
        assertThat(result.getScheduleId()).isEqualTo(10L);
    }

    @Test
    void shouldReturnAvailabilityListByUserId() {
        CrewMember member = new CrewMember();
        member.setId(1L);

        Game game = new Game();
        game.setGameId(1L);
        game.setScheduleId(22L);

        Availability a = new Availability();
        a.setCrewMember(member);
        a.setGame(game);
        a.setAvailability(1);
        a.setComment("Available");

        when(crewMemberRepository.findById(1L)).thenReturn(Optional.of(member));
        when(availabilityRepository.findByCrewMember_Id(1L)).thenReturn(List.of(a));

        List<AvailabilityResponseDto> result = availabilityService.findByUserId(1L);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getScheduleId()).isEqualTo(22L);
    }

    @Test
    void shouldThrowIfAvailabilityExists() {
        AvailabilityDto dto = new AvailabilityDto();
        dto.setUserId(1L);
        dto.setGameId(1L);

        when(availabilityRepository.findByCrewMemberIdAndGame_GameId(1L, 1L))
                .thenReturn(Optional.of(new Availability()));

        assertThrows(AvailabilityAlreadyExistsException.class, () -> {
            availabilityService.addAvailability(dto);
        });
    }
}
