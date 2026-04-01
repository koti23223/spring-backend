package com.smartcart.rest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.smartcart.dto.CartDto;
import com.smartcart.dto.QuantityDto;
import com.smartcart.entity.Cart;
import com.smartcart.service.CartService;

@RestController
@RequestMapping("/api/cart")
public class CartController {

    @Autowired
    private CartService cartService;

    @PostMapping("/add")
    public ResponseEntity<?> addToCart(@RequestBody CartDto cartDto) {
        return ResponseEntity.ok(cartService.addToCart(cartDto));
    }

    @GetMapping("/user/{email}")
    public List<Cart> getCartByEmail(@PathVariable String email) {
        return cartService.getCartByEmail(email);
    }

    @GetMapping("/count/{email}")
    public int getCartCount(@PathVariable String email) {
        return cartService.getCartCount(email);
    }

    @PutMapping("/update/{cartId}")
    public Cart updateCartQuantity(@PathVariable Long cartId, @RequestBody QuantityDto quantityDto) {
        return cartService.updateCartQuantity(cartId, quantityDto.getQuantity());
    }

    @DeleteMapping("/remove/{cartId}")
    public String removeCartItem(@PathVariable Long cartId) {
        cartService.removeCartItem(cartId);
        return "Cart item removed successfully";
    }
}