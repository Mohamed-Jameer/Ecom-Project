package com.example.Ecom_Project.config;

import com.example.Ecom_Project.model.Roles;
import com.example.Ecom_Project.model.Users;
import com.example.Ecom_Project.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    @Autowired
    private GooglePeopleApiService googlePeopleApiService;

    @Autowired
    private UserRepo userRepo;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) {
        System.out.println("1. CustomOAuth2UserService - loadUser called");
        System.out.println("2. Client Registration: " + userRequest.getClientRegistration().getRegistrationId());

        OAuth2User oAuth2User = super.loadUser(userRequest);
        System.out.println("3. OAuth2User attributes: " + oAuth2User.getAttributes());

        try {
            String accessToken = userRequest.getAccessToken().getTokenValue();
            Map<String, String> details = googlePeopleApiService.fetchExtraDetails(accessToken);

            String email = details.get("email");
            if (email == null) {
                email = oAuth2User.getAttribute("email");
                if (email == null) {
                    throw new RuntimeException("Email not found from OAuth2 provider");
                }
            }

            Optional<Users> existingUserOpt = userRepo.findByUserEmail(email);
            Users user;

            if (existingUserOpt.isEmpty()) {
                user = new Users();
                user.setUserEmail(email);
                user.setUserName(details.getOrDefault("name", oAuth2User.getAttribute("name")));
                user.setUserGender(details.getOrDefault("gender", "Not Specified"));

                String phone = details.get("phone");
                if (phone != null && !phone.isBlank()) {
                    try {
                        user.setUserPhoneNo(Long.parseLong(phone.replaceAll("[^0-9]", "")));
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid phone number format: " + phone);
                    }
                }

                user.setUserAddress(details.getOrDefault("address", "Unknown"));
                user.setUserPassword("OAUTH");

                Roles role = new Roles();
                role.setName("USER");
                user.setRoles(Set.of(role));

                userRepo.save(user);
            } else {
                user = existingUserOpt.get();
            }

            // Build authorities from user roles
            Set<SimpleGrantedAuthority> authorities = user.getRoles().stream()
                    .map(role -> new SimpleGrantedAuthority("ROLE_" + role.getName()))
                    .collect(Collectors.toSet());

            // Combine OAuth attributes with our custom attributes
            Map<String, Object> combinedAttributes = new HashMap<>(oAuth2User.getAttributes());
            combinedAttributes.put("id", user.getUserId());
            combinedAttributes.put("email", user.getUserEmail());
            combinedAttributes.put("name", user.getUserName());

            return new DefaultOAuth2User(
                    authorities,
                    combinedAttributes,
                    "email" // name attribute key
            );

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("OAuth2 authentication failed", e);
        }
    }

}
