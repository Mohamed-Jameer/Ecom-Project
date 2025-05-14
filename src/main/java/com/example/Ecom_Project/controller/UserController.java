package com.example.Ecom_Project.controller;

import com.example.Ecom_Project.dto.LoginDTO;
import com.example.Ecom_Project.dto.RegisterDTO;
import com.example.Ecom_Project.model.Users;
import com.example.Ecom_Project.service.JWTService;
import com.example.Ecom_Project.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private JWTService jwtService;

//    @GetMapping("/home")
//    public Map<String, String> homePage(HttpServletRequest request) {
//        String jwtToken = (String) request.getSession().getAttribute("jwtToken");
//
//        System.out.println("JWT"+" "+ jwtToken);
//
//        Map<String, String> response = new HashMap<>();
//        response.put("token", jwtToken != null ? jwtToken : "Token not found");
//        return response;
//
//    }

    //  http://localhost:8080/swagger-ui/index.html

    @GetMapping("/home")
    public String homePage(HttpServletRequest request) {
        String jwtToken = (String) request.getSession().getAttribute("jwtToken");

        System.out.println("JWT"+" "+ jwtToken);

        Map<String, String> response = new HashMap<>();
        response.put("token", jwtToken != null ? jwtToken : "Token not found");
        return jwtToken;
    }

    @GetMapping("/")
    public String home() {
        return "Welcome to Home!";
    }

//    @GetMapping("/userDetails")
//    public String getUserDetails(Model model, OAuth2AuthenticationToken authentication) {
//        OAuth2User user = authentication.getPrincipal();
//
//        // Retrieve user information
//        String name = user.getAttribute("name");
//        String email = user.getAttribute("email");
//        String picture = user.getAttribute("picture");
//
//        // Add to model to display in the view
//        model.addAttribute("name", name);
//        model.addAttribute("email", email);
//        model.addAttribute("picture", picture);
//
//        return "userDetails"; // View name
//    }

    @GetMapping("/loginSuccess")
    public String loginSuccess(OAuth2AuthenticationToken authentication, Model model) {

        OAuth2User user = authentication.getPrincipal();
        String username = user.getAttribute("name");

        // Generate JWT token
        String jwtToken = jwtService.generateToken(username);

        // Add JWT token to the model
        model.addAttribute("jwtToken", jwtToken);

        return "jwtTokenPage";  // Name of your view
    }


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


