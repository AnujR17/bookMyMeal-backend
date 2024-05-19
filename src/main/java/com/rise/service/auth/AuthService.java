package com.rise.service.auth;


import com.rise.dto.SignupRequest;
import com.rise.dto.UserDto;

public interface AuthService {
    UserDto createUser(SignupRequest signupRequest);



}
