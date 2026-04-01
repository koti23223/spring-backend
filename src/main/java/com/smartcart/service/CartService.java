package com.smartcart.service;

import java.util.List;

import com.smartcart.dto.CartDto;
import com.smartcart.entity.Cart;

public interface CartService {

    Cart addToCart(CartDto cartDto);

    List<Cart> getCartByEmail(String email);

    int getCartCount(String email);

    Cart updateCartQuantity(Long cartId, Integer quantity);

    void removeCartItem(Long cartId);
}