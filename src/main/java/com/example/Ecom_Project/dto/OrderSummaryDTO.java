package com.example.Ecom_Project.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderSummaryDTO {
    private int orderId;
    private double totalAmount;
    private LocalDateTime orderDate;
    private String status;
    private int totalItems; // The number of items in the order
}
