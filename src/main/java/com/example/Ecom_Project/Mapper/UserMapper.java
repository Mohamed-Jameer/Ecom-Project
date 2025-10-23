package com.example.Ecom_Project.Mapper;

import com.example.Ecom_Project.dto.UserDTO;
import com.example.Ecom_Project.model.Users;

public class UserMapper {

    public UserDTO convertToDTO(Users user) {
        UserDTO dto = new UserDTO();
        dto.setUserId(user.getUserId());
        dto.setUserName(user.getUserName());
        dto.setUserEmail(user.getUserEmail());
        dto.setUserPhoneNo(user.getUserPhoneNo());
        dto.setUserGender(user.getUserGender());
        dto.setUserAddress(user.getUserAddress());
        dto.setRoles(user.getRoles().stream()
                .map(role -> role.getName())
                .collect(java.util.stream.Collectors.toSet()));
        return dto;
    }

}
