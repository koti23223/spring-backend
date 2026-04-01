package com.smartcart.service;

import java.util.List;

import com.smartcart.dto.WishlistDto;
import com.smartcart.entity.Wishlist;

public interface WishlistService {
	Wishlist addToWishlist(WishlistDto wishlistDto);

    List<Wishlist> getWishlistByEmail(String email);

    int getWishlistCount(String email);

    void removeWishlistItem(Long wishlistId);
}
