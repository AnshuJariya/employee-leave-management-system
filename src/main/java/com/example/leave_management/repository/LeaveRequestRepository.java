package com.example.leave_management.repository;

import com.example.leave_management.entity.LeaveRequest;
import com.example.leave_management.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface LeaveRequestRepository
        extends JpaRepository<LeaveRequest, Long> {

    List<LeaveRequest> findByEmployee(User user);

    List<LeaveRequest> findByStatus(String status);

    long countByEmployeeAndStatus(User user, String status);

    long countByStatus(String status);

    @Query("""
       SELECT l.employee.position, COUNT(l)
       FROM LeaveRequest l
       GROUP BY l.employee.position
       """)
    List<Object[]> getPositionWiseLeaveCount();
    @Query("""
       SELECT l.employee.position,
              COUNT(l)
       FROM LeaveRequest l
       WHERE l.status='PENDING'
       GROUP BY l.employee.position
       """)
    List<Object[]> getPendingPositionWiseCount();

    List<LeaveRequest> findByEmployee_Position(String position);
}