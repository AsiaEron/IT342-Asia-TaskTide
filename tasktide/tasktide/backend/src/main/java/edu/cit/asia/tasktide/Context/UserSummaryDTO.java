package edu.cit.asia.tasktide.Context;

public class UserSummaryDTO {

    private Integer userId;
    private String email;
    private String firstName;
    private String lastName;

    public UserSummaryDTO(Integer userId, String email, String firstName, String lastName) {
        this.userId = userId;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public Integer getUserId() {
        return userId;
    }

    public String getEmail() {
        return email;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }
}
