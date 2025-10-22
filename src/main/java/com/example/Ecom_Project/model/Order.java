package com.example.Ecom_Project.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonManagedReference; // ✅ New Import

@Setter
@Getter
@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int orderId;

    // ✅ Replaced primitive userId with a relationship to the Users entity
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    @JsonBackReference("user-orders") // ✅ Unique name for this relationship
    private Users user;

    private double totalAmount;
    private LocalDateTime orderDate;
    private String status; // e.g., "PLACED", "SHIPPED", "DELIVERED"

    // ✅ Added the shipping address field using an @Embedded class
    @Embedded
    private ShippingAddress shippingAddress;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference("order-items") // ✅ Unique name for this relationship
    private List<OrderItem> orderItems;
    // Lombok's @Getter and @Setter handle all the methods below automatically.
    // Manual methods are redundant if using Lombok.
    public Order() {}

    // Add a constructor for convenience without an orderId
    public Order(Users user, double totalAmount, LocalDateTime orderDate, String status, ShippingAddress shippingAddress) {
        this.user = user;
        this.totalAmount = totalAmount;
        this.orderDate = orderDate;
        this.status = status;
        this.shippingAddress = shippingAddress;
    }
}

