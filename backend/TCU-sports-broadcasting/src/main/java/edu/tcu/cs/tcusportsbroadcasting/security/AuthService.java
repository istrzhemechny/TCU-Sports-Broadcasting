package edu.tcu.cs.tcusportsbroadcasting.security;

import edu.tcu.cs.tcusportsbroadcasting.crewmember.CrewMember;
import edu.tcu.cs.tcusportsbroadcasting.crewmember.MyCrewMemberPrincipal;
import edu.tcu.cs.tcusportsbroadcasting.crewmember.dto.CrewMemberDto;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class AuthService {

    private final JwtProvider jwtProvider;

    public AuthService(JwtProvider jwtProvider) {
        this.jwtProvider = jwtProvider;
    }

    public Map<String, Object> createLoginInfo(Authentication authentication) {
        MyCrewMemberPrincipal principal = (MyCrewMemberPrincipal) authentication.getPrincipal();
        CrewMember crewMember = principal.getCrewMember();

        // Create the JWT token
        String token = this.jwtProvider.createToken(crewMember.getId(), crewMember.getRole(), crewMember.getEmail());

        // Now build the response data
        Map<String, Object> loginData = new HashMap<>();
        loginData.put("userId", crewMember.getId());
        loginData.put("role", crewMember.getRole());
        loginData.put("token", token);

        return loginData;
    }

}
