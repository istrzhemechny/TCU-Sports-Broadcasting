package edu.tcu.cs.tcusportsbroadcasting.security;

import edu.tcu.cs.tcusportsbroadcasting.common.ApiResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user/auth")
public class AuthController {

    private final AuthService authService;

    private static final Logger LOGGER = LoggerFactory.getLogger(AuthController.class);

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse> getLoginInfo(Authentication authentication){
        LOGGER.debug("Authentication user: '{}'", authentication.getName());
        ApiResponse response = new ApiResponse(true, 200, "Crew Member data and JSON Web Token", this.authService.createLoginInfo(authentication));
        return ResponseEntity.ok(response);
    }

}
