package edu.tcu.cs.tcusportsbroadcasting.crewmember;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.tcu.cs.tcusportsbroadcasting.crewmember.dto.CrewMemberDto;
import edu.tcu.cs.tcusportsbroadcasting.crewmember.dto.CrewMemberResponseDto;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CrewMemberController.class)
class CrewMemberControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CrewMemberService crewMemberService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldAddCrewMemberSuccessfully() throws Exception {
        // Arrange input
        CrewMemberDto dto = new CrewMemberDto();
        dto.setFirstName("Jane");
        dto.setLastName("Doe");
        dto.setEmail("jane.doe@example.com");
        dto.setPhoneNumber("1234567890");
        dto.setPassword("Secret123");
        dto.setRole("ADMIN");
        dto.setPosition(Arrays.asList("Director", "Producer"));

        // Arrange output
        CrewMemberResponseDto responseDto = new CrewMemberResponseDto(
                1L,
                "Jane",
                "Doe",
                "jane.doe@example.com",
                "1234567890",
                "ADMIN",
                Arrays.asList("Director", "Producer")
        );

        Mockito.when(crewMemberService.addCrewMember(Mockito.any(), Mockito.any()))
                .thenReturn(responseDto);

        // Act + Assert
        mockMvc.perform(post("/User/crewMember?token=test-token")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.message").value("Add Success"))
                .andExpect(jsonPath("$.data.email").value("jane.doe@example.com"));
    }

    @Test
    void shouldReturnCrewMemberByIdSuccessfully() throws Exception {
        // Arrange
        Long id = 1L;
        CrewMemberResponseDto responseDto = new CrewMemberResponseDto(
                id,
                "John",
                "Doe",
                "john.doe@example.com",
                "1234567890",
                "ADMIN",
                Arrays.asList("DIRECTOR", "PRODUCER")
        );

        Mockito.when(crewMemberService.findById(id)).thenReturn(responseDto);

        // Act + Assert
        mockMvc.perform(get("/User/crewMember/{userId}", id)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.message").value("Find Success"))
                .andExpect(jsonPath("$.data.email").value("john.doe@example.com"));
    }

}
