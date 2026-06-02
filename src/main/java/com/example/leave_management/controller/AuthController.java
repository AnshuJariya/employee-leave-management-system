package com.example.leave_management.controller;

import com.example.leave_management.dto.SignupRequest;
import com.example.leave_management.entity.User;
import com.example.leave_management.repository.UserRepository;
import com.example.leave_management.service.EmailService;
import jakarta.validation.Valid;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthController(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder) {

        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/signup")
    public String signup(
            @Valid
            @RequestBody SignupRequest request) {
//        System.out.println("Position = " + request.getPosition());

        if (request.getUsername() == null || request.getUsername().equals("")) {
            return "Username can not be empty";
        }
        if (request.getPassword() == null || request.getPassword().equals("")) {
            return "Password can not be empty";
        }

        if (userRepository.existsByUsername(
                request.getUsername())) {

            return "Username already exists";
        }

        User user = new User();

        user.setUsername(request.getUsername());

        user.setPassword(
                passwordEncoder.encode(
                        request.getPassword()
                )
        );

        user.setRole("EMPLOYEE");

        user.setPosition(request.getPosition());

        user.setEmail(request.getEmail());

        userRepository.save(user);

        return "Registration Successful";
    }
}