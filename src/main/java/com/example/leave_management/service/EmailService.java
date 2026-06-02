package com.example.leave_management.service;

import com.example.leave_management.entity.LeaveRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Async
    public void sendLeaveStatusEmail(String to, LeaveRequest leave) {


        if (leave == null || leave.getEmployee() == null) {
            System.out.println("Leave or Employee is null");
            return;
        }

        if (to == null || to.isBlank()) {
            System.out.println("Email address is missing");
            return;
        }

        System.out.println("Sending email to: " + email);

        SimpleMailMessage message = new SimpleMailMessage();

        message.setTo(to);
        message.setSubject("Leave Request Status Update");

        message.setText(
                "Hello " + leave.getEmployee().getUsername() + ",\n\n" +
                        "Your leave request has been " + leave.getStatus() + ".\n\n"+
                "Leave Details\n" +
                        "-------------------------\n" +
                        "Application ID : " + leave.getId() + "\n" +
                        "Leave Type     : " + leave.getLeaveType() + "\n" +
                        "From Date      : " + leave.getFromDate() + "\n" +
                        "To Date        : " + leave.getToDate() + "\n" +
                        "Reason         : " + leave.getReason() + "\n" +
                        "Status         : " + leave.getStatus() + "\n\n"

        );

        mailSender.send(message);
    }
}