package com.example.Ecom_Project.service;


import com.example.Ecom_Project.model.UserPrincipal;
import com.example.Ecom_Project.model.Users;
import com.example.Ecom_Project.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class MyUserDestailsService implements UserDetailsService {

    @Autowired
    private UserRepo repo;

    public MyUserDestailsService() {
    }

    public MyUserDestailsService(UserRepo repo) {
        this.repo = repo;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Optional<Users> user = repo.findByUserEmail(username);
        Users correctUser = null;
        if (user.isPresent()) {
            correctUser = user.get();
        }

        if (user == null) {
            System.out.println("User Not Found");
            throw new UsernameNotFoundException("User Not Found");
        }
        System.out.println(correctUser);
        System.out.println("Load "+ correctUser.getUserEmail());

        // Convert list of roles into a list of GrantedAuthority
        List<GrantedAuthority> authorities = correctUser.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role)) // Prefix roles with "ROLE_"
                .collect(Collectors.toList());

        return org.springframework.security.core.userdetails.User
                .withUsername(correctUser.getUserEmail())
                .password(correctUser.getUserPassword())
                .authorities(authorities) // Assign multiple roles
                .build();
    }
    }
