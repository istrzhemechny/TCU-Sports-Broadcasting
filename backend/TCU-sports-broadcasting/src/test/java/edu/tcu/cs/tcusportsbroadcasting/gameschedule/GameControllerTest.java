package edu.tcu.cs.tcusportsbroadcasting.gameschedule;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.tcu.cs.tcusportsbroadcasting.gameschedule.dto.GameResponseDto;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(GameController.class)
class GameControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private GameService gameService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldReturnAllGamesSuccessfully() throws Exception {
        // Arrange
        GameResponseDto g1 = new GameResponseDto(
                1L, 1L, LocalDate.of(2024, 9, 7), "Carter", "LIU", false
        );

        GameResponseDto g2 = new GameResponseDto(
                2L, 1L, LocalDate.of(2024, 9, 14), "Carter", "UCF", false
        );

        Mockito.when(gameService.findAllGames()).thenReturn(List.of(g1, g2));

        // Act & Assert
        mockMvc.perform(get("/game/gameSchedule/games")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.message").value("Find Success"))
                .andExpect(jsonPath("$.data[0].opponent").value("LIU"))
                .andExpect(jsonPath("$.data[1].gameDate").value("2024-09-14"));
    }
}
