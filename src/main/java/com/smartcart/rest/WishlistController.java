package com.smartcart.rest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.smartcart.dto.WishlistDto;
import com.smartcart.entity.Wishlist;
import com.smartcart.service.WishlistService;

@RestController
@RequestMapping("/api/wishlist")
public class WishlistController {

    @Autowired
    private WishlistService wishlistService;

    @PostMapping("/add")
    public ResponseEntity<?> addToWishlist(@RequestBody WishlistDto wishlistDto) {
        return ResponseEntity.ok(wishlistService.addToWishlist(wishlistDto));
    }

    @GetMapping("/user/{email}")
    public List<Wishlist> getWishlistByEmail(@PathVariable String email) {
        return wishlistService.getWishlistByEmail(email);
    }

    @GetMapping("/count/{email}")
    public int getWishlistCount(@PathVariable String email) {
        return wishlistService.getWishlistCount(email);
    }

    @DeleteMapping("/remove/{wishlistId}")
    public String removeWishlistItem(@PathVariable Long wishlistId) {
        wishlistService.removeWishlistItem(wishlistId);
        return "Wishlist item removed successfully";
    }
}