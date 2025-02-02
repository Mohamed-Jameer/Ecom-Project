package com.example.Ecom_Project.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int cartId;
    private int userId;
    private double totalOrderPrice;

    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<CartItem> cartItems = new ArrayList<>();

    // Getter and Setter Methods
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

    public double getTotalOrderPrice() {
        return totalOrderPrice;
    }

    public void setTotalOrderPrice(double totalOrderPrice) {
        this.totalOrderPrice = totalOrderPrice;
    }

    public List<CartItem> getCartItems() {
        return cartItems;
    }

    public void setCartItems(List<CartItem> cartItems) {
        this.cartItems = (cartItems != null) ? cartItems : new ArrayList<>();
    }

    // Constructors
    public Cart(int cartId, int userId, double totalOrderPrice, List<CartItem> cartItems) {
        this.cartId = cartId;
        this.userId = userId;
        this.totalOrderPrice = totalOrderPrice;
        this.cartItems = cartItems;
    }

    public Cart(int userId, double totalOrderPrice, List<CartItem> cartItems) {
        this.userId = userId;
        this.totalOrderPrice = totalOrderPrice;
        this.cartItems = (cartItems != null) ? cartItems : new ArrayList<>();
    }

    public Cart() {}

    @Override
    public String toString() {
        return "Cart{" +
                "cartId=" + cartId +
                ", userId=" + userId +
                ", totalOrderPrice=" + totalOrderPrice +
                ", cartItems=" + cartItems +
                '}';
    }


}
