package com.example.Ecom_Project.model;

import com.fasterxml.jackson.annotation.JsonBackReference; // ✅ Replaced @JsonIgnore with @JsonBackReference
import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

@Entity
@Data
public class CartItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int cartItemId;

    // A better design would be a ManyToOne relationship to the Product entity
    // @ManyToOne
    // @JoinColumn(name = "product_id")
    // @JsonBackReference("product-cartItems")
    // private Product product;
    private int productId;

    private int quantity;
    private double totalPrice;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "cart_id")
    @JsonBackReference("cart-items") // ✅ Use this to complete the pair with @JsonManagedReference
    @ToString.Exclude // Prevents a StackOverflowError in toString()
    private Cart cart;

    public CartItem() {}
}