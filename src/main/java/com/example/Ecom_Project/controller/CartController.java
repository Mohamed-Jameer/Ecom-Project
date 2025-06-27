package com.example.Ecom_Project.controller;

import com.example.Ecom_Project.dto.CartDTO;
import com.example.Ecom_Project.model.Cart;
import com.example.Ecom_Project.model.Users;
import com.example.Ecom_Project.service.CartService;
import com.example.Ecom_Project.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cart")
public class CartController {

    private final CartService cartService;
    private final UserService userService;

    public CartController(CartService cartService, UserService userService) {
        this.cartService = cartService;
        this.userService = userService;
    }


    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/all")
    public ResponseEntity<List<CartDTO>> getAllCarts() {

        return ResponseEntity.ok(cartService.getAllCarts());
    }


    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @GetMapping
    public ResponseEntity<Cart> getUserCart(@AuthenticationPrincipal UserDetails userDetails) {
        // Now uses JOIN FETCH method
        Users user = userService.findByUserEmail(userDetails.getUsername());

        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        Cart cart = cartService.getCartByUserId(user.getUserId());
        return ResponseEntity.ok(cart);
    }

    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @PostMapping("/add/{productId}")
    public ResponseEntity<Cart> addToCart(@AuthenticationPrincipal UserDetails userDetails,
                                          @PathVariable int productId) {
        Users user = userService.findByUserEmail(userDetails.getUsername());
        return ResponseEntity.ok(cartService.addToCart(user.getUserId(), productId));
    }

    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @PostMapping("/reduce/{productId}")
    public ResponseEntity<Cart> reduceQuantity(@AuthenticationPrincipal UserDetails userDetails,
                                               @PathVariable int productId) {
        Users user = userService.findByUserEmail(userDetails.getUsername());
        return ResponseEntity.ok(cartService.reduceQuantityt(user.getUserId(), productId));
    }

    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @DeleteMapping("/item/{productId}")
    public ResponseEntity<Cart> deleteCartItem(@AuthenticationPrincipal UserDetails userDetails,
                                               @PathVariable int productId) {
        Users user = userService.findByUserEmail(userDetails.getUsername());
        return ResponseEntity.ok(cartService.deleteCartItems(user.getUserId(), productId));
    }

    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @DeleteMapping("/{cartId}")
    public ResponseEntity<String> deleteCart(@AuthenticationPrincipal UserDetails userDetails,
                                             @PathVariable Long cartId) {
        Users user = userService.findByUserEmail(userDetails.getUsername());
        return ResponseEntity.ok(cartService.deleteCart(user.getUserId(), cartId));
    }
}
