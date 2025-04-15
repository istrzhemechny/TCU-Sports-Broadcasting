package edu.tcu.cs.tcusportsbroadcasting.availability;

import edu.tcu.cs.tcusportsbroadcasting.availability.dto.AvailabilityDto;
import edu.tcu.cs.tcusportsbroadcasting.availability.dto.AvailabilityResponseDto;
import edu.tcu.cs.tcusportsbroadcasting.common.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/availability")
public class AvailabilityController {

    private final AvailabilityService availabilityService;

    public AvailabilityController(AvailabilityService availabilityService) {
        this.availabilityService = availabilityService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse> addAvailability(@Valid @RequestBody AvailabilityDto dto) {
        AvailabilityResponseDto saved = availabilityService.addAvailability(dto);
        ApiResponse response = new ApiResponse(true, 200, "Add Success", saved);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<ApiResponse> getAvailabilityByUser(@PathVariable Long userId) {
        List<AvailabilityResponseDto> availabilities = availabilityService.findByUserId(userId);
        ApiResponse response = new ApiResponse(true, 200, "Find Success", availabilities);
        return ResponseEntity.ok(response);
    }
}
