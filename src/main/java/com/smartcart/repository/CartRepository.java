package com.smartcart.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.smartcart.entity.Cart;
import com.smartcart.entity.Product;
import com.smartcart.entity.User;

public interface CartRepository extends JpaRepository<Cart, Long> {

    List<Cart> findByUser_Email(String email);

    Optional<Cart> findByUserAndProduct(User user, Product product);

    // ⭐ ADD THIS
    void deleteByProduct_Id(Long productId);
    @Modifying
    @Query("DELETE FROM Cart c WHERE c.product.id = :productId")
    void deleteCartByProductId(@Param("productId") Long productId);
}