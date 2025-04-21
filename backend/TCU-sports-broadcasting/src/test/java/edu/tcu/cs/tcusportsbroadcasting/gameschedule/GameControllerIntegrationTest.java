package edu.tcu.cs.tcusportsbroadcasting.gameschedule;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.tcu.cs.tcusportsbroadcasting.availability.AvailabilityRepository;
import edu.tcu.cs.tcusportsbroadcasting.crewschedule.CrewScheduleRepository;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;


@SpringBootTest
@AutoConfigureMockMvc
class GameControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private GameRepository gameRepository;

    @Autowired
    private AvailabilityRepository availabilityRepository;

    @Autowired
    private CrewScheduleRepository crewScheduleRepository;

    @Autowired
    private GameScheduleRepository gameScheduleRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        crewScheduleRepository.deleteAll();
        availabilityRepository.deleteAll();
        gameRepository.deleteAll();

        Game g1 = new Game();
        g1.setScheduleId(1L);
        g1.setGameDate(LocalDate.of(2024, 9, 7));
        g1.setVenue("Carter");
        g1.setOpponent("LIU");
        g1.setFinalized(false);

        Game g2 = new Game();
        g2.setScheduleId(1L);
        g2.setGameDate(LocalDate.of(2024, 9, 14));
        g2.setVenue("Carter");
        g2.setOpponent("UCF");
        g2.setFinalized(false);

        gameRepository.save(g1);
        gameRepository.save(g2);
    }

    @Test
    void shouldReturnAllGamesFromDatabase() throws Exception {
        mockMvc.perform(get("/game/gameSchedule/games")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.message").value("Find Success"))
                .andExpect(jsonPath("$.data.length()").value(2))
                .andExpect(jsonPath("$.data[0].opponent").value("LIU"));
    }

    @Test
    void shouldAddGameToScheduleSuccessfully() throws Exception {
        GameSchedule schedule = new GameSchedule("Basketball", "2024-2025");
        schedule = gameScheduleRepository.save(schedule); // must exist

        String requestJson = """
    {
      "gameDate": "2025-10-10",
      "venue": "Amon G. Carter Stadium",
      "opponent": "Texas Longhorns",
      "isFinalized": true
    }
    """;

        mockMvc.perform(post("/game/schedule/gameSchedule/" + schedule.getId() + "/games")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data.venue").value("Amon G. Carter Stadium"))
                .andExpect(jsonPath("$.data.scheduleId").value(schedule.getId()));
    }

}
