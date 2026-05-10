package edu.cit.asia.tasktide.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import edu.cit.asia.tasktide.shared.entity.UserModel;
import edu.cit.asia.tasktide.shared.repository.UserRepository;
import edu.cit.asia.tasktide.shared.security.JwtUtil;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    public UserModel registerUser(UserModel user) {
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
        return userRepository.save(user);
    }

    public String login(String email, String rawPassword) {
        UserModel user = userRepository.findByEmail(email);
        if (user != null && passwordEncoder.matches(rawPassword, user.getPassword())) {
            // Return JWT
            return jwtUtil.generateToken(email);
        }
        return null; // invalid credentials
    }

    public UserModel findByEmail(String email) {
        return userRepository.findByEmail(email);
    }
}
