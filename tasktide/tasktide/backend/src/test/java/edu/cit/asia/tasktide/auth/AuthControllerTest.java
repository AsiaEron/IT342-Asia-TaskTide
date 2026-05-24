package edu.cit.asia.tasktide.auth;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import edu.cit.asia.tasktide.auth.LoginResult;
import edu.cit.asia.tasktide.shared.entity.UserModel;

@WebMvcTest(AuthController.class)
public class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testRegisterUser_Success() throws Exception {
        UserModel user = new UserModel();
        user.setFname("John");
        user.setLname("Doe");
        user.setEmail("john@example.com");
        user.setPassword("password");

        UserModel savedUser = new UserModel();
        savedUser.setUser_id(1);
        savedUser.setEmail("john@example.com");

        when(userService.registerUser(any(UserModel.class))).thenReturn(savedUser);

        mockMvc.perform(post("/users/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.user_id").value(1))
                .andExpect(jsonPath("$.email").value("john@example.com"))
                .andExpect(jsonPath("$.message").value("Registration successful. A verification code has been sent to your email."));
    }

    @Test
    public void testLogin_Success() throws Exception {
        UserModel user = new UserModel();
        user.setEmail("john@example.com");
        user.setPassword("password");

        UserModel existingUser = new UserModel();
        existingUser.setUser_id(1);
        existingUser.setEmail("john@example.com");

        when(userService.login(anyString(), anyString())).thenReturn(new LoginResult(LoginResult.Status.SUCCESS, "token123"));
        when(userService.findByEmail(anyString())).thenReturn(existingUser);

        mockMvc.perform(post("/users/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("token123"))
                .andExpect(jsonPath("$.user_id").value(1))
                .andExpect(jsonPath("$.email").value("john@example.com"));
    }

    @Test
    public void testLogin_InvalidCredentials() throws Exception {
        UserModel user = new UserModel();
        user.setEmail("john@example.com");
        user.setPassword("wrongpassword");

        when(userService.login(anyString(), anyString())).thenReturn(new LoginResult(LoginResult.Status.INVALID_CREDENTIALS, null));

        mockMvc.perform(post("/users/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isUnauthorized());
    }
}