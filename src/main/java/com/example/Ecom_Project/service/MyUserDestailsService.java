package com.example.Ecom_Project.service;


import com.example.Ecom_Project.model.UserPrincipal;
import com.example.Ecom_Project.model.Users;
import com.example.Ecom_Project.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class MyUserDestailsService implements UserDetailsService {

    @Autowired
    private UserRepo repo;

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

        System.out.println("Load "+ correctUser.getUserEmail());
        return new UserPrincipal(correctUser);
    }
}
