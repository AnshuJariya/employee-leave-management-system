package com.example.leave_management.service;

import com.example.leave_management.LeaveManagementApplication;
import com.example.leave_management.entity.User;
import com.example.leave_management.repository.LeaveRequestRepository;
import com.example.leave_management.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.*;

//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;

    private LeaveRequestRepository leaveRequestRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);
        if (user == null) {
//            throw new UsernameNotFoundException(username);
            throw new UsernameNotFoundException("User not found with username: " + username);

        }
        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                Collections.singleton(
                        new SimpleGrantedAuthority("ROLE_"+user.getRole())
                )
        );
    }

    public Map<String, Long> getPositionWiseLeaveCount() {

        List<Object[]> results =
                leaveRequestRepository.getPositionWiseLeaveCount();

        Map<String, Long> counts = new HashMap<>();

        for (Object[] row : results) {

            String position = (String) row[0];

            Long count = (Long) row[1];

            counts.put(position, count);
        }

        return counts;
    }
}
