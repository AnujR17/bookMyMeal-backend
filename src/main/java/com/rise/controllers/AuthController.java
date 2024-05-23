package com.rise.controllers;


import com.rise.Util.JwtUtil;
import com.rise.dto.AuthenticationRequest;
import com.rise.dto.AuthenticationResponse;
import com.rise.dto.SignupRequest;
import com.rise.dto.UserDto;
import com.rise.entity.User;
import com.rise.repository.UserRepository;
import com.rise.service.auth.AuthService;
import com.rise.service.auth.jwt.UserDetailsServiceImpl;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Map;
import java.util.Optional;

import static org.springframework.http.HttpStatus.*;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    private final UserDetailsServiceImpl userDetailsService;

    private final AuthenticationManager authenticationManager;

    private final JwtUtil jwtUtil;

    private final UserRepository userRepository;

//    private final ForgotPasswordService forgotPasswordService;


    public AuthController(AuthService authService, UserDetailsServiceImpl userDetailsService, AuthenticationManager authenticationManager, JwtUtil jwtUtil, UserRepository userRepository) {
        this.authService = authService;
        this.userDetailsService = userDetailsService;
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.userRepository = userRepository;
    }

    @PostMapping("/signup1")
    public ResponseEntity<?> signupUser(@RequestBody SignupRequest signupRequest) {
        //passwordmatch or not
        if (!signupRequest.getPassword().equals(signupRequest.getConfirmPassword())) {
            return new ResponseEntity<>("Password and confirm password do not match", HttpStatus.BAD_REQUEST);
        }

        //username already exists
        Optional<User> existingUser = Optional.ofNullable(userRepository.findByEmail(signupRequest.getEmail()));
        if (existingUser.isPresent()) {
            return new ResponseEntity<>("Username already exists", HttpStatus.BAD_REQUEST);
        }


        UserDto createdUserDto = authService.createUser(signupRequest);

        if (createdUserDto == null) {

            return new ResponseEntity<>("user not created.come again later", BAD_REQUEST);

        }
        return new ResponseEntity<>(createdUserDto, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<?> createAuthenticationToken(@RequestBody AuthenticationRequest authenticationRequest) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authenticationRequest.getEmail(), authenticationRequest.getPassword()));
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Incorrect username or password");
        } catch (DisabledException disabledException) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("User account is disabled");
        }

        final UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getEmail());
        final String jwt = jwtUtil.generateToken(userDetails);
        Optional<User> optionalUser = Optional.ofNullable(userRepository.findByEmail(userDetails.getUsername()));
        if (optionalUser.isPresent()) {
            AuthenticationResponse authenticationResponse = new AuthenticationResponse();
            authenticationResponse.setJwt(jwt);
            authenticationResponse.setName(optionalUser.get().getName());
            authenticationResponse.setUserId(optionalUser.get().getId());
            return ResponseEntity.ok(authenticationResponse);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }
    }



}


