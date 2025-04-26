package edu.tcu.cs.tcusportsbroadcasting.availability;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.tcu.cs.tcusportsbroadcasting.availability.dto.AvailabilityDto;
import edu.tcu.cs.tcusportsbroadcasting.availability.dto.AvailabilityResponseDto;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AvailabilityController.class)
class AvailabilityControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AvailabilityService availabilityService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldReturnAvailabilityResponseWhenAdded() throws Exception {
        AvailabilityDto dto = new AvailabilityDto();
        dto.setUserId(1L);
        dto.setGameId(2L);
        dto.setAvailability(1);
        dto.setComment("Coming");

        AvailabilityResponseDto response = new AvailabilityResponseDto(1L, 20L, 2L, 1, "Coming");

        Mockito.when(availabilityService.addAvailability(Mockito.any())).thenReturn(response);

        mockMvc.perform(post("/availability/availability")
                        .with(jwt().jwt(jwt -> jwt.claim("authorities", "ROLE_ADMIN")))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.userId").value(1))
                .andExpect(jsonPath("$.data.gameId").value(2))
                .andExpect(jsonPath("$.data.scheduleId").value(20))
                .andExpect(jsonPath("$.data.comment").value("Coming"));
    }

    @Test
    void shouldReturnAvailabilityListByUserId() throws Exception {
        AvailabilityResponseDto dto = new AvailabilityResponseDto(1L, 11L, 101L, 1, "Available");

        Mockito.when(availabilityService.findByUserId(1L)).thenReturn(List.of(dto));

        mockMvc.perform(get("/availability/availability/1")
                        .with(jwt().jwt(jwt -> jwt.claim("authorities", "ROLE_ADMIN")))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.length()").value(1))
                .andExpect(jsonPath("$.data[0].userId").value(1))
                .andExpect(jsonPath("$.data[0].scheduleId").value(11));
    }
}
