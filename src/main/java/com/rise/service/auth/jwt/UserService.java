package com.rise.service.auth.jwt;

import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService {

    UserDetailsService UserDetailsService();

    UserDetailsService userDetailsService();

//    void updatePassword(String email, String newPassword);
}
