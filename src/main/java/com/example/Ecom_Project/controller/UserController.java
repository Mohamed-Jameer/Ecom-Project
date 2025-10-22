package com.example.Ecom_Project.controller;

import com.example.Ecom_Project.dto.LoginDTO;
import com.example.Ecom_Project.dto.RegisterDTO;
import com.example.Ecom_Project.model.Product;
import com.example.Ecom_Project.model.Users;
import com.example.Ecom_Project.repository.ProductRepo;
import com.example.Ecom_Project.service.JWTService;
import com.example.Ecom_Project.service.ProductService;
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
@RequestMapping("/api")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private ProductService productService;

    @Autowired
    private JWTService jwtService;

    @GetMapping("/home")
    public List<Product> home() {
        return productService.getFeaturedProducts();
    }

    @GetMapping("/")
    public String homePage(HttpServletRequest request) {
        String jwtToken = (String) request.getSession().getAttribute("jwtToken");
        return jwtToken != null ? jwtToken : "Token not found";
    }

    @GetMapping("/loginSuccess")
    public String loginSuccess(OAuth2AuthenticationToken authentication, Model model) {
        OAuth2User user = authentication.getPrincipal();
        String username = user.getAttribute("name");
        String jwtToken = jwtService.generateToken(username);
        model.addAttribute("jwtToken", jwtToken);
        return "redirect:http://localhost:3000/oauth2/redirect?token=" + jwtToken;    }

    // ✅ Public: Register
    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody RegisterDTO registerDTO) {
        return userService.register(registerDTO);
    }

    // ✅ Public: Login
    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody LoginDTO loginDTO) {
        String token = userService.verify(loginDTO);
        Map<String, String> response = new HashMap<>();
        response.put("token", token); // wrap token in JSON
        return ResponseEntity.ok(response);
    }

    // 🔐 ADMIN Only: Get all users
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/user/users")
    public List<Users> getAllUser() {
        return userService.getAllUser();
    }

    // 🔐 ADMIN Only: Delete user by ID
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/admin/deleteUser/{uid}")
    public List<Users> deleteUserById(@PathVariable int uid) {
        return userService.deleteUserById(uid);
    }

    // 🔐 USER or ADMIN: Update own user details
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @PutMapping("/updateUserDetail")
    public Users updateUserDetail(@AuthenticationPrincipal UserDetails userDetails,
                                  @RequestBody Users user) {
        Users userIdentity = userService.findByUserEmail(userDetails.getUsername());
        return userService.updateUserDetail(userIdentity.getUserId(), user);
    }

    // 🔐 ADMIN Only: Update admin details
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/admin/updateAdminDetail")
    public Users updateAdminDetail(@AuthenticationPrincipal UserDetails userDetails,
                                   @RequestBody Users user,
                                   @RequestParam String role) {
        Users userIdentity = userService.findByUserEmail(user.getUserEmail());
        if (userIdentity == null) {
            return userService.updateAdminDetail(user, role);
        }
        return userService.updateAdminDetail(userIdentity.getUserId(), user, role);
    }
}



