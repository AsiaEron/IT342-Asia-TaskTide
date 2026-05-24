package edu.cit.asia.tasktide.auth.dto;

public class VerifyEmailRequestDTO {

    private String email;
    private String verificationCode;

    public VerifyEmailRequestDTO() {
    }

    public VerifyEmailRequestDTO(String email, String verificationCode) {
        this.email = email;
        this.verificationCode = verificationCode;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getVerificationCode() {
        return verificationCode;
    }

    public void setVerificationCode(String verificationCode) {
        this.verificationCode = verificationCode;
    }
}
