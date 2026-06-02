package com.example.leave_management.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "leave_requests")
@Getter @Setter
public class LeaveRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String userName;
    private String leaveType;
    private LocalDate fromDate;
    private LocalDate toDate;

    @Column(length = 1000)
    private String reason;

    private String status;

    @ManyToOne
    @JoinColumn(name = "employee_id")
    private User employee;
}
