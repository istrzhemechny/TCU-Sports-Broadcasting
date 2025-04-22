package edu.tcu.cs.tcusportsbroadcasting.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.tcu.cs.tcusportsbroadcasting.auth.dto.LoginRequestDto;
import edu.tcu.cs.tcusportsbroadcasting.crewmember.CrewMember;
import edu.tcu.cs.tcusportsbroadcasting.crewmember.CrewMemberRepository;
import edu.tcu.cs.tcusportsbroadcasting.crewschedule.CrewScheduleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class AuthControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CrewMemberRepository crewMemberRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    CrewScheduleRepository crewScheduleRepository;

    @BeforeEach
    void setUp() {
        crewScheduleRepository.deleteAll(); // delete child rows first
        crewMemberRepository.deleteAll();   // then delete parents

        CrewMember cm = new CrewMember();
        cm.setFirstName("John");
        cm.setLastName("Doe");
        cm.setEmail("john.doe@example.com");
        cm.setPhoneNumber("5551234567");
        cm.setPassword("password"); // plaintext for now
        cm.setRole("ADMIN");
        cm.setPosition(List.of("DIRECTOR"));

        crewMemberRepository.save(cm);
    }

    @Test
    void shouldLoginSuccessfully() throws Exception {
        LoginRequestDto login = new LoginRequestDto();
        login.setEmail("john.doe@example.com");
        login.setPassword("password");

        mockMvc.perform(post("/user/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(login)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.message").value("Login Success"))
                .andExpect(jsonPath("$.data.userId").exists())
                .andExpect(jsonPath("$.data.role").value("ADMIN"))
                .andExpect(jsonPath("$.data.token").isNotEmpty());
    }

    @Test
    void shouldFailLoginWithWrongPassword() throws Exception {
        LoginRequestDto login = new LoginRequestDto();
        login.setEmail("john.doe@example.com");
        login.setPassword("wrong");

        mockMvc.perform(post("/user/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(login)))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.flag").value(false))
                .andExpect(jsonPath("$.code").value(401))
                .andExpect(jsonPath("$.message").value("username or password is incorrect"));
    }

    @Test
    void shouldFailLoginWithUnknownEmail() throws Exception {
        LoginRequestDto login = new LoginRequestDto();
        login.setEmail("unknown@example.com");
        login.setPassword("whatever");

        mockMvc.perform(post("/user/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(login)))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.flag").value(false))
                .andExpect(jsonPath("$.code").value(401))
                .andExpect(jsonPath("$.message").value("username or password is incorrect"));
    }
}
