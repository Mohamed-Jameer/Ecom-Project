package com.example.Ecom_Project.service;

import com.example.Ecom_Project.dto.CartItemDTO;
import com.example.Ecom_Project.dto.OrderSummaryDTO;
import com.example.Ecom_Project.dto.ProductDTO;
import com.example.Ecom_Project.dto.UserDashboardDTO;
import com.example.Ecom_Project.model.*;
import com.example.Ecom_Project.repository.CartRepository;
import com.example.Ecom_Project.repository.OrderRepository;
import com.example.Ecom_Project.repository.ProductRepo;
import com.example.Ecom_Project.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class DashboardService {

    @Autowired
    private UserRepo userRepository;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private CartRepository cartRepository; // You'll need to create this
    @Autowired
    private ProductRepo productRepository;

    public UserDashboardDTO getDashboardData(String userEmail) {
        Users user = userRepository.findByUserEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        UserDashboardDTO dashboardDTO = new UserDashboardDTO();

        // Populate User Info
        dashboardDTO.setUserId(user.getUserId());
        dashboardDTO.setUserName(user.getUserName());
        dashboardDTO.setUserEmail(user.getUserEmail());
        dashboardDTO.setUserPhoneNo(user.getUserPhoneNo());
        dashboardDTO.setUserAddress(user.getUserAddress());

        Cart userCart = cartRepository.findByUserId(user.getUserId());
        if (userCart != null) {
           // Cart cart = userCart.get();
            List<CartItem> cartItems = userCart.getCartItems();

            // Map cart items to DTOs and calculate total
            List<CartItemDTO> cartItemDTOs = cartItems.stream()
                    .map(this::convertToCartItemDTO)
                    .collect(Collectors.toList());

            dashboardDTO.setCartItems(cartItemDTOs);
            dashboardDTO.setTotalCartItems(cartItemDTOs.size());

            double total = cartItemDTOs.stream().mapToDouble(CartItemDTO::getTotalPrice).sum();
            dashboardDTO.setCartTotal(total);

        } else {
            dashboardDTO.setCartItems(List.of());
            dashboardDTO.setTotalCartItems(0);
            dashboardDTO.setCartTotal(0.0);
        }
        // Populate Recent Orders
        List<Order> recentOrders = orderRepository.findTop5ByUserUserIdOrderByOrderDateDesc(user.getUserId());
        dashboardDTO.setRecentOrders(recentOrders.stream()
                .map(this::convertToOrderSummaryDTO)
                .collect(Collectors.toList()));

        // Populate Recommended Products (you can use a simple example for now)
        List<Product> recommendedProducts = productRepository.findTop5ByOrderByReleaseDateDesc();
        dashboardDTO.setRecommendedProducts(recommendedProducts.stream()
                .map(this::convertToProductDTO)
                .collect(Collectors.toList()));

        return dashboardDTO;
    }

    // Helper methods to convert models to DTOs
    private OrderSummaryDTO convertToOrderSummaryDTO(Order order) {
        OrderSummaryDTO dto = new OrderSummaryDTO();
        dto.setOrderId(order.getOrderId());
        dto.setTotalAmount(order.getTotalAmount());
        dto.setOrderDate(order.getOrderDate());
        dto.setStatus(order.getStatus());
        dto.setTotalItems(order.getOrderItems().size());
        return dto;
    }

    private ProductDTO convertToProductDTO(Product product) {
        ProductDTO dto = new ProductDTO();
        dto.setId(product.getId());
        dto.setName(product.getName());
        dto.setDescription(product.getDescription());
        dto.setBrand(product.getBrand());
        dto.setPrice(product.getPrice());
        dto.setCategory(product.getCategory());

        return dto;
    }

    private CartItemDTO convertToCartItemDTO(CartItem cartItem) {
        CartItemDTO dto = new CartItemDTO();
        dto.setCartItemId(cartItem.getCartItemId());
        dto.setProductId(cartItem.getProductId());
        dto.setQuantity(cartItem.getQuantity());
        dto.setTotalPrice(cartItem.getTotalPrice());

        // Fetch product name from the database using productId
        Optional<Product> product = productRepository.findById(cartItem.getProductId());
        product.ifPresent(p -> dto.setProductName(p.getName()));

        return dto;
    }

}
