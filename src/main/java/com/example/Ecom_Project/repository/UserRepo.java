package com.example.Ecom_Project.repository;

import com.example.Ecom_Project.model.Users;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepo  extends JpaRepository<Users , Integer> {

    boolean existsByUserEmail(String userEmail);

    Users findByUserName(String username);

    Optional<Users> findByUserEmail(String userEmail);


}
