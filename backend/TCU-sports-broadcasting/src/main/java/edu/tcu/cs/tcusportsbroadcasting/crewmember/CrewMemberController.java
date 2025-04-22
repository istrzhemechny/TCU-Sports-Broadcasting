package edu.tcu.cs.tcusportsbroadcasting.crewmember;

import edu.tcu.cs.tcusportsbroadcasting.common.ApiResponse;
import edu.tcu.cs.tcusportsbroadcasting.crewmember.dto.CrewMemberDto;
import edu.tcu.cs.tcusportsbroadcasting.crewmember.dto.CrewMemberListDto;
import edu.tcu.cs.tcusportsbroadcasting.crewmember.dto.CrewMemberResponseDto;
import edu.tcu.cs.tcusportsbroadcasting.crewmember.dto.InviteRequestDto;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
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

    @GetMapping("/crewMember")
    public ResponseEntity<ApiResponse> findAllCrewMembers() {
        List<CrewMemberListDto> crewList = crewMemberService.findAll();

        ApiResponse response = new ApiResponse(
                true,
                200,
                "Find Success",
                crewList
        );

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/crewMember/{userId}")
    public ResponseEntity<ApiResponse> deleteCrewMember(@PathVariable Long userId) {
        crewMemberService.deleteCrewMember(userId);
        ApiResponse response = new ApiResponse(true, 200, "Delete Success", null);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/invite")
    public ResponseEntity<ApiResponse> inviteCrewMembers(@Valid @RequestBody InviteRequestDto dto) {
        List<String> invited = crewMemberService.inviteCrewMembers(dto.getEmails());
        ApiResponse response = new ApiResponse(true, 200, "Invite Success", invited);
        return ResponseEntity.ok(response);
    }


}
