package com.example.Ecom_Project.service;

import com.example.Ecom_Project.dto.RegisterDTO;
import com.example.Ecom_Project.model.Roles;
import com.example.Ecom_Project.model.Users;
import com.example.Ecom_Project.repository.RoleRepo;
import com.example.Ecom_Project.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class UserService {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private RoleRepo roleRepo;


    public Users register(RegisterDTO registerDTO) {

        Users user = new Users(registerDTO.getUserName(),registerDTO.getUserEmail(),registerDTO.getUserPhoneNo(),
                registerDTO.getUserPassword(),registerDTO.getUserGender(),registerDTO.getUserAddress(),setRole());


        return userRepo.save(user);
    }


    private Set<Roles> setRole() {
        Set<Roles> setRoles = new HashSet<>();

        // Fetch all roles with the name "User"
        List<Roles> rolesList = roleRepo.findByName("User");

        if (rolesList.isEmpty()) {
            // If no roles found, create and save the "User" role
            Roles role = new Roles();
            role.setName("User");
            rolesList.add(roleRepo.save(role));
        }

        // Add roles to the set (in case there are multiple roles with the same name)
        setRoles.addAll(rolesList);

        return setRoles; // Return the set of roles
    }
}
