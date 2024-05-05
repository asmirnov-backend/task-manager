package com.example.taskmanager.user.dto;

import com.example.taskmanager.user.Password;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ChangePasswordDTO {
    @Password
    private String currentPassword;

    @Password
    private String newPassword;
}
