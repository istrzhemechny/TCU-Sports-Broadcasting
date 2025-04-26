package edu.tcu.cs.tcusportsbroadcasting.crewmember;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.tcu.cs.tcusportsbroadcasting.crewmember.dto.CrewMemberDto;
import edu.tcu.cs.tcusportsbroadcasting.crewmember.dto.CrewMemberListDto;
import edu.tcu.cs.tcusportsbroadcasting.crewmember.dto.CrewMemberResponseDto;
import edu.tcu.cs.tcusportsbroadcasting.crewmember.dto.InviteRequestDto;
import edu.tcu.cs.tcusportsbroadcasting.crewmember.exception.CrewMemberNotFoundException;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
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
        mockMvc.perform(post("/user/crewMember?token=test-token")
                        .with(jwt().jwt(jwt -> jwt.claim("authorities", "ROLE_ADMIN")))
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
        mockMvc.perform(get("/user/crewMember/{userId}", id)
                        .with(jwt().jwt(jwt -> jwt.claim("authorities", "ROLE_ADMIN")))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.message").value("Find Success"))
                .andExpect(jsonPath("$.data.email").value("john.doe@example.com"));
    }

    @Test
    void shouldReturnAllCrewMembersSuccessfully() throws Exception {
        CrewMemberListDto cm1 = new CrewMemberListDto(
                1L, "John Doe", "john.doe@example.com", "1234567890"
        );
        CrewMemberListDto cm2 = new CrewMemberListDto(
                2L, "Jane Smith", "jane.smith@example.com", "1112223333"
        );

        Mockito.when(crewMemberService.findAll()).thenReturn(List.of(cm1, cm2));

        mockMvc.perform(get("/user/crewMember")
                        .with(jwt().jwt(jwt -> jwt.claim("authorities", "ROLE_ADMIN")))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.message").value("Find Success"))
                .andExpect(jsonPath("$.data.length()").value(2))
                .andExpect(jsonPath("$.data[0].fullName").value("John Doe"));
    }

    @Test
    void shouldReturnSuccessResponseWhenDeletingCrewMember() throws Exception {
        // Arrange
        Long userId = 1L;
        doNothing().when(crewMemberService).deleteCrewMember(userId);

        // Act + Assert
        mockMvc.perform(delete("/user/crewMember/{userId}", userId)
                        .with(jwt().jwt(jwt -> jwt.claim("authorities", "ROLE_ADMIN"))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.message").value("Delete Success"))
                .andExpect(jsonPath("$.data").isEmpty());
    }

    @Test
    void shouldReturnNotFoundWhenDeletingNonExistentUser() throws Exception {
        // Arrange
        Long userId = 999L;
        doThrow(new CrewMemberNotFoundException(userId)).when(crewMemberService).deleteCrewMember(userId);

        // Act + Assert
        mockMvc.perform(delete("/user/crewMember/{userId}", userId)
                        .with(jwt().jwt(jwt -> jwt.claim("authorities", "ROLE_ADMIN"))))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.flag").value(false))
                .andExpect(jsonPath("$.code").value(404))
                .andExpect(jsonPath("$.message").value("Could not find user with id 999"))
                .andExpect(jsonPath("$.data").isEmpty());
    }

    @Test
    void shouldInviteCrewMembersSuccessfully() throws Exception {
        // Arrange
        List<String> emails = List.of("john.smith@example.com", "jane.doe@example.com");
        InviteRequestDto dto = new InviteRequestDto();
        dto.setEmails(emails);

        Mockito.when(crewMemberService.inviteCrewMembers(emails)).thenReturn(emails);

        // Act & Assert
        mockMvc.perform(post("/user/invite")
                        .with(jwt().jwt(jwt -> jwt.claim("authorities", "ROLE_ADMIN")))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.message").value("Invite Success"))
                .andExpect(jsonPath("$.data.length()").value(2))
                .andExpect(jsonPath("$.data[0]").value("john.smith@example.com"))
                .andExpect(jsonPath("$.data[1]").value("jane.doe@example.com"));
    }


}