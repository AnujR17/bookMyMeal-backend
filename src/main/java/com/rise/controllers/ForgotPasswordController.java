package com.rise.controllers;
//
//import com.MEALPROJECT.MEAL_PROJECT1.Util.ChangePassword;
//import com.MEALPROJECT.MEAL_PROJECT1.dtos.MailBody;
//import com.MEALPROJECT.MEAL_PROJECT1.entities.ForgotPassword;
//import com.MEALPROJECT.MEAL_PROJECT1.entities.User;
//import com.MEALPROJECT.MEAL_PROJECT1.repositories.ForgotPasswordRepository;
//import com.MEALPROJECT.MEAL_PROJECT1.repositories.UserRepository;
//import com.MEALPROJECT.MEAL_PROJECT1.services.auth.EmailService;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.web.bind.annotation.*;
//
//
//
//import java.time.Instant;
//import java.util.Date;
//import java.util.Objects;
//import java.util.Random;
//
//@CrossOrigin(origins = "*")
//@RestController
//@RequestMapping("/forgotPassword")
//public class ForgotPasswordController {
//    private  final UserRepository userRepository;
//
//    private  final EmailService emailService;
//    private  final ForgotPasswordRepository forgotPasswordRepository;
//
//    private  final PasswordEncoder passwordEncoder;
//
//    public ForgotPasswordController(UserRepository userRepository, EmailService emailService, ForgotPasswordRepository forgotPasswordRepository, PasswordEncoder passwordEncoder) {
//        this.userRepository = userRepository;
//        this.emailService = emailService;
//        this.forgotPasswordRepository = forgotPasswordRepository;
//        this.passwordEncoder = passwordEncoder;
//    }
////
////    @PostMapping("/verifyMail/{email}")
////    public ResponseEntity<String> verifyEmail(@PathVariable String email){
////        User user = userRepository.findByEmail(email)
////                .orElseThrow(() -> new UsernameNotFoundException("please provider an valid email!"));
////
////        int otp= otpGenertor();
////        MailBody mailBody=MailBody.builder()
////                .to(email)
////                .text("this is this otp for ur forgot password request:"+otp)
////                .subject("OTP for forgot password request")
////                .build();
////        ForgotPassword fp = ForgotPassword.builder()
////                .otp(otp)
////                .expirationTime(new Date(System.currentTimeMillis()+70*1000))
////                .user(user)
////                .build();
////
////        emailService.sendSimpleMessage(mailBody);
////        forgotPasswordRepository.save(fp);
////
////        return ResponseEntity.ok("Email sent for verification!");
////
////    }
//
//    @PostMapping("/verifyMail")
//    public ResponseEntity<String> verifyEmail(@PathVariable String email){
//        User user = userRepository.findByEmail(email);
//
//        if (user == null) {
//            throw new UsernameNotFoundException("Please provide a valid email!");
//        }
//
//        int otp = otpGenertor();
//        MailBody mailBody = MailBody.builder()
//                .to(email)
//                .text("This is the OTP for your forgot password request: " + otp)
//                .subject("OTP for forgot password request")
//                .build();
//        ForgotPassword fp = ForgotPassword.builder()
//                .otp(otp)
//                .expirationTime(new Date(System.currentTimeMillis() + 70 * 1000))
//                .user(user)
//                .build();
//
//        emailService.sendSimpleMessage(mailBody);
//        forgotPasswordRepository.save(fp);
//
//        return ResponseEntity.ok("Email sent for verification!");
//    }
//
//    @PostMapping("/verifyOtp")
//    public ResponseEntity<String> verifyOtp(@PathVariable Integer otp,@PathVariable String email){
//        User user = userRepository.findByEmail(email);
////                .orElseThrow(()-> new UsernameNotFoundException("please provider an valid email!")) ;
//        if (user == null) {
//            throw new UsernameNotFoundException("Please provide a valid email!");
//        }
//        ForgotPassword fp = forgotPasswordRepository.findByOtpAndUser(otp,user)
//                .orElseThrow(()-> new RuntimeException("Invaild OTP for email:" +email));
//
//        if(fp.getExpirationTime().before(Date.from(Instant.now()))){
//            forgotPasswordRepository.deleteById(fp.getFid());
//            return new ResponseEntity<>("Otp has expired!", HttpStatus.EXPECTATION_FAILED);
//        }
//
//        return ResponseEntity.ok("OTP verified!");
//
//    }
//
//    @PostMapping("/changePassword/{email}")
//    public ResponseEntity<String> changePasswordHandler(@RequestBody ChangePassword changePassword, @PathVariable String email){
//
//        if(!Objects.equals(changePassword.password(),changePassword.repeatPassword())){
//            return new ResponseEntity<>("please enter the password again!!",HttpStatus.EXPECTATION_FAILED);
//        }
//
//        String encodedPassword = passwordEncoder.encode(changePassword.password());
//
//        userRepository.updatePassword(email,encodedPassword);
//        return ResponseEntity.ok("Password has been changed!");
//
//    }
//
//    private Integer otpGenertor(){
//        Random random = new Random();
//        return random.nextInt(100_000,999_999);
//
//    }
//}


import com.MEALPROJECT.MEAL_PROJECT1.Util.ChangePassword;
import com.MEALPROJECT.MEAL_PROJECT1.dtos.MailBody;
import com.MEALPROJECT.MEAL_PROJECT1.entities.ForgotPassword;
import com.MEALPROJECT.MEAL_PROJECT1.entities.User;
import com.MEALPROJECT.MEAL_PROJECT1.repositories.ForgotPasswordRepository;
import com.MEALPROJECT.MEAL_PROJECT1.repositories.UserRepository;
import com.MEALPROJECT.MEAL_PROJECT1.services.auth.EmailService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.Date;
import java.util.Objects;
import java.util.Optional;
import java.util.Random;

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


    // send mail for email verification
    @PostMapping("/verifyMail/{email}")
    public ResponseEntity<String> verifyEmail(@PathVariable String email) {
//        User user = userRepository.findByEmail(email)
//                .orElseThrow(() -> new UsernameNotFoundException("Please provide an valid email!" + email));

        Optional<User> userOptional = Optional.ofNullable(userRepository.findByEmail(email));
        User user = userOptional.orElseThrow(() -> new UsernameNotFoundException("Please provide a valid email!"));

        int otp = otpGenerator();
        MailBody mailBody = MailBody.builder()
                .to(email)
                .text("This is the OTP for your Forgot Password request : " + otp)
                .subject("OTP for Forgot Password request")
                .build();

        ForgotPassword fp = ForgotPassword.builder()
                .otp(otp)
                .expirationTime(new Date(System.currentTimeMillis() + 20 * 1000))
                .user(user)
                .build();

        emailService.sendSimpleMessage(mailBody);
        forgotPasswordRepository.save(fp);

        return ResponseEntity.ok("Email sent for verification!");
    }

    @PostMapping("/verifyOtp/{otp}/{email}")
    public ResponseEntity<String> verifyOtp(@PathVariable Integer otp, @PathVariable String email) {
//        User user = userRepository.findByEmail(email)
//                .orElseThrow(() -> new UsernameNotFoundException("Please provide an valid email!"));
        Optional<User> userOptional = Optional.ofNullable(userRepository.findByEmail(email));
        User user = userOptional.orElseThrow(() -> new UsernameNotFoundException("Please provide a valid email!"));


        ForgotPassword fp = forgotPasswordRepository.findByOtpAndUser(otp, user)
                .orElseThrow(() -> new RuntimeException("Invalid OTP for email: " + email));

        if (fp.getExpirationTime().before(Date.from(Instant.now()))) {
            forgotPasswordRepository.deleteById(fp.getFid());
            return new ResponseEntity<>("OTP has expired!", HttpStatus.EXPECTATION_FAILED);
        }

        return ResponseEntity.ok("OTP verified!");
    }


    @PostMapping("/changePassword/{email}")
    public ResponseEntity<String> changePasswordHandler(@RequestBody ChangePassword changePassword,
                                                        @PathVariable String email) {
        if (!Objects.equals(changePassword.password(), changePassword.repeatPassword())) {
            return new ResponseEntity<>("Please enter the password again!", HttpStatus.EXPECTATION_FAILED);
        }

        String encodedPassword = passwordEncoder.encode(changePassword.password());
        userRepository.updatePassword(email, encodedPassword);

        return ResponseEntity.ok("Password has been changed!");
    }

    private Integer otpGenerator() {
        Random random = new Random();
        return random.nextInt(100_000, 999_999);
    }
}
