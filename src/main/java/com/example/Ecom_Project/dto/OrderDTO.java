package com.example.Ecom_Project.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderDTO {
    private int orderId;
    private int userId;
    private double totalAmount;
    private LocalDateTime orderDate;
    private String status;
    private List<OrderItemDTO> orderItems;
}