package edu.tcu.cs.tcusportsbroadcasting.crewmember;

import edu.tcu.cs.tcusportsbroadcasting.common.ApiResponse;
import edu.tcu.cs.tcusportsbroadcasting.crewmember.dto.CrewMemberDto;
import edu.tcu.cs.tcusportsbroadcasting.crewmember.dto.CrewMemberResponseDto;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/User")
public class CrewMemberController {

    private final CrewMemberService crewMemberService;

    public CrewMemberController(CrewMemberService crewMemberService) {
        this.crewMemberService = crewMemberService;
    }

    @PostMapping("/crewMember")
    public ResponseEntity<ApiResponse> addCrewMember(
            @RequestParam(name = "token", required = false) String token,
            @Valid @RequestBody CrewMemberDto dto
    ) {
        CrewMemberResponseDto saved = crewMemberService.addCrewMember(token, dto);

        ApiResponse response = new ApiResponse(
                true,
                200,
                "Add Success",
                saved
        );

        return ResponseEntity.ok(response);
    }

    @GetMapping("/crewMember/{userId}")
    public ResponseEntity<ApiResponse> findCrewMemberById(@PathVariable Long userId) {
        CrewMemberResponseDto found = crewMemberService.findById(userId);

        ApiResponse response = new ApiResponse(
                true,
                200,
                "Find Success",
                found
        );

        return ResponseEntity.ok(response);
    }

}
