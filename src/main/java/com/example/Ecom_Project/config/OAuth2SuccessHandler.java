package com.example.Ecom_Project.config;

import com.example.Ecom_Project.model.Roles;
import com.example.Ecom_Project.model.Users;
import com.example.Ecom_Project.repository.RoleRepo;
import com.example.Ecom_Project.repository.UserRepo;
import com.example.Ecom_Project.service.JWTService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.*;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.*;
@Component
@RequiredArgsConstructor
public class OAuth2SuccessHandler implements AuthenticationSuccessHandler {

    private final OAuth2AuthorizedClientService authorizedClientService;
    private final UserRepo userRepository;
    private final RoleRepo roleRepo;
    private final GooglePeopleApiService googlePeopleApiService;
    private final JWTService jwtService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();

        String clientRegistrationId = "google"; // or dynamic from auth.getDetails() if multiple providers
        String principalName = oAuth2User.getName();

        OAuth2AuthorizedClient client = authorizedClientService.loadAuthorizedClient(
                clientRegistrationId,
                principalName
        );

        if (client == null || client.getAccessToken() == null) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "OAuth2 client not authorized");
            return;
        }

        String accessToken = client.getAccessToken().getTokenValue();
        String email = oAuth2User.getAttribute("email");

        if (email == null || email.isEmpty()) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Email not found in user profile");
            return;
        }

        Users user = userRepository.findByUserEmail(email).orElse(null);
        if (user == null) {
            // Fetch more details using People API
            Map<String, String> details = googlePeopleApiService.fetchExtraDetails(accessToken);
            String name = details.get("name");
            String gender = details.get("gender");
            String phone = details.get("phone");

            System.out.println(phone);
            String address = details.get("address");

            long phoneNumber = 0L;
            if (phone != null && !phone.trim().isEmpty()) {
                try {
                    phoneNumber = Long.parseLong(phone);
                } catch (NumberFormatException e) {
                    // Optional: log this
                    phoneNumber = 0L;
                }
            }

            user = new Users();
            user.setUserEmail(email);
            user.setUserName(name);
            user.setUserGender(gender);
            user.setUserPhoneNo(phoneNumber);
            user.setUserAddress(address);
            user.setUserPassword("");
            user.setRoles(setRole("User"));

            userRepository.save(user);
        }

        System.out.println("onAuthenticationSuccess");

        String jwtToken = jwtService.generateToken(email);

        System.out.println(jwtToken);

//        Map<String, Object> body = new HashMap<>();
//        body.put("token", jwtToken);
//        body.put("user", user);
//
//        response.setContentType("application/json");
//        new ObjectMapper().writeValue(response.getOutputStream(), body);
        // Add token to response header or cookie if needed
        response.setHeader("Authorization", "Bearer " + jwtToken);

        // Option 1: Store in session
        request.getSession().setAttribute("jwtToken", jwtToken);

        response.sendRedirect("http://localhost:3000/oauth2/redirect?token=" + jwtToken);
    }

    private String extractGender(Map<String, Object> data) {
        List<Map<String, Object>> genders = (List<Map<String, Object>>) data.get("genders");
        if (genders != null && !genders.isEmpty()) {
            return (String) genders.get(0).get("value");
        }
        return "unknown";
    }

    private String extractPhoneNumber(Map<String, Object> data) {
        List<Map<String, Object>> phones = (List<Map<String, Object>>) data.get("phoneNumbers");
        if (phones != null && !phones.isEmpty()) {
            return (String) phones.get(0).get("value");
        }
        return "0";
    }

    private String extractAddress(Map<String, Object> data) {
        List<Map<String, Object>> addresses = (List<Map<String, Object>>) data.get("addresses");
        if (addresses != null && !addresses.isEmpty()) {
            return (String) addresses.get(0).get("formattedValue");
        }
        return "N/A";
    }

    public Set<Roles> setRole(String roles) {
        Set<Roles> setRoles = new HashSet<>();


        List<Roles> rolesList = roleRepo.findByName(roles);
        if (rolesList.isEmpty()) {
            Roles role = new Roles();
            role.setName(roles);
            rolesList.add(roleRepo.save(role));
        }
        setRoles.addAll(rolesList);
        return setRoles;
    }
}
