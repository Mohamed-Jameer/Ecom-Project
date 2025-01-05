package com.example.Ecom_Project.controller;

import com.example.Ecom_Project.dto.RegisterDTO;
import com.example.Ecom_Project.model.Users;
import com.example.Ecom_Project.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/User")
public class UserController {

    @Autowired
    private UserService userService;


    @PostMapping("/register")
    public Users register(@RequestBody RegisterDTO registerDTO){
        return userService.register(registerDTO);
    }

}
