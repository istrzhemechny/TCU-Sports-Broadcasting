package edu.tcu.cs.tcusportsbroadcasting.crewmember;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.tcu.cs.tcusportsbroadcasting.crewmember.dto.CrewMemberDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class CrewMemberControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    CrewMemberRepository crewMemberRepository;

    @Test
    void shouldCreateCrewMemberAndPersistToDatabase() throws Exception {
        // Arrange
        CrewMemberDto dto = new CrewMemberDto();
        dto.setFirstName("Clark");
        dto.setLastName("Kent");
        dto.setEmail("clark.kent@dailyplanet.com");
        dto.setPhoneNumber("5551234567");
        dto.setPassword("superman123");
        dto.setRole("USER");
        dto.setPosition(Arrays.asList("Camera", "EVS"));

        // Act + Assert
        mockMvc.perform(post("/User/crewMember?token=test")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.data.email").value("clark.kent@dailyplanet.com"))
                .andExpect(jsonPath("$.data.position[0]").value("Camera"));
    }

    @Test
    void shouldReturnCrewMemberByIdFromDatabase() throws Exception {
        // Arrange: manually insert a crew member using the repository
        CrewMember cm = new CrewMember();
        cm.setFirstName("Bruce");
        cm.setLastName("Wayne");
        cm.setEmail("bruce@wayneenterprises.com");
        cm.setPhoneNumber("0001234567");
        cm.setPassword("batman");
        cm.setRole("ADMIN");
        cm.setPositions(Arrays.asList("Producer", "EVS"));

        cm = crewMemberRepository.save(cm); // get generated ID

        // Act + Assert
        mockMvc.perform(get("/User/crewMember/{userId}", cm.getId())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.message").value("Find Success"))
                .andExpect(jsonPath("$.data.email").value("bruce@wayneenterprises.com"))
                .andExpect(jsonPath("$.data.position[0]").value("Producer"));
    }

}
