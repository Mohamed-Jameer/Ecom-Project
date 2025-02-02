package com.example.Ecom_Project.repository;

import com.example.Ecom_Project.model.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartItemRepository extends JpaRepository<CartItem, Integer> {

}
