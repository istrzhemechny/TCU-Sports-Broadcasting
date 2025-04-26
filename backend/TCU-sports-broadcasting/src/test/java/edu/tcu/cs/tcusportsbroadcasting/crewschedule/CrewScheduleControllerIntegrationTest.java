package edu.tcu.cs.tcusportsbroadcasting.crewschedule;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.tcu.cs.tcusportsbroadcasting.crewmember.CrewMember;
import edu.tcu.cs.tcusportsbroadcasting.crewmember.CrewMemberRepository;
import edu.tcu.cs.tcusportsbroadcasting.crewschedule.dto.CrewScheduleDto;
import edu.tcu.cs.tcusportsbroadcasting.gameschedule.Game;
import edu.tcu.cs.tcusportsbroadcasting.gameschedule.GameRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import java.time.LocalDate;
import java.util.List;
import static java.util.List.of;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class CrewScheduleControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private GameRepository gameRepository;

    @Autowired
    private CrewMemberRepository crewMemberRepository;

    @Autowired
    private CrewScheduleRepository crewScheduleRepository;

    @Test
    void shouldAddCrewScheduleAndReturnSuccess() throws Exception {
        // -- Setup: Insert a Game with ID = 1
        Game game = new Game();
        game.setGameDate(LocalDate.of(2025, 10, 10));
        game.setVenue("Amon G. Carter Stadium");
        game.setOpponent("Texas Longhorns");
        game.setFinalized(true);
        game = gameRepository.save(game); // save & get generated ID

        // -- Setup: Insert CrewMembers with userId = 1 and 2
        CrewMember cm1 = new CrewMember();
        cm1.setFirstName("Kate");
        cm1.setLastName("Bednarz");
        cm1.setEmail("kate@tcu.edu");
        cm1.setPhoneNumber("1231231234");
        cm1.setPassword("password");
        cm1.setRole("USER");
        cm1.setPosition(List.of("PRODUCER"));
        crewMemberRepository.save(cm1);

        CrewMember cm2 = new CrewMember();
        cm2.setFirstName("Dave");
        cm2.setLastName("Park");
        cm2.setEmail("dave@tcu.edu");
        cm2.setPhoneNumber("1112223333");
        cm2.setPassword("password");
        cm2.setRole("USER");
        cm2.setPosition(List.of("DIRECTOR"));
        crewMemberRepository.save(cm2);

        // -- Arrange DTOs
        CrewScheduleDto dto1 = new CrewScheduleDto();
        dto1.setUserId(cm1.getId());
        dto1.setPosition("PRODUCER");
        dto1.setReportTime("5:30 PM");
        dto1.setReportLocation("Control Room");

        CrewScheduleDto dto2 = new CrewScheduleDto();
        dto2.setUserId(cm2.getId());
        dto2.setPosition("DIRECTOR");
        dto2.setReportTime("5:30 PM");
        dto2.setReportLocation("Control Room");

        // -- Act & Assert
        mockMvc.perform(post("/crewSchedule/crewSchedule/" + game.getGameId())
                        .with(jwt().authorities(new SimpleGrantedAuthority("ROLE_ADMIN")))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(List.of(dto1, dto2))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.data.length()").value(2))
                .andExpect(jsonPath("$.data[0].position").value("PRODUCER"))
                .andExpect(jsonPath("$.data[1].position").value("DIRECTOR"));
    }


    @Test
    void shouldReturn404WhenGameNotFound() throws Exception {
        CrewScheduleDto dto = new CrewScheduleDto();
        dto.setUserId(1L);
        dto.setPosition("GRAPHICS");

        mockMvc.perform(post("/crewSchedule/crewSchedule/999")
                        .with(jwt().authorities(new SimpleGrantedAuthority("ROLE_ADMIN")))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(List.of(dto))))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Could not find game with id 999"));
    }

    @Test
    void shouldReturn400WhenPositionMissing() throws Exception {
        CrewScheduleDto dto = new CrewScheduleDto();
        dto.setUserId(1L); // no position

        mockMvc.perform(post("/crewSchedule/crewSchedule/1")
                        .with(jwt().authorities(new SimpleGrantedAuthority("ROLE_ADMIN")))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(List.of(dto))))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Provided arguments are invalid, see data for details."))
                .andExpect(jsonPath("$.data.position").value("Position is required"));
    }

    @Test
    void shouldReturnCrewListByGameId() throws Exception {
        Game game = new Game();
        game.setGameDate(LocalDate.of(2025, 11, 1));
        game.setVenue("Amon G. Carter Stadium");
        game.setOpponent("Texas Longhorns");
        game.setFinalized(true);
        game = gameRepository.save(game);

        CrewMember cm = new CrewMember();
        cm.setFirstName("John");
        cm.setLastName("Smith");
        cm.setEmail("john@tcu.edu");
        cm.setPhoneNumber("1234567890");
        cm.setPassword("pass");
        cm.setRole("USER");
        cm.setPosition(List.of("DIRECTOR"));
        cm = crewMemberRepository.save(cm);

        CrewSchedule cs = new CrewSchedule();
        cs.setCrewMember(cm);
        cs.setGame(game);
        cs.setPosition("DIRECTOR");
        cs.setReportTime("12:00");
        cs.setReportLocation("CONTROL ROOM");
        crewScheduleRepository.save(cs);

        mockMvc.perform(get("/crewSchedule/crewList/crewList/" + game.getGameId())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data.gameId").value(game.getGameId()))
                .andExpect(jsonPath("$.data.crewedMembers.length()").value(1))
                .andExpect(jsonPath("$.data.crewedMembers[0].position").value("DIRECTOR"));
    }

}
