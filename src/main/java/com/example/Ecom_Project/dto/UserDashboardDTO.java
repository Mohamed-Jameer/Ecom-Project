package com.example.Ecom_Project.dto;

import lombok.Data;

import java.util.List;

@Data
public class UserDashboardDTO {
    // 1. User Information
    private int userId;
    private String userName;
    private String userEmail;
    private long userPhoneNo;
    private String userAddress;

    // 2. Shopping Cart Summary
    private List<CartItemDTO> cartItems;
    private double cartTotal;
    private int totalCartItems;

    // 3. Recent Order History
    private List<OrderSummaryDTO> recentOrders;

    // 4. Product Recommendations
    private List<ProductDTO> recommendedProducts;
}
