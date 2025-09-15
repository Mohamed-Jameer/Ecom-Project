package com.example.Ecom_Project.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class CartItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int cartItemId;
    private int productId;
    private int quantity;
    private double totalPrice;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "cart_id")
    @JsonIgnore
    private Cart cart;


    // Getter and Setter Methods
    public int getCartItemId() {
        return cartItemId;
    }

    public void setCartItemId(int cartItemId) {
        this.cartItemId = cartItemId;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public Cart getCart() {
        return cart;
    }

    public void setCart(Cart cart) {
        this.cart = cart;
    }

    public CartItem(int cartItemId, int productId, int quantity, double totalPrice, Cart cart) {
        this.cartItemId = cartItemId;
        this.productId = productId;
        this.quantity = quantity;
        this.totalPrice = totalPrice;
        this.cart = cart;
    }

    public CartItem(int productId, int quantity, double totalPrice, Cart cart) {
        this.productId = productId;
        this.quantity = quantity;
        this.totalPrice = totalPrice;
        this.cart = cart;
    }

    public CartItem() {}

    @Override
    public String toString() {
        return "CartItem{" +
                "cartItemId=" + cartItemId +
                ", productId=" + productId +
                ", quantity=" + quantity +
                ", totalPrice=" + totalPrice +
                '}';
    }
}
