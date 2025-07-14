package com.example.Ecom_Project.controller;

import com.example.Ecom_Project.model.Order;
import com.example.Ecom_Project.model.Users;
import com.example.Ecom_Project.service.EmailService;
import com.example.Ecom_Project.service.OrderService;
import com.example.Ecom_Project.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private UserService userService;

    @Autowired
    private EmailService emailService;

    // ✅ Place Order (USER or ADMIN)
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @PostMapping("/place")
    public ResponseEntity<Order> placeOrder(@AuthenticationPrincipal UserDetails userDetails) {
        Users user = userService.findByUserEmail(userDetails.getUsername());
        Order order = orderService.placeOrder(user.getUserId());
        System.out.println("After Email");
        emailService.sendEmail(userDetails.getUsername(), "Order Confirmed", "Thanks for your purchase!");
        System.out.println("Before Email");
        return ResponseEntity.ok(order);
    }

    // ✅ Get All Orders (ADMIN only)
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/all")
    public ResponseEntity<List<Order>> getAllOrders() {
        return ResponseEntity.ok(orderService.getAllOrders());
    }

    // ✅ Get Order by ID (ADMIN only)
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{orderId}")
    public ResponseEntity<Order> getOrderById(@PathVariable int orderId) {
        return ResponseEntity.ok(orderService.getOrderById(orderId));
    }

    // ✅ Get Orders by Current Logged-in User
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @GetMapping("/my")
    public ResponseEntity<List<Order>> getMyOrders(@AuthenticationPrincipal UserDetails userDetails) {
        Users user = userService.findByUserEmail(userDetails.getUsername());
        return ResponseEntity.ok(orderService.getOrdersByUserId(user.getUserId()));
    }

    // ✅ Update Order Status (ADMIN only)
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{orderId}/status")
    public ResponseEntity<Order> updateOrderStatus(@PathVariable int orderId, @RequestParam String status) {
        return ResponseEntity.ok(orderService.updateOrderStatus(orderId, status));
    }

    // ✅ Delete Order (ADMIN only)
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{orderId}")
    public ResponseEntity<String> deleteOrder(@PathVariable int orderId) {
        orderService.deleteOrder(orderId);
        return ResponseEntity.ok("Order deleted successfully.");
    }
}
