package edu.cit.asia.tasktide.Controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import edu.cit.asia.tasktide.Context.AuthResponseDTO;
import edu.cit.asia.tasktide.Context.RegisterResponseDTO;
import edu.cit.asia.tasktide.Context.UserSummaryDTO;
import edu.cit.asia.tasktide.Model.UserModel;
import edu.cit.asia.tasktide.Service.UserService;

@RestController
@RequestMapping("/users")
@CrossOrigin(origins = "*")
public class UserController {

    @Autowired
    UserService userService;

    @PostMapping("/register")
    public ResponseEntity<RegisterResponseDTO> registerUser(@RequestBody UserModel user) {
        UserModel savedUser = userService.registerUser(user);
        RegisterResponseDTO response = new RegisterResponseDTO(
                savedUser.getUser_id(),
                savedUser.getEmail(),
                "Account created successfully"
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> login(@RequestBody UserModel user) {
        String token = userService.login(user.getEmail(), user.getPassword());
        if (token == null || token.isBlank()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        UserModel existingUser = userService.findByEmail(user.getEmail());
        AuthResponseDTO response = new AuthResponseDTO(
                token,
                existingUser != null ? existingUser.getUser_id() : null,
                user.getEmail()
        );
        return ResponseEntity.ok(response);
    }

    @GetMapping("/all")
    public List<UserSummaryDTO> getAllUsers() {
        return userService.getAllUsers().stream()
                .map(u -> new UserSummaryDTO(u.getUser_id(), u.getEmail(), u.getFname(), u.getLname()))
                .collect(Collectors.toList());
    }

}