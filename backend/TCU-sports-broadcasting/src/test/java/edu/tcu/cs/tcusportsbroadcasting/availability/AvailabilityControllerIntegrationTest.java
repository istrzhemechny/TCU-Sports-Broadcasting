package edu.tcu.cs.tcusportsbroadcasting.availability;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.tcu.cs.tcusportsbroadcasting.availability.dto.AvailabilityDto;
import edu.tcu.cs.tcusportsbroadcasting.crewmember.CrewMember;
import edu.tcu.cs.tcusportsbroadcasting.crewmember.CrewMemberRepository;
import edu.tcu.cs.tcusportsbroadcasting.gameschedule.Game;
import edu.tcu.cs.tcusportsbroadcasting.gameschedule.GameRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class AvailabilityControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CrewMemberRepository crewMemberRepository;

    @Autowired
    private GameRepository gameRepository;

    @Autowired
    private AvailabilityRepository availabilityRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private Long userId;
    private Long gameId;
    private Long scheduleId;

    @BeforeEach
    void setUp() {
        availabilityRepository.deleteAll();
        crewMemberRepository.deleteAll();
        gameRepository.deleteAll();

        CrewMember cm = new CrewMember();
        cm.setFirstName("Test");
        cm.setLastName("User");
        cm.setEmail("test@example.com");
        cm.setPhoneNumber("9999999999");
        cm.setPassword("pass");
        cm.setRole("USER");
        cm.setPosition(List.of("EVS"));
        cm = crewMemberRepository.save(cm);
        userId = cm.getId();

        Game game = new Game();
        game.setScheduleId(10L);
        game.setGameDate(LocalDate.now());
        game.setVenue("TCU");
        game.setOpponent("Baylor");
        game.setFinalized(false);
        game = gameRepository.save(game);
        gameId = game.getGameId();
        scheduleId = game.getScheduleId();
    }

    @Test
    void shouldCreateAvailability() throws Exception {
        AvailabilityDto dto = new AvailabilityDto();
        dto.setUserId(userId);
        dto.setGameId(gameId);
        dto.setAvailability(1);
        dto.setComment("Ready");

        mockMvc.perform(post("/availability/availability")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.userId").value(userId))
                .andExpect(jsonPath("$.data.gameId").value(gameId))
                .andExpect(jsonPath("$.data.scheduleId").value(scheduleId));
    }

    @Test
    void shouldReturnAvailabilityListByUserId() throws Exception {
        Availability a = new Availability();
        a.setCrewMember(crewMemberRepository.findById(userId).orElseThrow());
        a.setGame(gameRepository.findById(gameId).orElseThrow());
        a.setAvailability(1);
        a.setComment("I'm good");

        availabilityRepository.save(a);

        mockMvc.perform(get("/availability/availability/" + userId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.data.length()").value(1))
                .andExpect(jsonPath("$.data[0].userId").value(userId))
                .andExpect(jsonPath("$.data[0].gameId").value(gameId));
    }
}
