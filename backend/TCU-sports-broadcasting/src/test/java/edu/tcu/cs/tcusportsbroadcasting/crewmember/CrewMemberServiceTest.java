package edu.tcu.cs.tcusportsbroadcasting.crewmember;

import edu.tcu.cs.tcusportsbroadcasting.crewmember.dto.CrewMemberDto;
import edu.tcu.cs.tcusportsbroadcasting.crewmember.CrewMember;
import edu.tcu.cs.tcusportsbroadcasting.crewmember.CrewMemberRepository;
import edu.tcu.cs.tcusportsbroadcasting.crewmember.dto.CrewMemberResponseDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
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
        assertThat(result.getPositions()).contains("Director", "Graphics");
    }


    @Test
    void shouldThrowExceptionForDuplicateEmail() {
        // given
        CrewMemberDto dto = new CrewMemberDto();
        dto.setEmail("already@used.com");
        when(crewMemberRepository.existsByEmail("already@used.com")).thenReturn(true);

        // expect
        org.junit.jupiter.api.Assertions.assertThrows(
                edu.tcu.cs.tcusportsbroadcasting.crewmember.exception.DuplicateEmailException.class,
                () -> crewMemberService.addCrewMember("token", dto)
        );
    }
}
