package com.example.Ecom_Project.repository;

import com.example.Ecom_Project.model.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {
    Cart findByUserId(int userId);
}