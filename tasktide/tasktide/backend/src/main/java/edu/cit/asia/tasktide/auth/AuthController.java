package edu.cit.asia.tasktide.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import edu.cit.asia.tasktide.auth.dto.AuthResponseDTO;
import edu.cit.asia.tasktide.auth.dto.RegisterResponseDTO;
import edu.cit.asia.tasktide.auth.dto.VerifyEmailRequestDTO;
import edu.cit.asia.tasktide.shared.entity.UserModel;

@RestController
@RequestMapping("/users")
@CrossOrigin(origins = "*")
public class AuthController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<RegisterResponseDTO> registerUser(@RequestBody UserModel user) {
        UserModel savedUser = userService.registerUser(user);
        RegisterResponseDTO response = new RegisterResponseDTO(
                savedUser.getUser_id(),
                savedUser.getEmail(),
                "Registration successful. A verification code has been sent to your email."
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/register-admin")
    public ResponseEntity<RegisterResponseDTO> registerAdmin(@RequestBody UserModel user) {
        UserModel savedUser = userService.registerAdmin(user);
        RegisterResponseDTO response = new RegisterResponseDTO(
                savedUser.getUser_id(),
                savedUser.getEmail(),
                "Admin registration successful. A verification code has been sent to the admin email."
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/verify-email")
    public ResponseEntity<String> verifyEmail(@RequestBody VerifyEmailRequestDTO request) {
        boolean verified = userService.verifyEmail(request.getEmail(), request.getVerificationCode());
        if (!verified) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid verification code or email.");
        }
        return ResponseEntity.ok("Email verified successfully.");
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UserModel user) {
        LoginResult result = userService.login(user.getEmail(), user.getPassword());
        if (result.getStatus() == LoginResult.Status.INVALID_CREDENTIALS) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials.");
        }
        if (result.getStatus() == LoginResult.Status.UNVERIFIED) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Email not verified. Please verify your email before logging in.");
        }

        UserModel existingUser = userService.findByEmail(user.getEmail());
        String role = existingUser != null && existingUser.getRole() != null ? existingUser.getRole().getRoleName() : null;
        AuthResponseDTO response = new AuthResponseDTO(
                result.getToken(),
                existingUser != null ? existingUser.getUser_id() : null,
                user.getEmail(),
                role
        );
        return ResponseEntity.ok(response);
    }
}
