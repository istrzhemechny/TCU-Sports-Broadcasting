package edu.tcu.cs.tcusportsbroadcasting.gameschedule;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.tcu.cs.tcusportsbroadcasting.gameschedule.dto.GameScheduleDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class GameScheduleControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldAddGameScheduleAndReturnSuccess() throws Exception {
        GameScheduleDto dto = new GameScheduleDto();
        dto.setSport("Soccer");
        dto.setSeason("2025-2026");

        mockMvc.perform(post("/schedule/gameSchedule")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.message").value("Add Success"))
                .andExpect(jsonPath("$.data.sport").value("Soccer"))
                .andExpect(jsonPath("$.data.season").value("2025-2026"));
    }

    @Test
    void shouldReturn400WhenGameScheduleIsInvalid() throws Exception {
        GameScheduleDto dto = new GameScheduleDto();
        dto.setSport(""); // Invalid: blank
        dto.setSeason(""); // Invalid: blank

        mockMvc.perform(post("/schedule/gameSchedule")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.flag").value(false))
                .andExpect(jsonPath("$.code").value(400))
                .andExpect(jsonPath("$.message").value("Provided arguments are invalid, see data for details."))
                .andExpect(jsonPath("$.data.sport").value("Sport is required"))
                .andExpect(jsonPath("$.data.season").value("Season is required"));
    }

}
