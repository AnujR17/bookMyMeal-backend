package com.rise.controllers;

import com.MEALPROJECT.MEAL_PROJECT1.Util.JwtUtil;
import com.MEALPROJECT.MEAL_PROJECT1.dtos.AuthenticationRequest;
import com.MEALPROJECT.MEAL_PROJECT1.dtos.AuthenticationResponse;
import com.MEALPROJECT.MEAL_PROJECT1.dtos.SignupRequest;
import com.MEALPROJECT.MEAL_PROJECT1.dtos.UserDto;
import com.MEALPROJECT.MEAL_PROJECT1.entities.User;
import com.MEALPROJECT.MEAL_PROJECT1.repositories.UserRepository;
import com.MEALPROJECT.MEAL_PROJECT1.services.auth.AuthService;
import com.MEALPROJECT.MEAL_PROJECT1.services.auth.jwt.UserDetailsServiceImpl;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Optional;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

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
        Optional<User> existingUser = userRepository.findFirstByEmail(signupRequest.getEmail());
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
    public AuthenticationResponse createAuthenticationToken(@RequestBody AuthenticationRequest authenticationRequest, HttpServletResponse response) throws IOException {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken((authenticationRequest.getEmail()), authenticationRequest.getPassword()));

        } catch (BadCredentialsException e) {
            throw new BadCredentialsException("Incorrrect username or password");
        } catch (DisabledException disabledException) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "User not active");
            return null;
        }


        final UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getEmail());
        final String jwt = jwtUtil.generateToken(userDetails);
        Optional<User> optionalUser = userRepository.findFirstByEmail(userDetails.getUsername());
        AuthenticationResponse authenticationResponse = new AuthenticationResponse();
        if (optionalUser.isPresent()) {
            authenticationResponse.setJwt(jwt);
            authenticationResponse.setUserRole(optionalUser.get().getUserRole());
            authenticationResponse.setUserId(optionalUser.get().getId());

        }
        return authenticationResponse;
    }


}


