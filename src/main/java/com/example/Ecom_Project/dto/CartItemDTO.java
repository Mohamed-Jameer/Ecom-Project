package com.example.Ecom_Project.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// ✅ Cleaned up with Lombok annotations
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartItemDTO {
    private int cartItemId;
    private int productId;
    private String productName;
    private double price; // ✅ Add price to DTO for easy calculation
    private int quantity;
    private double totalPrice;
    private String brand;
    private String description;
    private String image; // Base64 encoded image string
}