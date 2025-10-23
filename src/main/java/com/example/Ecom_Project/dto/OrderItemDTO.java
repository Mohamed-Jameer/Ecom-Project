package com.example.Ecom_Project.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderItemDTO {
    private int productId;
    private int quantity;
    private double totalPrice;
    private int orderItemId;



}
