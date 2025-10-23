package com.example.Ecom_Project.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

// ✅ Cleaned up with Lombok annotations
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartDTO {
    private int cartId;
    private int userId;
    private double totalOrderPrice;
    private List<CartItemDTO> cartItems;
}