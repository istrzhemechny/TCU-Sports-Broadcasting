package edu.tcu.cs.tcusportsbroadcasting.crewmember;

import edu.tcu.cs.tcusportsbroadcasting.crewmember.dto.CrewMemberDto;
import edu.tcu.cs.tcusportsbroadcasting.crewmember.dto.CrewMemberResponseDto;
import edu.tcu.cs.tcusportsbroadcasting.crewmember.exception.CrewMemberNotFoundException;
import edu.tcu.cs.tcusportsbroadcasting.crewmember.exception.DuplicateEmailException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class CrewMemberServiceTest {

    private CrewMemberRepository crewMemberRepository;

    private CrewMemberService crewMemberService;

    @BeforeEach
    void setUp() {
        crewMemberRepository = mock(CrewMemberRepository.class);
        crewMemberService = new CrewMemberService(crewMemberRepository);
    }

    @Test
    void shouldAddNewCrewMemberSuccessfully() {
        // given
        CrewMemberDto dto = new CrewMemberDto();
        dto.setFirstName("Alice");
        dto.setLastName("Wonderland");
        dto.setEmail("alice@wonder.com");
        dto.setPhoneNumber("1234567890");
        dto.setPassword("pass123");
        dto.setRole("USER");
        dto.setPosition(Arrays.asList("Director", "Graphics"));

        when(crewMemberRepository.existsByEmail("alice@wonder.com")).thenReturn(false);

        // Stub save to return the passed-in crew member with an ID set
        when(crewMemberRepository.save(any(CrewMember.class))).thenAnswer(invocation -> {
            CrewMember c = invocation.getArgument(0);
            c.setId(1L);
            return c;
        });

        // when
        CrewMemberResponseDto result = crewMemberService.addCrewMember("token", dto);

        // then
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getFirstName()).isEqualTo("Alice");
        assertThat(result.getPosition()).contains("Director", "Graphics");
    }


    @Test
    void shouldThrowExceptionForDuplicateEmail() {
        // given
        CrewMemberDto dto = new CrewMemberDto();
        dto.setEmail("already@used.com");
        when(crewMemberRepository.existsByEmail("already@used.com")).thenReturn(true);

        // expect
        assertThrows(
                DuplicateEmailException.class,
                () -> crewMemberService.addCrewMember("token", dto)
        );
    }

    @Test
    void shouldReturnCrewMemberById() {
        // Arrange
        CrewMember cm = new CrewMember();
        cm.setId(1L);
        cm.setFirstName("Peter");
        cm.setLastName("Parker");
        cm.setEmail("peter@dailybugle.com");
        cm.setPhoneNumber("2223334444");
        cm.setPassword("sp1derm@n");
        cm.setRole("USER");
        cm.setPosition(List.of("Graphics"));

        when(crewMemberRepository.findById(1L)).thenReturn(Optional.of(cm));

        // Act
        CrewMemberResponseDto result = crewMemberService.findById(1L);

        // Assert
        assertThat(result.getEmail()).isEqualTo("peter@dailybugle.com");
        assertThat(result.getRole()).isEqualTo("USER");
        assertThat(result.getPosition()).contains("Graphics");
    }

    @Test
    void shouldThrowExceptionWhenCrewMemberNotFound() {
        // Arrange
        when(crewMemberRepository.findById(404L)).thenReturn(Optional.empty());

        // Assert
        assertThrows(CrewMemberNotFoundException.class, () -> {
            crewMemberService.findById(404L);
        });
    }

    @Test
    void shouldDeleteCrewMemberByIdSuccessfully() {
        CrewMember cm = new CrewMember();
        cm.setId(1L);
        when(crewMemberRepository.findById(1L)).thenReturn(Optional.of(cm));

        crewMemberService.deleteCrewMember(1L);

        verify(crewMemberRepository).delete(cm);
    }

    @Test
    void shouldThrowExceptionWhenUserToDeleteNotFound() {
        when(crewMemberRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(CrewMemberNotFoundException.class, () -> crewMemberService.deleteCrewMember(1L));
    }

    @Test
    void shouldReturnListOfInvitedEmails() {
        // Arrange
        List<String> emails = List.of("john.smith@example.com", "jane.doe@example.com");

        // Act
        List<String> result = crewMemberService.inviteCrewMembers(emails);

        // Assert
        assertThat(result).isEqualTo(emails);
    }


}