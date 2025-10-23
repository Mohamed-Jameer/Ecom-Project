package com.example.Ecom_Project.controller;


import com.example.Ecom_Project.dto.PlaceOrderRequest; // ✅ Import the new DTO
import com.example.Ecom_Project.model.Order;
import com.example.Ecom_Project.model.Users;
import com.example.Ecom_Project.service.OrderService;
import com.example.Ecom_Project.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus; // ✅ Import HttpStatus
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private UserService userService;

    // ✅ Place Order (USER or ADMIN) - Updated to handle the full checkout flow
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @PostMapping("/place")
    // ✅ Accepts the PlaceOrderRequest DTO from the frontend
    public ResponseEntity<?> placeOrder(@AuthenticationPrincipal UserDetails userDetails, @RequestBody PlaceOrderRequest placeOrderRequest) {
        try {
            Users user = userService.findByUserEmail(userDetails.getUsername());

            // ✅ Pass the user object and the shipping request to the service layer
            Order order = orderService.placeOrder(user, placeOrderRequest);

            return ResponseEntity.ok(order);
        } catch (Exception e) {
            // ✅ Return a more specific error response
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
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
        // ✅ The service method should now take the full User object
        return ResponseEntity.ok(orderService.getOrdersByUser(user));
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