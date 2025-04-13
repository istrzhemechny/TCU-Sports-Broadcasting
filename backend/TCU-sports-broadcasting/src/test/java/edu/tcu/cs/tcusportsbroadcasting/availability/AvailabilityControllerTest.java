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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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
    void shouldReturnAvailabilityResponseWhenAddedSuccessfully() throws Exception {
        AvailabilityDto dto = new AvailabilityDto();
        dto.setUserId(1L);
        dto.setGameId(1L);
        dto.setAvailability(1);
        dto.setComment("All good");

        AvailabilityResponseDto responseDto = new AvailabilityResponseDto(
                1L, 1L, 1, "All good"
        );

        Mockito.when(availabilityService.addAvailability(Mockito.any())).thenReturn(responseDto);

        mockMvc.perform(post("/availability")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.message").value("Add Success"))
                .andExpect(jsonPath("$.data.userId").value(1))
                .andExpect(jsonPath("$.data.availability").value(1))
                .andExpect(jsonPath("$.data.comment").value("All good"));
    }
}
