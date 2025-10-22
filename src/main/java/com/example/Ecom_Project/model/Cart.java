package com.example.Ecom_Project.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor // Lombok's default constructor
@AllArgsConstructor // Lombok's all-args constructor
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int cartId;

    // A better design would be a ManyToOne relationship to the Users entity
    // @OneToOne
    // @JoinColumn(name = "user_id")
    // private Users user;
    private int userId;

    private double totalOrderPrice;

    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JsonManagedReference("cart-items") // ✅ Matches the @JsonBackReference in CartItem
    @ToString.Exclude // Prevents a StackOverflowError in toString()
    private List<CartItem> cartItems = new ArrayList<>();
}