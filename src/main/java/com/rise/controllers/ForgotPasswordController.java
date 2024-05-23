
package com.rise.controllers;


import com.rise.dto.MailBody;
import com.rise.entity.ForgotPassword;
import com.rise.entity.User;
import com.rise.repository.ForgotPasswordRepository;
import com.rise.repository.UserRepository;
import com.rise.service.auth.EmailService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.*;

@CrossOrigin("*")
@RestController
@RequestMapping("/forgotPassword")
public class ForgotPasswordController {

    private final UserRepository userRepository;

    private final EmailService emailService;

    private final ForgotPasswordRepository forgotPasswordRepository;

    private final PasswordEncoder passwordEncoder;

    public ForgotPasswordController(UserRepository userRepository, EmailService emailService, ForgotPasswordRepository forgotPasswordRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.emailService = emailService;
        this.forgotPasswordRepository = forgotPasswordRepository;
        this.passwordEncoder = passwordEncoder;
    }
    @PostMapping("/verifyMail")
    public ResponseEntity<String> verifyEmail(@RequestBody Map<String, String> requestBody) {
        String email = requestBody.get("email");

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Please provide a valid email!"));

        int otp = otpGenerator();
        MailBody mailBody = MailBody.builder()
                .to(email)
                .text("This is the OTP for your Forgot Password request : " + otp)
                .subject("OTP for Forgot Password request")
                .build();

        // Find existing ForgotPassword entity
        ForgotPassword existingForgotPassword = forgotPasswordRepository.findByUser(user);

        // If exists, update OTP and expiration time
        if (existingForgotPassword != null) {
            existingForgotPassword.setOtp(otp);
            existingForgotPassword.setExpirationTime(new Date(System.currentTimeMillis() + 70 * 1000));
        } else { // Otherwise, create a new ForgotPassword entity
            existingForgotPassword = ForgotPassword.builder()
                    .otp(otp)
                    .expirationTime(new Date(System.currentTimeMillis() + 70 * 1000))
                    .user(user)
                    .build();
        }

        emailService.sendSimpleMessage(mailBody);
        forgotPasswordRepository.save(existingForgotPassword);

        return ResponseEntity.ok("Email sent for verification!");
    }



    //otp
    @PostMapping("/verifyOtp")
    public ResponseEntity<String> verifyOtp(@RequestBody Map<String, String> requestBody) {
        String email = requestBody.get("email");
        int otp = Integer.parseInt(requestBody.get("otp"));

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Please provide a valid email!"));

        ForgotPassword fp = forgotPasswordRepository.findByOtpAndUser(otp, user)
                .orElseThrow(() -> new RuntimeException("Invalid OTP for email: " + email));

        if (fp.getExpirationTime().before(Date.from(Instant.now()))) {
            forgotPasswordRepository.deleteById(fp.getFid());
            return new ResponseEntity<>("OTP has expired!", HttpStatus.EXPECTATION_FAILED);
        }

        return ResponseEntity.ok("OTP verified!");
    }

    @PostMapping("/changePassword")
    public ResponseEntity<String> changePasswordHandler(@RequestBody Map<String, String> requestBody) {
        String email = requestBody.get("email");
        String password = requestBody.get("password");
        String repeatPassword = requestBody.get("repeatPassword");

        if (!Objects.equals(password, repeatPassword)) {
            return new ResponseEntity<>("Please enter the password again!", HttpStatus.EXPECTATION_FAILED);
        }

        String encodedPassword = passwordEncoder.encode(password);
        userRepository.updatePassword(email, encodedPassword);

        return ResponseEntity.ok("Password has been changed!");
    }



    private Integer otpGenerator() {
        Random random = new Random();
        return random.nextInt(100_000, 999_999);
    }
}

