package com.rise.dto;

import lombok.Data;

@Data
public class ChangePasswordRequest {
    private String name;
    private String currentPassword;
    private String newPassword;

}
