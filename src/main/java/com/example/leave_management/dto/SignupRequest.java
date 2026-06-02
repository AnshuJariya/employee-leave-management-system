package com.example.leave_management.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SignupRequest {

   @NotBlank(message = "Username is required")
    private String username;
   @NotBlank(message = "Password is required")
    private String password;
    private String position;
    private String email;
}