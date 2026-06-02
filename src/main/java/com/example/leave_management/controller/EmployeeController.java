package com.example.leave_management.controller;

import com.example.leave_management.entity.LeaveRequest;
import com.example.leave_management.entity.User;
import com.example.leave_management.repository.LeaveRequestRepository;
import com.example.leave_management.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/employee")
public class EmployeeController {

    private final LeaveRequestRepository leaveRepository;
    private final UserRepository userRepository;

    public EmployeeController(
            LeaveRequestRepository leaveRepository,
            UserRepository userRepository) {

        this.leaveRepository = leaveRepository;
        this.userRepository = userRepository;
    }

    @GetMapping("/dashboard")
    public Map<String, Long> dashboard(
            Authentication authentication) {

        User user = userRepository
                .findByUsername(authentication.getName());

        Map<String, Long> data = new HashMap<>();

        data.put(
                "approved",
                leaveRepository.countByEmployeeAndStatus(
                        user,
                        "APPROVED"
                )
        );

        data.put(
                "pending",
                leaveRepository.countByEmployeeAndStatus(
                        user,
                        "PENDING"
                )
        );

        return data;
    }

    @PostMapping("/apply")
    public String applyLeave(
            @RequestBody LeaveRequest leave,
            Authentication authentication) {

        User user = userRepository
                .findByUsername(authentication.getName());

        leave.setEmployee(user);
        leave.setStatus("PENDING");

        leaveRepository.save(leave);

        return "Leave Applied Successfully";
    }

    @GetMapping("/history")
    public List<LeaveRequest> history(
            Authentication authentication) {

        User user = userRepository
                .findByUsername(authentication.getName());

        return leaveRepository.findByEmployee(user);
    }
}