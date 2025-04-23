package edu.tcu.cs.tcusportsbroadcasting.auth;

import edu.tcu.cs.tcusportsbroadcasting.auth.dto.LoginRequestDto;
import edu.tcu.cs.tcusportsbroadcasting.auth.dto.LoginResponseDto;
import edu.tcu.cs.tcusportsbroadcasting.common.ApiResponse;
import edu.tcu.cs.tcusportsbroadcasting.crewmember.CrewMember;
import edu.tcu.cs.tcusportsbroadcasting.crewmember.CrewMemberRepository;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/user/auth")
public class AuthController {

    private final CrewMemberRepository crewMemberRepository;

    public AuthController(CrewMemberRepository crewMemberRepository) {
        this.crewMemberRepository = crewMemberRepository;
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse> login(@Valid @RequestBody LoginRequestDto loginRequest) {
        Optional<CrewMember> optionalUser = crewMemberRepository.findAll().stream()
                .filter(u -> u.getEmail().equalsIgnoreCase(loginRequest.getEmail()))
                .findFirst();

        if (optionalUser.isPresent()) {
            CrewMember user = optionalUser.get();

            if (user.getPassword().equals(loginRequest.getPassword())) {
                String token = JwtUtil.generateToken(user.getId(), user.getRole(), user.getEmail());
                LoginResponseDto responseDto = new LoginResponseDto(user.getId(), user.getRole(), token);
                ApiResponse response = new ApiResponse(true, 200, "Login Success", responseDto);
                return ResponseEntity.ok(response);
            }
        }

        ApiResponse error = new ApiResponse(false, 401, "username or password is incorrect", null);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
    }
}
