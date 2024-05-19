package com.rise.service.auth.jwt;

import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService {

    UserDetailsService UserDetailsService();

//    void updatePassword(String email, String newPassword);
}
