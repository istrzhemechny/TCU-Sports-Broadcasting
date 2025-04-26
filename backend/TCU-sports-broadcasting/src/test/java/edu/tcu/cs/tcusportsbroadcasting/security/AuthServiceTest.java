package edu.tcu.cs.tcusportsbroadcasting.security;

import edu.tcu.cs.tcusportsbroadcasting.crewmember.CrewMember;
import edu.tcu.cs.tcusportsbroadcasting.crewmember.MyCrewMemberPrincipal;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.core.Authentication;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class AuthServiceTest {

    private JwtProvider jwtProvider;
    private AuthService authService;

    @BeforeEach
    void setUp() {
        this.jwtProvider = mock(JwtProvider.class);
        this.authService = new AuthService(jwtProvider);
    }

    @Test
    void createLoginInfoShouldReturnCorrectData() {
        // Arrange
        CrewMember crewMember = new CrewMember();
        crewMember.setId(1L);
        crewMember.setEmail("john.doe@example.com");
        crewMember.setRole("ADMIN");

        MyCrewMemberPrincipal principal = new MyCrewMemberPrincipal(crewMember);

        Authentication authentication = mock(Authentication.class);
        when(authentication.getPrincipal()).thenReturn(principal);

        when(jwtProvider.createToken(1L, "ADMIN", "john.doe@example.com"))
                .thenReturn("fake-jwt-token");

        // Act
        Map<String, Object> result = authService.createLoginInfo(authentication);

        // Assert
        assertThat(result.get("userId")).isEqualTo(1L);
        assertThat(result.get("role")).isEqualTo("ADMIN");
        assertThat(result.get("token")).isEqualTo("fake-jwt-token");
    }
}
