package edu.cit.asia.tasktide.auth;

public class LoginResult {

    public enum Status {
        SUCCESS,
        UNVERIFIED,
        INVALID_CREDENTIALS
    }

    private final Status status;
    private final String token;

    public LoginResult(Status status, String token) {
        this.status = status;
        this.token = token;
    }

    public Status getStatus() {
        return status;
    }

    public String getToken() {
        return token;
    }
}
