package com.smartcart.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.smartcart.dto.CartDto;
import com.smartcart.entity.Cart;
import com.smartcart.entity.Product;
import com.smartcart.entity.User;
import com.smartcart.repository.CartRepository;
import com.smartcart.repository.ProductRepository;
import com.smartcart.repository.UserRepository;

@Service
public class CartServiceImpl implements CartService {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;
    @Override
    public Cart addToCart(CartDto cartDto) {

        User user = userRepository.findByEmail(cartDto.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Product product = productRepository.findById(cartDto.getProductId())
                .orElseThrow(() -> new RuntimeException("Product not found"));

        Cart cart = cartRepository.findByUserAndProduct(user, product).orElse(null);

        if (cart != null) {

            cart.setQuantity(cart.getQuantity() + cartDto.getQuantity());

        } else {

            cart = new Cart();
            cart.setUser(user);

            // ⭐ VERY IMPORTANT
            cart.setEmail(user.getEmail());

            cart.setProduct(product);
            cart.setQuantity(cartDto.getQuantity());
        }

        return cartRepository.save(cart);
    }
    @Override
    public List<Cart> getCartByEmail(String email) {
        return cartRepository.findByUser_Email(email);
    }

    @Override
    public int getCartCount(String email) {
        List<Cart> cartItems = cartRepository.findByUser_Email(email);

        return cartItems.stream()
                .mapToInt(item -> item.getQuantity() == null ? 0 : item.getQuantity())
                .sum();
    }

    @Override
    public Cart updateCartQuantity(Long cartId, Integer quantity) {
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new RuntimeException("Cart item not found"));

        cart.setQuantity(quantity);
        return cartRepository.save(cart);
    }

    @Override
    public void removeCartItem(Long cartId) {
        cartRepository.deleteById(cartId);
    }
}