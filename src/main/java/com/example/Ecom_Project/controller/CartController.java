package com.example.Ecom_Project.controller;

import com.example.Ecom_Project.model.Cart;
import com.example.Ecom_Project.model.CartItem;
import com.example.Ecom_Project.model.Users;
import com.example.Ecom_Project.service.CartService;
import com.example.Ecom_Project.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class CartController {

    @Autowired
    private CartService cartService;

    @Autowired
    private UserService userService;


    @GetMapping("/getAllCarts")
    public List<Cart> getAllCarts(){
         return cartService.getAllCarts();
    }

    @GetMapping("/getCarts")
    public ResponseEntity<Cart> getCart(@AuthenticationPrincipal UserDetails userDetails) {
        Users user =  userService.findByUserEmail(userDetails.getUsername());
        Cart cart = cartService.getCartByUserId(user.getUserId());
        return ResponseEntity.ok(cart);
    }


    @PostMapping("/addToCart/{pid}")
    public Cart addToCart(@AuthenticationPrincipal UserDetails userDetails, @PathVariable int pid) {
        Users user = userService.findByUserEmail(userDetails.getUsername());
        return cartService.addToCart(user.getUserId(), pid);
    }

    @PostMapping("/reduceQuantity/{pid}")
    public Cart reduceQuantity(@AuthenticationPrincipal UserDetails userDetails, @PathVariable int pid){
        Users user = userService.findByUserEmail(userDetails.getUsername());
        return cartService.reduceQuantityt(user.getUserId(), pid);
    }

    @DeleteMapping("/deleteCartItem/{pid}")
    public Cart deleteCartItem(@AuthenticationPrincipal UserDetails userDetails, @PathVariable int pid){
            Users user = userService.findByUserEmail(userDetails.getUsername());
            return cartService.deleteCartItems(user.getUserId(), pid);
        }

    @DeleteMapping("/deleteCart/{pid}")
    public String deleteCart(@AuthenticationPrincipal UserDetails userDetails, @PathVariable Long pid){
        Users user = userService.findByUserEmail(userDetails.getUsername());
        return cartService.deleteCart(user.getUserId(), pid);
    }

}







