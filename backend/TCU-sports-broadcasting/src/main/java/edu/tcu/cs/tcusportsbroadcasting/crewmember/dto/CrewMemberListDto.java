package edu.tcu.cs.tcusportsbroadcasting.crewmember.dto;

public class CrewMemberListDto {

    private Long userId;
    private String fullName;
    private String email;
    private String phoneNumber;

    public CrewMemberListDto(Long userId, String fullName, String email, String phoneNumber) {
        this.userId = userId;
        this.fullName = fullName;
        this.email = email;
        this.phoneNumber = phoneNumber;
    }

    public Long getUserId() {
        return userId;
    }

    public String getFullName() {
        return fullName;
    }

    public String getEmail() {
        return email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }
}
