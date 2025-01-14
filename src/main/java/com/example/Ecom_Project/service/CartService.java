package com.example.Ecom_Project.service;

import com.example.Ecom_Project.model.Cart;
import com.example.Ecom_Project.repository.CartRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CartService {

    @Autowired
    private CartRepository cartRepository;

    public Cart getCartByUserId(int userId) {
     return   cartRepository.findByUserId(userId);
    }
}
