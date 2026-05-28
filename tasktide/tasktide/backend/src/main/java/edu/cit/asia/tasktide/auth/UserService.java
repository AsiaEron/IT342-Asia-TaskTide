package edu.cit.asia.tasktide.auth;

import java.util.Date;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import edu.cit.asia.tasktide.email.EmailService;
import edu.cit.asia.tasktide.user.entity.RoleModel;
import edu.cit.asia.tasktide.user.entity.UserModel;
import edu.cit.asia.tasktide.user.repository.RoleRepository;
import edu.cit.asia.tasktide.user.repository.UserRepository;
import edu.cit.asia.tasktide.shared.security.JwtUtil;

@Service
public class UserService {

    private static final String DEFAULT_ROLE_NAME = "ROLE_USER";
    private static final String ADMIN_ROLE_NAME = "ROLE_ADMIN";

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private EmailService emailService;

    public UserModel registerUser(UserModel user) {
        normalizeEmail(user);
        return registerUser(user, DEFAULT_ROLE_NAME);
    }

    public UserModel registerAdmin(UserModel user) {
        normalizeEmail(user);
        return registerUser(user, ADMIN_ROLE_NAME);
    }

    private UserModel registerUser(UserModel user, String roleName) {
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
        user.setEmailVerified(false);
        user.setRole(getOrCreateRole(roleName));
        user.setDate_joined(new Date());

        String verificationCode = generateVerificationCode();
        user.setVerificationCode(verificationCode);

        UserModel savedUser = userRepository.save(user);
        emailService.sendVerificationCodeEmail(savedUser, verificationCode);
        return savedUser;
    }

    private RoleModel getOrCreateRole(String roleName) {
        RoleModel role = roleRepository.findByRoleName(roleName);
        if (role == null) {
            role = new RoleModel();
            role.setRoleName(roleName);
            role = roleRepository.save(role);
        }
        return role;
    }

    public boolean verifyEmail(String email, String verificationCode) {
        String normalizedEmail = normalizeEmail(email);
        UserModel user = userRepository.findByEmailIgnoreCase(normalizedEmail);
        if (user == null || user.getVerificationCode() == null) {
            return false;
        }

        if (user.getVerificationCode().equals(verificationCode)) {
            user.setEmailVerified(true);
            user.setVerificationCode(null);
            userRepository.save(user);
            return true;
        }
        return false;
    }

    public LoginResult login(String email, String rawPassword) {
        String normalizedEmail = normalizeEmail(email);
        UserModel user = userRepository.findByEmailIgnoreCase(normalizedEmail);
        if (user == null || !passwordEncoder.matches(rawPassword, user.getPassword())) {
            return new LoginResult(LoginResult.Status.INVALID_CREDENTIALS, null);
        }

        if (!user.isEmailVerified()) {
            return new LoginResult(LoginResult.Status.UNVERIFIED, null);
        }

        String roleName = user.getRole() != null ? user.getRole().getRoleName() : "ROLE_USER";
        return new LoginResult(LoginResult.Status.SUCCESS, jwtUtil.generateToken(normalizedEmail, roleName));
    }

    private String generateVerificationCode() {
        int code = new Random().nextInt(900000) + 100000;
        return String.valueOf(code);
    }

    public UserModel findByEmail(String email) {
        return userRepository.findByEmailIgnoreCase(normalizeEmail(email));
    }

    private void normalizeEmail(UserModel user) {
        if (user != null && user.getEmail() != null) {
            user.setEmail(user.getEmail().trim().toLowerCase());
        }
    }

    private String normalizeEmail(String email) {
        return email != null ? email.trim().toLowerCase() : null;
    }
}
