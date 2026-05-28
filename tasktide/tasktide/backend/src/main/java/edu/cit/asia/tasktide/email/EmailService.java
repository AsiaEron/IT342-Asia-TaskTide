package edu.cit.asia.tasktide.email;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import edu.cit.asia.tasktide.user.entity.UserModel;

@Service
public class EmailService {

    private static final Logger logger = LoggerFactory.getLogger(EmailService.class);

    @Autowired
    private JavaMailSender mailSender;

    @Value("${spring.mail.username:}")
    private String mailUsername;

    public void sendWelcomeEmail(UserModel user) {
        if (user == null || user.getEmail() == null || user.getEmail().isBlank()) {
            return;
        }

        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(user.getEmail());
            message.setSubject("Welcome to TaskTide");
            message.setText("Hello " + (user.getFname() != null ? user.getFname() : "") + ",\n\n" +
                    "Your TaskTide account has been created successfully.\n\n" +
                    "Thank you for joining TaskTide!\n\n" +
                    "If you did not request this, please ignore this message.");
            message.setFrom(getFromAddress());
            mailSender.send(message);
        } catch (Exception ex) {
            logger.error("Failed to send welcome email to {}", user.getEmail(), ex);
        }
    }

    public void sendVerificationCodeEmail(UserModel user, String code) {
        if (user == null || user.getEmail() == null || user.getEmail().isBlank() || code == null || code.isBlank()) {
            return;
        }

        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(user.getEmail());
            message.setSubject("TaskTide Email Verification Code");
            message.setText("Hello " + (user.getFname() != null ? user.getFname() : "") + ",\n\n" +
                    "Your email verification code is: " + code + "\n\n" +
                    "Enter this code in TaskTide to verify your account.\n\n" +
                    "If you did not create this account, please ignore this email.");
            message.setFrom(getFromAddress());
            mailSender.send(message);
        } catch (Exception ex) {
            logger.error("Failed to send verification email to {}", user.getEmail(), ex);
        }
    }

    private String getFromAddress() {
        String sender = mailUsername != null && !mailUsername.isBlank() ? mailUsername : System.getenv("MAIL_USERNAME");
        return sender != null && !sender.isBlank() ? sender : "no-reply@tasktide.app";
    }
}
