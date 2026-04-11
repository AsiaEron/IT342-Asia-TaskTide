package edu.cit.asia.tasktide.Context;

public class AuthResponseDTO {

    private String token;
    private Integer userId;
    private String email;

    public AuthResponseDTO(String token, Integer userId, String email) {
        this.token = token;
        this.userId = userId;
        this.email = email;
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
}
