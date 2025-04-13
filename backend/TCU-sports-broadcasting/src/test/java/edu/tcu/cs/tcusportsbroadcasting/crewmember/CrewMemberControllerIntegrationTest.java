package edu.tcu.cs.tcusportsbroadcasting.crewmember;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.tcu.cs.tcusportsbroadcasting.crewmember.dto.CrewMemberDto;
import edu.tcu.cs.tcusportsbroadcasting.availability.AvailabilityRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

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

    @Autowired
    AvailabilityRepository availabilityRepository;

    @Test
    void shouldCreateCrewMemberAndPersistToDatabase() throws Exception {
        CrewMemberDto dto = new CrewMemberDto();
        dto.setFirstName("Clark");
        dto.setLastName("Kent");
        dto.setEmail("clark.kent@dailyplanet.com");
        dto.setPhoneNumber("5551234567");
        dto.setPassword("superman123");
        dto.setRole("USER");
        dto.setPosition(Arrays.asList("Camera", "EVS"));

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
        CrewMember cm = new CrewMember();
        cm.setFirstName("Bruce");
        cm.setLastName("Wayne");
        cm.setEmail("bruce@wayneenterprises.com");
        cm.setPhoneNumber("0001234567");
        cm.setPassword("batman");
        cm.setRole("ADMIN");
        cm.setPosition(Arrays.asList("Producer", "EVS"));

        cm = crewMemberRepository.save(cm);

        mockMvc.perform(get("/User/crewMember/{userId}", cm.getId())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.message").value("Find Success"))
                .andExpect(jsonPath("$.data.email").value("bruce@wayneenterprises.com"))
                .andExpect(jsonPath("$.data.position[0]").value("Producer"));
    }

    @Test
    void shouldReturnAllCrewMembersFromDatabase() throws Exception {
        // Clear Availability first to avoid FK constraint violation
        availabilityRepository.deleteAll();
        crewMemberRepository.deleteAll();

        CrewMember cm1 = new CrewMember();
        cm1.setFirstName("John");
        cm1.setLastName("Doe");
        cm1.setEmail("john.doe@example.com");
        cm1.setPhoneNumber("1234567890");
        cm1.setPassword("password");
        cm1.setRole("USER");
        cm1.setPosition(List.of("Graphics"));

        CrewMember cm2 = new CrewMember();
        cm2.setFirstName("Jane");
        cm2.setLastName("Smith");
        cm2.setEmail("jane.smith@example.com");
        cm2.setPhoneNumber("1112223333");
        cm2.setPassword("password");
        cm2.setRole("USER");
        cm2.setPosition(List.of("Camera"));

        crewMemberRepository.save(cm1);
        crewMemberRepository.save(cm2);

        mockMvc.perform(get("/User/crewMember")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.message").value("Find Success"))
                .andExpect(jsonPath("$.data.length()").value(2))
                .andExpect(jsonPath("$.data[0].fullName").value("John Doe"));
    }
}
