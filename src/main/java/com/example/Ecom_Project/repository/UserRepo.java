package com.example.Ecom_Project.repository;

import com.example.Ecom_Project.model.Users;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepo  extends JpaRepository<Users , Integer> {

    boolean existsByUserEmail(String userEmail);

    Users findByUserName(String username);

    Optional<Users> findByUserEmail(String userEmail);

    // ✅ Fetch user with roles using JOIN FETCH to avoid LazyInitializationException
    @Query("SELECT u FROM Users u LEFT JOIN FETCH u.roles WHERE u.userEmail = :email")
    Optional<Users> findByUserEmailWithRoles(@Param("email") String email);
}
