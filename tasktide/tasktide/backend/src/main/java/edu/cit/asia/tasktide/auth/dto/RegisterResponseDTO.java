package edu.cit.asia.tasktide.auth.dto;

public class RegisterResponseDTO {

    private Integer userId;
    private String email;
    private String message;

    public RegisterResponseDTO(Integer userId, String email, String message) {
        this.userId = userId;
        this.email = email;
        this.message = message;
    }

    public Integer getUserId() {
        return userId;
    }

    public String getEmail() {
        return email;
    }

    public String getMessage() {
        return message;
    }
}
