package com.example.Ecom_Project.controller;

import com.example.Ecom_Project.model.Cart;
import com.example.Ecom_Project.model.Users;
import com.example.Ecom_Project.service.CartService;
import com.example.Ecom_Project.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart")
public class CartController {

    @Autowired
    private CartService cartService;

    @Autowired
    private UserService userService;


    @GetMapping("/getAllCarts")
    public ResponseEntity<Cart> getCart(@AuthenticationPrincipal UserDetails userDetails) {
        Users user =  userService.findByUserEmail(userDetails.getUsername());
        Cart cart = cartService.getCartByUserId(user.getUserId());
        return ResponseEntity.ok(cart);
    }

//    // Add item to cart
//    @PostMapping("/{userId}/add")
//    public ResponseEntity<Cart> addItemToCart(@PathVariable int userId, @RequestBody CartItem cartItem) {
//        Cart updatedCart = cartService.addItemToCart(userId, cartItem);
//        return ResponseEntity.ok(updatedCart);
//    }
//
//    // Remove item from cart
//    @DeleteMapping("/{userId}/remove/{cartItemId}")
//    public ResponseEntity<Cart> removeItemFromCart(@PathVariable int userId, @PathVariable int cartItemId) {
//        Cart updatedCart = cartService.removeItemFromCart(userId, cartItemId);
//        return ResponseEntity.ok(updatedCart);
//    }
//
//    // Clear cart
//    @DeleteMapping("/{userId}/clear")
//    public ResponseEntity<String> clearCart(@PathVariable int userId) {
//        cartService.clearCart(userId);
//        return ResponseEntity.ok("Cart cleared successfully!");
//    }
}
