package com.example.Ecom_Project.service;

import com.example.Ecom_Project.dto.LoginDTO;
import com.example.Ecom_Project.dto.RegisterDTO;
import com.example.Ecom_Project.model.Roles;
import com.example.Ecom_Project.model.Users;
import com.example.Ecom_Project.repository.RoleRepo;
import com.example.Ecom_Project.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class UserService {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private RoleRepo roleRepo;

    @Autowired
    private JWTService jwtService;

    @Autowired
    AuthenticationManager authManager;

    private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);

    public ResponseEntity<String> register(RegisterDTO registerDTO) {

        if(userRepo.existsByUserEmail(registerDTO.getUserEmail())){
          return new ResponseEntity<>("Email already exists", HttpStatus.CONFLICT);
        }

        Users user = new Users(registerDTO.getUserName(),registerDTO.getUserEmail(),registerDTO.getUserPhoneNo(),
                encoder.encode(registerDTO.getUserPassword()),registerDTO.getUserGender(),registerDTO.getUserAddress(),setRole());

 userRepo.save(user);
        return new ResponseEntity<>("SuccessFull Added", HttpStatus.OK);
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

    public List<Users> getAllUser() {
        return  userRepo.findAll();
    }

    public String verify(LoginDTO loginDTO) {
        Authentication authentication =
                authManager.authenticate(new UsernamePasswordAuthenticationToken(loginDTO.getUserEmail(),loginDTO.getUserPassword()));
        if(authentication.isAuthenticated()){
            return  jwtService.generateToken(loginDTO.getUserEmail());
        }

        return  "Failed";
    }

    public Users findByUserEmail(String userEmail){
        Optional<Users> user = userRepo.findByUserEmail(userEmail);
        Users correctUser = null;
        if (user.isPresent()) {
            correctUser = user.get();
        }

        return correctUser;
    }


}
