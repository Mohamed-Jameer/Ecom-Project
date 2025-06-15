package com.example.Ecom_Project.service;

import com.example.Ecom_Project.dto.LoginDTO;
import com.example.Ecom_Project.dto.RegisterDTO;
import com.example.Ecom_Project.model.Roles;
import com.example.Ecom_Project.model.Users;
import com.example.Ecom_Project.repository.RoleRepo;
import com.example.Ecom_Project.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@CacheConfig(cacheNames = {"users", "user"})
public class UserService {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private RoleRepo roleRepo;

    @Autowired
    private JWTService jwtService;

    @Autowired
    private AuthenticationManager authManager;

    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);

    // ✅ Register user (no cache)
    public ResponseEntity<String> register(RegisterDTO registerDTO) {
        if (userRepo.existsByUserEmail(registerDTO.getUserEmail())) {
            return new ResponseEntity<>("Email already exists", HttpStatus.CONFLICT);
        }

        Users user = new Users(
                registerDTO.getUserName(),
                registerDTO.getUserEmail(),
                registerDTO.getUserPhoneNo(),
                encoder.encode(registerDTO.getUserPassword()),
                registerDTO.getUserGender(),
                registerDTO.getUserAddress(),
                setRole("User")
        );

        userRepo.save(user);
        return new ResponseEntity<>("User registered successfully", HttpStatus.OK);
    }

    // ✅ Assign role
    public Set<Roles> setRole(String roleName) {
        Set<Roles> roles = new HashSet<>();
        List<Roles> rolesList = roleRepo.findByName(roleName);
        if (rolesList.isEmpty()) {
            Roles newRole = new Roles();
            newRole.setName(roleName);
            rolesList.add(roleRepo.save(newRole));
        }
        roles.addAll(rolesList);
        return roles;
    }

    // ✅ Get all users (cache result)
    @Cacheable("users")
    public List<Users> getAllUser() {
        return userRepo.findAll();
    }

    // ✅ Login and generate JWT (no cache)
    public String verify(LoginDTO loginDTO) {
        Authentication authentication = authManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginDTO.getUserEmail(), loginDTO.getUserPassword())
        );
        return authentication.isAuthenticated()
                ? jwtService.generateToken(loginDTO.getUserEmail())
                : "Invalid credentials";
    }


    // ✅ Delete user (evict from cache)
    @CacheEvict(value = {"user", "users"}, allEntries = true)
    public List<Users> deleteUserById(int uid) {
        userRepo.deleteById(uid);
        return userRepo.findAll(); // repopulate cache on next call
    }

    // ✅ Update user details (cache update)
    @CachePut(value = "user", key = "#uid")
    @CacheEvict(value = "users", allEntries = true)
    public Users updateUserDetail(int uid, Users user) {
        Users updated = new Users(
                uid,
                user.getUserName(),
                user.getUserEmail(),
                user.getUserPhoneNo(),
                encoder.encode(user.getUserPassword()),
                user.getUserGender(),
                user.getUserAddress(),
                setRole("User")
        );
        return userRepo.save(updated);
    }

    // ✅ Admin update by ID (cache put)
    @CachePut(value = "user", key = "#uid")
    @CacheEvict(value = "users", allEntries = true)
    public Users updateAdminDetail(int uid, Users user, String role) {
        Users updated = new Users(
                uid,
                user.getUserName(),
                user.getUserEmail(),
                user.getUserPhoneNo(),
                encoder.encode(user.getUserPassword()),
                user.getUserGender(),
                user.getUserAddress(),
                setRole(role)
        );
        return userRepo.save(updated);
    }

    // ✅ Admin update without ID (cache put)
    @CachePut(value = "user", key = "#user.getUserEmail()")
    @CacheEvict(value = "users", allEntries = true)
    public Users updateAdminDetail(Users user, String role) {
        Users updated = new Users(
                user.getUserName(),
                user.getUserEmail(),
                user.getUserPhoneNo(),
                encoder.encode(user.getUserPassword()),
                user.getUserGender(),
                user.getUserAddress(),
                setRole(role)
        );
        return userRepo.save(updated);
    }


    @Cacheable(value = "user", key = "#userEmail")
    @Transactional
    public Users findByUserEmail(String userEmail) {
        return userRepo.findByUserEmailWithRoles(userEmail).orElse(null);
    }

}
