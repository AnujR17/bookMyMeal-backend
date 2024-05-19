package com.rise.dto;


import com.rise.enums.UserRole;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {

    private  Long id;

    private String name;

    private String email;

    private String password;

    private UserRole userRole;
}
