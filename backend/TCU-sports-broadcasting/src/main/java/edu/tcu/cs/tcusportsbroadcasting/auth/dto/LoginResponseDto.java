package edu.tcu.cs.tcusportsbroadcasting.auth.dto;

public class LoginResponseDto {

    private Long userId;
    private String role;
    private String token;

    public LoginResponseDto(Long userId, String role, String token) {
        this.userId = userId;
        this.role = role;
        this.token = token;
    }

    // Getters

    public Long getUserId() {
        return userId;
    }

    public String getRole() {
        return role;
    }

    public String getToken() {
        return token;
    }
}
