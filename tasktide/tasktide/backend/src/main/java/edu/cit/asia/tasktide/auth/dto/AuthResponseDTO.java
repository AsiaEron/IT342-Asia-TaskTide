package edu.cit.asia.tasktide.auth.dto;

public class AuthResponseDTO {

    private String token;
    private Integer userId;
    private String email;
    private String role;

    public AuthResponseDTO(String token, Integer userId, String email, String role) {
        this.token = token;
        this.userId = userId;
        this.email = email;
        this.role = role;
    }

    public String getToken() {
        return token;
    }

    public Integer getUserId() {
        return userId;
    }

    public String getEmail() {
        return email;
    }

    public String getRole() {
        return role;
    }
}
