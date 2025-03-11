package com.example.Ecom_Project.controller;

import com.example.Ecom_Project.dto.LoginDTO;
import com.example.Ecom_Project.dto.RegisterDTO;
import com.example.Ecom_Project.model.Users;
import com.example.Ecom_Project.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class UserController {

    @Autowired
    private UserService userService;


    @GetMapping("/user/users")
    public List<Users> getAllUser(){
        return userService.getAllUser();
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody RegisterDTO registerDTO){
        return userService.register(registerDTO);
    }

    @PostMapping("/login")
    public String login(@RequestBody LoginDTO loginDTO){
        System.out.println(loginDTO);
        return userService.verify(loginDTO);
    }

    @PostMapping("/admin/deleteUser/{uid}")
    public List<Users> deleteUserById(@PathVariable int uid){
        return userService.deleteUserById(uid);
    }

    @PutMapping("/updateUserDetail")
    public Users updateUserDetail(@AuthenticationPrincipal UserDetails userDetails, @RequestBody Users user){
        Users userIdentity = userService.findByUserEmail(userDetails.getUsername());
        return userService.updateUserDetail(userIdentity.getUserId(),user);
    }


    @PutMapping("/admin/updateAdminDetail")
    public Users updateAdminDetail(@AuthenticationPrincipal UserDetails userDetails, @RequestBody Users user , @RequestParam String role){
        Users userIdentity = userService.findByUserEmail(user.getUserEmail());
        if(userIdentity == null){
            return userService.updateAdminDetail(user,role);
        }
        return userService.updateAdminDetail(userIdentity.getUserId(),user,role);
    }






}


