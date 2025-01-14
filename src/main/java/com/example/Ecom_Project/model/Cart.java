package com.example.Ecom_Project.model;

import jakarta.annotation.Generated;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int cartId;
    private int userId;
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "cartId", referencedColumnName = "cartId")
    private List<CartItem> cartItems = new ArrayList<>();

    public int getCartId() {
        return cartId;
    }

    public void setCartId(int cartId) {
        this.cartId = cartId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public List<CartItem> getCartItems() {
        return cartItems;
    }

    public void setCartItems(List<CartItem> cartItems) {
        this.cartItems = cartItems;
    }

    public Cart(int cartId, int userId, List<CartItem> cartItems) {
        this.cartId = cartId;
        this.userId = userId;
        this.cartItems = cartItems;
    }

    public Cart() {
    }
}
