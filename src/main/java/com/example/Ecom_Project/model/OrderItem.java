package com.example.Ecom_Project.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class OrderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int orderItemId;
    private int productId;
    private int quantity;
    private double totalPrice;

    @ManyToOne
    @JoinColumn(name = "order_id")
    @JsonBackReference
    private Order order;

    public void setOrderItemId(int orderItemId) {
        this.orderItemId = orderItemId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public OrderItem() {
    }

    public int getOrderItemId() {
        return orderItemId;
    }

    public int getProductId() {
        return productId;
    }

    public int getQuantity() {
        return quantity;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public Order getOrder() {
        return order;
    }

    public OrderItem(int orderItemId, int productId, int quantity, double totalPrice, Order order) {
        this.orderItemId = orderItemId;
        this.productId = productId;
        this.quantity = quantity;
        this.totalPrice = totalPrice;
        this.order = order;
    }

    public OrderItem(int productId, int quantity, double totalPrice, Order order) {
        this.productId = productId;
        this.quantity = quantity;
        this.totalPrice = totalPrice;
        this.order = order;
    }

    @Override
    public String toString() {
        return "OrderItem{" +
                "orderItemId=" + orderItemId +
                ", productId=" + productId +
                ", quantity=" + quantity +
                ", totalPrice=" + totalPrice +
                ", order=" + order +
                '}';
    }
}
