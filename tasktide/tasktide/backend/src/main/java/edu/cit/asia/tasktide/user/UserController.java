package edu.cit.asia.tasktide.user;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import edu.cit.asia.tasktide.user.repository.UserRepository;
import edu.cit.asia.tasktide.user.dto.UserSummaryDTO;

@RestController
@RequestMapping("/users")
@CrossOrigin(origins = "*")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/all")
    public List<UserSummaryDTO> getAllUsers() {
        return userRepository.findAll().stream()
                .filter(u -> u.getRole() == null || !"ROLE_ADMIN".equals(u.getRole().getRoleName()))
                .map(u -> new UserSummaryDTO(u.getUser_id(), u.getEmail(), u.getFname(), u.getLname()))
                .collect(Collectors.toList());
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<String> deleteUser(@PathVariable int userId) {
        if (!userRepository.existsById(userId)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found.");
        }

        userRepository.deleteById(userId);
        return ResponseEntity.ok("User deleted successfully.");
    }
}
