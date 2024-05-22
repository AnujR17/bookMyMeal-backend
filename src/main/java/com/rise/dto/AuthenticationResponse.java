package com.rise.dto;


import com.rise.enums.UserRole;
import lombok.Data;

@Data
public class AuthenticationResponse {

    private String jwt;

    private String name;

    private Long userId;

}
