package com.example.Ecom_Project.service;


import com.example.Ecom_Project.dto.PlaceOrderRequest; // ✅ Import the DTO
import com.example.Ecom_Project.model.*; // ✅ Import the models
import com.example.Ecom_Project.repository.CartRepository;
import com.example.Ecom_Project.repository.OrderRepository;
import com.example.Ecom_Project.repository.UserRepo; // ✅ Import User Repository if needed
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class OrderService {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ProductService productService;

    @Autowired
    private EmailService emailService; // ✅ Inject EmailService here

    @Autowired
    private UserRepo userRepository; // ✅ Inject UserRepository to find User by ID if needed

    @Transactional
    // ✅ Updated method to accept the User and the request DTO
    public Order placeOrder(Users user, PlaceOrderRequest placeOrderRequest) {
        Cart cart = cartRepository.findByUserId(user.getUserId());

        if (cart == null || cart.getCartItems().isEmpty()) {
            throw new RuntimeException("Cart is empty. Cannot place order.");
        }

        // Create a new Order
        Order order = new Order();
        order.setUser(user);
        order.setOrderDate(LocalDateTime.now());
        order.setStatus("PLACED");

        // Set shipping address
        ShippingAddress shippingAddress = new ShippingAddress();
        shippingAddress.setFullName(placeOrderRequest.getFullName());
        shippingAddress.setAddressLine1(placeOrderRequest.getAddressLine1());
        shippingAddress.setAddressLine2(placeOrderRequest.getAddressLine2());
        shippingAddress.setCity(placeOrderRequest.getCity());
        shippingAddress.setState(placeOrderRequest.getState());
        shippingAddress.setPostalCode(placeOrderRequest.getPostalCode());
        shippingAddress.setCountry(placeOrderRequest.getCountry());
        order.setShippingAddress(shippingAddress);

        // Calculate total amount from cart items
        double totalAmount = 0;
        List<OrderItem> orderItems = new ArrayList<>();
        for (CartItem cartItem : cart.getCartItems()) {
            OrderItem orderItem = new OrderItem();
            orderItem.setProduct(productService.getProductById(cartItem.getProductId()).toEntity());
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setTotalPrice(cartItem.getTotalPrice());
            orderItem.setOrder(order);
            orderItems.add(orderItem);

            totalAmount += cartItem.getTotalPrice(); // sum total
        }
        order.setOrderItems(orderItems);
        order.setTotalAmount(totalAmount);

        // Save order
        orderRepository.save(order);

        // Clear cart
        cart.getCartItems().clear();
        cart.setTotalOrderPrice(0.0);
        cartRepository.save(cart);

        return order;
    }

    // ✅ READ Orders
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    public Order getOrderById(int orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));
    }

    // ✅ Updated method to find by the User object instead of the primitive ID
    public List<Order> getOrdersByUser(Users user) {
        List<Order> orders = orderRepository.findByUser(user);
        // Force initialization of the lazy-loaded user object for each order
        for (Order order : orders) {
            if (order.getUser() != null) {
                // Accessing the field forces Hibernate to load the data from the proxy
                // The object will be initialized before returning from the service
                order.getUser().getUserId();
            }
        }
        return orders;    }

    // ✅ UPDATE Order Status
    public Order updateOrderStatus(int orderId, String status) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        order.setStatus(status);
        return orderRepository.save(order);
    }

    // ✅ DELETE Order
    public void deleteOrder(int orderId) {
        Optional<Order> order = orderRepository.findById(orderId);
        if (order.isPresent()) {
            orderRepository.deleteById(orderId);
        } else {
            throw new RuntimeException("Order not found");
        }
    }
}