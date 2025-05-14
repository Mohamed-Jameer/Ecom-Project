package com.example.Ecom_Project.service;

import com.example.Ecom_Project.dto.LoginDTO;
import com.example.Ecom_Project.dto.RegisterDTO;
import com.example.Ecom_Project.model.Roles;
import com.example.Ecom_Project.model.Users;
import com.example.Ecom_Project.repository.RoleRepo;
import com.example.Ecom_Project.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

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
                encoder.encode(registerDTO.getUserPassword()),registerDTO.getUserGender(),registerDTO.getUserAddress(),setRole("User"));

 userRepo.save(user);
        return new ResponseEntity<>("SuccessFull Added", HttpStatus.OK);
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

   // @Cacheable(value = "users")
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

    //@Cacheable(value = "user", key = "#userEmail")
    public Users findByUserEmail(String userEmail){
        Optional<Users> user = userRepo.findByUserEmail(userEmail);
        Users correctUser = null;
        if (user.isPresent()) {
            correctUser = user.get();
        }

        return correctUser;
    }

  //  @CacheEvict(value = "users", allEntries = true)
    public List<Users> deleteUserById(int uid) {
        userRepo.deleteById(uid);
        return userRepo.findAll();
    }

  //  @CachePut(value = "user", key = "#uid")
    public Users updateUserDetail(int uid , Users user) {
        Users userDetails = new Users(uid, user.getUserName(),user.getUserEmail(),user.getUserPhoneNo(),
                encoder.encode(user.getUserPassword()),user.getUserGender(),user.getUserAddress(),setRole("User"));
        System.out.println(userDetails);
        return  userRepo.save(userDetails);
    }

 //   @CachePut(value = "user", key = "#uid")
    public Users updateAdminDetail(int uid , Users user,String role) {
        Users userUpdateDetails = new Users(uid, user.getUserName(),user.getUserEmail(),user.getUserPhoneNo(),
                encoder.encode(user.getUserPassword()),user.getUserGender(),user.getUserAddress(),setRole(role) );
        return  userRepo.save(userUpdateDetails);
    }

   // @CachePut(value = "user", key = "#user.getUserEmail()")
    public Users updateAdminDetail( Users user,String role) {
        Users userUpdateDetails = new Users(user.getUserName(),user.getUserEmail(),user.getUserPhoneNo(),
                encoder.encode(user.getUserPassword()),user.getUserGender(),user.getUserAddress(),setRole(role) );
        return  userRepo.save(userUpdateDetails);
    }
}
