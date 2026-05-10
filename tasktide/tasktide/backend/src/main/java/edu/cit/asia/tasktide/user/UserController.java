package edu.cit.asia.tasktide.user;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import edu.cit.asia.tasktide.shared.repository.UserRepository;
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
                .map(u -> new UserSummaryDTO(u.getUser_id(), u.getEmail(), u.getFname(), u.getLname()))
                .collect(Collectors.toList());
    }
}
