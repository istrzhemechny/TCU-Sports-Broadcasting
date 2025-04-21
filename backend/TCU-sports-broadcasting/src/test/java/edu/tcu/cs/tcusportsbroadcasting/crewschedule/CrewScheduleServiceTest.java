package edu.tcu.cs.tcusportsbroadcasting.crewschedule;

import edu.tcu.cs.tcusportsbroadcasting.crewmember.CrewMember;
import edu.tcu.cs.tcusportsbroadcasting.crewmember.CrewMemberRepository;
import edu.tcu.cs.tcusportsbroadcasting.crewschedule.dto.CrewScheduleDto;
import edu.tcu.cs.tcusportsbroadcasting.gameschedule.Game;
import edu.tcu.cs.tcusportsbroadcasting.gameschedule.GameRepository;
import edu.tcu.cs.tcusportsbroadcasting.gameschedule.exception.GameNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CrewScheduleServiceTest {

    private CrewScheduleRepository crewScheduleRepository;
    private CrewMemberRepository crewMemberRepository;
    private GameRepository gameRepository;
    private CrewScheduleService crewScheduleService;

    @BeforeEach
    void setUp() {
        this.crewScheduleRepository = mock(CrewScheduleRepository.class);
        this.crewMemberRepository = mock(CrewMemberRepository.class);
        this.gameRepository = mock(GameRepository.class);
        this.crewScheduleService = new CrewScheduleService(crewScheduleRepository, crewMemberRepository, gameRepository);
    }

    @Test
    void shouldAddCrewScheduleSuccessfully() {
        Game game = new Game();
        game.setGameId(1L);

        CrewMember member = new CrewMember();
        member.setId(1L);
        member.setFirstName("John");
        member.setLastName("Doe");

        CrewScheduleDto dto = new CrewScheduleDto();
        dto.setUserId(1L);
        dto.setPosition("PRODUCER");
        dto.setReportTime("5:00 PM");
        dto.setReportLocation("Control Room");

        when(gameRepository.findById(1L)).thenReturn(Optional.of(game));
        when(crewMemberRepository.findById(1L)).thenReturn(Optional.of(member));
        when(crewScheduleRepository.save(any(CrewSchedule.class))).thenAnswer(invocation -> {
            CrewSchedule cs = invocation.getArgument(0);
            cs.setCrewedUserId(10L);
            return cs;
        });

        List<CrewScheduleDto> result = crewScheduleService.addCrewSchedule(1L, List.of(dto));

        assertEquals(1, result.size());
        CrewScheduleDto saved = result.get(0);
        assertEquals("John Doe", saved.getFullName());
        assertEquals("PRODUCER", saved.getPosition());
        assertEquals("Control Room", saved.getReportLocation());
        assertEquals("5:00 PM", saved.getReportTime());
        assertEquals(10L, saved.getCrewedUserId());
    }

    @Test
    void shouldThrowExceptionWhenGameNotFound() {
        when(gameRepository.findById(99L)).thenReturn(Optional.empty());

        CrewScheduleDto dto = new CrewScheduleDto();
        dto.setUserId(1L);
        dto.setPosition("GRAPHICS");

        assertThrows(GameNotFoundException.class, () -> {
            crewScheduleService.addCrewSchedule(99L, List.of(dto));
        });
    }
}
