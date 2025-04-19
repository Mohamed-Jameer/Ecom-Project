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
            System.out.println("loadUserByUsername");
            Users user = repo.findByUserEmail(username)
                    .orElseThrow(() -> new UsernameNotFoundException("User not found"));

            List<GrantedAuthority> authorities = user.getRoles().stream()
                    .map(role -> new SimpleGrantedAuthority("ROLE_" + role.getName()))
                    .collect(Collectors.toList());

            return new org.springframework.security.core.userdetails.User(
                    user.getUserEmail(), user.getUserPassword(), authorities);
        }

}
