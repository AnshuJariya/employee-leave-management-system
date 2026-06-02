package com.example.leave_management.controller;

import com.example.leave_management.entity.LeaveRequest;
import com.example.leave_management.repository.LeaveRequestRepository;
import com.example.leave_management.repository.UserRepository;
//import org.springframework.ui.Model;
import com.example.leave_management.service.EmailService;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/admin")
public class AdminController {

    private final LeaveRequestRepository leaveRepository;
    private final UserRepository userRepository;

    private final EmailService emailService;

    public AdminController(
            LeaveRequestRepository leaveRepository,
            UserRepository userRepository,
            EmailService emailService) {

        this.leaveRepository = leaveRepository;
        this.userRepository = userRepository;
        this.emailService = emailService;
    }

//    public AdminController(
//            LeaveRequestRepository leaveRepository,
//            UserRepository userRepository) {
//
//        this.leaveRepository = leaveRepository;
//        this.userRepository = userRepository;
//    }

    @GetMapping("/dashboard")
    public Map<String, Long> dashboard() {

        Map<String, Long> data =
                new HashMap<>();

        data.put(
                "employees",
                userRepository.count()
        );

        data.put(
                "pending",
                leaveRepository.countByStatus("PENDING")
        );

        data.put(
                "approved",
                leaveRepository.countByStatus("APPROVED")
        );
        data.put(
                "rejected",
                leaveRepository.countByStatus("REJECTED")
        );

        return data;
    }

    @GetMapping("/leaves")
    public List<LeaveRequest> allLeaves() {
        return leaveRepository.findAll();
    }

    @PutMapping("/approve/{id}")
    public String approve(
            @PathVariable Long id) {

        LeaveRequest leave =
                leaveRepository.findById(id)
                        .orElseThrow();

        leave.setStatus("APPROVED");

        leaveRepository.save(leave);

        String email = leave.getEmployee().getEmail();

        if (email != null && !email.isBlank()) {
            emailService.sendLeaveStatusEmail(email, leave);
        }

        return "Approved";
    }

    @PutMapping("/reject/{id}")
    public String reject(
            @PathVariable Long id) {

        LeaveRequest leave =
                leaveRepository.findById(id)
                        .orElseThrow();

        leave.setStatus("REJECTED");

        leaveRepository.save(leave);

        String email = leave.getEmployee().getEmail();

        if (email != null && !email.isBlank()) {
            emailService.sendLeaveStatusEmail(email, leave);
        }

        return "Rejected";
    }
    @PutMapping("/toggle/{id}")
    public String toggleLeave(@PathVariable Long id) {

        LeaveRequest leave =
                leaveRepository.findById(id)
                        .orElseThrow();

        if ("APPROVED".equals(leave.getStatus())) {

            leave.setStatus("REJECTED");

        } else {

            leave.setStatus("APPROVED");
        }

        leaveRepository.save(leave);

        System.out.println("Employee Email: " +
                leave.getEmployee().getEmail());

        emailService.sendLeaveStatusEmail(
                leave.getEmployee().getEmail(),
                leave
        );

        return leave.getStatus();
    }

    @GetMapping("/position-count")
    public Map<String, Long> getPositionCounts() {

        List<Object[]> results =
                leaveRepository.getPendingPositionWiseCount();

        Map<String, Long> counts =
                new HashMap<>();

        for (Object[] row : results) {

            String position =
                    (String) row[0];

            Long count =
                    (Long) row[1];

            counts.put(position, count);
        }

        return counts;
    }

    @GetMapping("/leaves/position/{position}")
    public List<LeaveRequest> getLeavesByPosition(
            @PathVariable String position) {

        return leaveRepository
                .findByEmployee_Position(position);
    }

}