package com.example.Ecom_Project.service;

import com.example.Ecom_Project.model.Cart;
import com.example.Ecom_Project.model.CartItem;
import com.example.Ecom_Project.model.Product;
import com.example.Ecom_Project.repository.CartItemRepository;
import com.example.Ecom_Project.repository.CartRepository;
import com.example.Ecom_Project.repository.ProductRepo;
import org.hibernate.boot.CacheRegionDefinition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

@Service
public class CartService {

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private ProductService productService ;

  //  @Cacheable(value = "carts")
    public  List<Cart> getAllCarts() {
        return cartRepository.findAll();
    }



   // @Cacheable(value = "cart", key = "#userId")
    public Cart getCartByUserId(int userId) {
        return  cartRepository.findByUserId(userId);
    }



    //@CachePut(value = "cart", key = "#userId")
    public Cart addToCart(int userId, int productId) {
        Product product = productService.getProductById(productId);

        Cart cart = cartRepository.findByUserId(userId);

        if (cart == null) {
            cart = new Cart(); // Assign new cart to the cart variable
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
            existingCartItem.setQuantity(existingCartItem.getQuantity() + 1);
            existingCartItem.setTotalPrice(existingCartItem.getQuantity() * product.getPrice());
        } else {
            CartItem cartItem = new CartItem();
            cartItem.setProductId(productId);
            cartItem.setQuantity(1);
            cartItem.setTotalPrice(product.getPrice());
            cartItem.setCart(cart);
            cartItems.add(cartItem);
        }

        cart.setCartItems(cartItems);
        cart.setTotalOrderPrice(cartItems.stream().mapToDouble(CartItem::getTotalPrice).sum());

        cart.setTotalOrderPrice(Math.round(cart.getTotalOrderPrice() * 100.0) / 100.0);
        return cartRepository.save(cart);
    }



    // This for delete CartItem
   // @CacheEvict(value = "cart", key = "#userId")
    @Transactional
    public Cart deleteCartItems(int userId, int pid) {
        Cart cart = getCartByUserId(userId);
        if (cart == null || cart.getCartItems() == null || cart.getCartItems().isEmpty()) {
            return cart;
        }

        List<CartItem> cartItems = cart.getCartItems();
        Iterator<CartItem> iterator = cartItems.iterator();
        boolean itemFound = false;
        while (iterator.hasNext()) {
            CartItem item = iterator.next();
            if (item.getProductId() == pid) {
                iterator.remove();
                cartItemRepository.delete(item);
                itemFound = true;
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
        return cart;
    }


  // This for Delete Cart
//  @CacheEvict(value = "cart", key = "#userId")
    public String deleteCart(int userId, Long pid) {
        cartRepository.deleteById(pid);
        return "SuccessFulli Delete";
    }

    //Reduce the Quantity
  //  @CachePut(value = "cart", key = "#userId")
    public Cart reduceQuantityt(int userId, int productId) {
        Product product = productService.getProductById(productId);

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
            if( existingCartItem.getQuantity() ==0){
               return deleteCartItems(userId,productId);
            }
            existingCartItem.setTotalPrice(existingCartItem.getQuantity() * product.getPrice());
        }else{
            return cartRepository.save(cart);
        }

        cart.setCartItems(cartItems);
        cart.setTotalOrderPrice(cartItems.stream().mapToDouble(CartItem::getTotalPrice).sum());

        cart.setTotalOrderPrice(Math.round(cart.getTotalOrderPrice() * 100.0) / 100.0);
        return cartRepository.save(cart);

    }

}
