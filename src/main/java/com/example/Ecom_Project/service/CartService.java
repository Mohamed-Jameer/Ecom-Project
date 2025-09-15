package com.example.Ecom_Project.service;

import com.example.Ecom_Project.dto.CartDTO;
import com.example.Ecom_Project.dto.CartItemDTO;
import com.example.Ecom_Project.model.Cart;
import com.example.Ecom_Project.model.CartItem;
import com.example.Ecom_Project.model.Product;
import com.example.Ecom_Project.repository.CartItemRepository;
import com.example.Ecom_Project.repository.CartRepository;
import com.example.Ecom_Project.repository.ProductRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

@Service
@CacheConfig(cacheNames = {"cart"})
public class CartService {

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductRepo productRepo;

    @Transactional
    @Cacheable(key = "'allCarts'")
    public List<CartDTO> getAllCarts() {
        List<Cart> carts = cartRepository.findAll();

        return carts.stream().map(cart -> {
            List<CartItemDTO> itemDTOs = cart.getCartItems().stream()
                    .map(item -> {
                        Product product = productRepo.findById(item.getProductId()).orElse(null);
                        String productName = (product != null) ? product.getName() : "Unknown Product";
                        String brand = (product != null) ? product.getBrand() : "N/A";
                        String description = (product != null) ? product.getDescription() : "N/A";
                        String image = (product != null && product.getImageDate() != null) ? Base64.getEncoder().encodeToString(product.getImageDate()) : null;

                        return new CartItemDTO(
                                item.getCartItemId(),
                                item.getProductId(),
                                productName,
                                item.getTotalPrice() / item.getQuantity(),
                                item.getQuantity(),
                                item.getTotalPrice(),
                                brand, // ✅ Populate brand
                                description, // ✅ Populate description
                                image // ✅ Populate image
                        );
                    })
                    .toList();
            return new CartDTO(cart.getCartId(), cart.getUserId(), cart.getTotalOrderPrice(), itemDTOs);
        }).toList();
    }


    @Transactional
    @Cacheable(key = "#userId")
    public CartDTO getCartByUserId(int userId) {
        Cart cart = cartRepository.findByUserId(userId);
        if (cart == null) {
            return null;
        }
        return convertToDto(cart);
    }


    @Transactional
    @CachePut(key = "#userId")
    public CartDTO addToCart(int userId, int productId) {
        Product product = productRepo.findById(productId).orElseThrow(() -> new RuntimeException("Product not found"));

        Cart cart = cartRepository.findByUserId(userId);

        if (cart == null) {
            cart = new Cart();
            cart.setUserId(userId);
            cart.setTotalOrderPrice(0);
            cart.setCartItems(new ArrayList<>());
        }

        List<CartItem> cartItems = cart.getCartItems();
        CartItem existingCartItem = null;

        for (CartItem item : cartItems) {
            if (item.getProductId() == productId) {
                existingCartItem = item;
                break;
            }
        }

        int newQuantity = (existingCartItem != null) ? existingCartItem.getQuantity() + 1 : 1;

        if (newQuantity > product.getQuantity()) {
            throw new RuntimeException("Product is out of stock.");
        }

        if (existingCartItem != null) {
            existingCartItem.setQuantity(newQuantity);
            existingCartItem.setTotalPrice(newQuantity * product.getPrice());
        } else {
            CartItem cartItem = new CartItem();
            cartItem.setProductId(productId);
            cartItem.setQuantity(newQuantity);
            cartItem.setTotalPrice(product.getPrice());
            cartItem.setCart(cart);
            cartItems.add(cartItem);
        }

        cart.setCartItems(cartItems);
        cart.setTotalOrderPrice(cartItems.stream().mapToDouble(CartItem::getTotalPrice).sum());

        cart.setTotalOrderPrice(Math.round(cart.getTotalOrderPrice() * 100.0) / 100.0);

        cart = cartRepository.save(cart);
        return convertToDto(cart);
    }

    @Transactional
    @CachePut(key = "#userId")
    public CartDTO deleteCartItems(int userId, int pid) {
        Cart cart = cartRepository.findByUserId(userId);
        if (cart == null || cart.getCartItems() == null || cart.getCartItems().isEmpty()) {
            return convertToDto(cart);
        }

        List<CartItem> cartItems = cart.getCartItems();
        Iterator<CartItem> iterator = cartItems.iterator();
        while (iterator.hasNext()) {
            CartItem item = iterator.next();
            if (item.getProductId() == pid) {
                iterator.remove();
                cartItemRepository.delete(item);
                break;
            }
        }
        if (cartItems.isEmpty()) {
            cart.setCartItems(new ArrayList<>());
        } else {
            cart.setCartItems(cartItems);
        }
        double totalOrderPrice = cartItems.stream().mapToDouble(CartItem::getTotalPrice).sum();
        cart.setTotalOrderPrice(Math.round(totalOrderPrice * 100.0) / 100.0);
        cartRepository.save(cart);

        return convertToDto(cart);
    }

    @CacheEvict(key = "#userId")
    public String deleteCart(int userId, Long pid) {
        cartRepository.deleteById(pid);
        return "SuccessFulli Delete";
    }

    @Transactional
    @CachePut(key = "#userId")
    public CartDTO reduceQuantityt(int userId, int productId) {
        Product product = productRepo.findById(productId).orElseThrow(() -> new RuntimeException("Product not found"));

        Cart cart = cartRepository.findByUserId(userId);

        if (cart == null) {
            cart = new Cart();
            cart.setUserId(userId);
            cart.setTotalOrderPrice(0);
            cart.setCartItems(new ArrayList<>());
        }

        List<CartItem> cartItems = cart.getCartItems();
        CartItem existingCartItem = null;

        for (CartItem item : cartItems) {
            if (item.getProductId() == productId) {
                existingCartItem = item;
                break;
            }
        }

        if (existingCartItem != null) {
            existingCartItem.setQuantity(existingCartItem.getQuantity() - 1);
            if (existingCartItem.getQuantity() <= 0) {
                existingCartItem.setQuantity(1); // Keep quantity at 1 instead of removing
                existingCartItem.setTotalPrice(product.getPrice());
            } else {
                existingCartItem.setTotalPrice(existingCartItem.getQuantity() * product.getPrice());
            }
        } else {
            return convertToDto(cartRepository.save(cart));
        }

        cart.setCartItems(cartItems);
        cart.setTotalOrderPrice(cartItems.stream().mapToDouble(CartItem::getTotalPrice).sum());

        cart.setTotalOrderPrice(Math.round(cart.getTotalOrderPrice() * 100.0) / 100.0);
        cartRepository.save(cart);

        return convertToDto(cart);
    }

    // ✅ Updated helper method to populate new fields
    private CartDTO convertToDto(Cart cart) {
        if (cart == null) {
            return null;
        }

        List<CartItemDTO> itemDTOs = cart.getCartItems().stream().map(item -> {
            Product product = productRepo.findById(item.getProductId()).orElse(null);
            String productName = (product != null) ? product.getName() : "Unknown Product";
            double price = (product != null) ? product.getPrice() : item.getTotalPrice() / item.getQuantity();
            String brand = (product != null) ? product.getBrand() : "N/A";
            String description = (product != null) ? product.getDescription() : "N/A";
            String image = (product != null && product.getImageDate() != null) ? Base64.getEncoder().encodeToString(product.getImageDate()) : null;

            return new CartItemDTO(
                    item.getCartItemId(),
                    item.getProductId(),
                    productName,
                    price,
                    item.getQuantity(),
                    item.getTotalPrice(),
                    brand,
                    description,
                    image
            );
        }).toList();

        return new CartDTO(cart.getCartId(), cart.getUserId(), cart.getTotalOrderPrice(), itemDTOs);
    }
}