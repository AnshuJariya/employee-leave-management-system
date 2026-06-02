package com.example.leave_management.controller;

import com.example.leave_management.entity.User;
import com.example.leave_management.repository.UserRepository;
import org.springframework.stereotype.Controller;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DashboardController {

    private final UserRepository userRepository;

    public DashboardController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("/dashboard")
    public String dashboard(Authentication authentication) {

        User user =
                userRepository.findByUsername(
                        authentication.getName()
                );

        if ("ADMIN".equals(user.getRole())) {
            return "redirect:/admin-dashboard.html";
        }

        return "redirect:/employee-dashboard.html";
    }
}