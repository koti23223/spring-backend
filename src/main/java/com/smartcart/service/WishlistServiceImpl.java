package com.smartcart.service;

//import java.util.List;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import com.smartcart.dto.WishlistDto;
//import com.smartcart.entity.Product;
//import com.smartcart.entity.User;
//import com.smartcart.entity.Wishlist;
//import com.smartcart.repository.ProductRepository;
//import com.smartcart.repository.UserRepository;
//import com.smartcart.repository.WishlistRepository;
//
//@Service
//public class WishlistServiceImpl implements WishlistService {
//	
//	@Autowired
//    private WishlistRepository wishlistRepository;
//
//    @Autowired
//    private UserRepository userRepository;
//
//    @Autowired
//    private ProductRepository productRepository;
//    
//    
//	@Override
//	public Wishlist addToWishlist(WishlistDto wishlistDto) {
//		User user = userRepository.findByEmail(wishlistDto.getEmail())
//                .orElseThrow(() -> new RuntimeException("User not found"));
//
//        Product product = productRepository.findById(wishlistDto.getProductId())
//                .orElseThrow(() -> new RuntimeException("Product not found"));
//
//        Wishlist existingWishlist = wishlistRepository.findByUserAndProduct(user, product)
//                .orElse(null);
//
//        if (existingWishlist != null) {
//            throw new RuntimeException("Product already exists in wishlist");
//        }
//
//        Wishlist wishlist = new Wishlist();
//        wishlist.setUser(user);
//        wishlist.setProduct(product);
//
//        return wishlistRepository.save(wishlist);
//	}
//
//	@Override
//	public List<Wishlist> getWishlistByEmail(String email) {
//		return wishlistRepository.findByUser_Email(email);
//	}
//
//	@Override
//	public int getWishlistCount(String email) {
//		return wishlistRepository.findByUser_Email(email).size();
//	}
//
//	@Override
//	public void removeWishlistItem(Long wishlistId) {
//		wishlistRepository.deleteById(wishlistId);
//
//	}
//
//}


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.smartcart.dto.WishlistDto;
import com.smartcart.entity.Product;
import com.smartcart.entity.User;
import com.smartcart.entity.Wishlist;
import com.smartcart.repository.ProductRepository;
import com.smartcart.repository.UserRepository;
import com.smartcart.repository.WishlistRepository;

@Service
public class WishlistServiceImpl implements WishlistService {

    @Autowired
    private WishlistRepository wishlistRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;

    @Override
    public Wishlist addToWishlist(WishlistDto wishlistDto) {

        // ⭐ Find user using email
        User user = userRepository.findByEmail(wishlistDto.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        // ⭐ Find product
        Product product = productRepository.findById(wishlistDto.getProductId())
                .orElseThrow(() -> new RuntimeException("Product not found"));

        // ⭐ Check if already exists
        Wishlist existingWishlist = wishlistRepository
                .findByUserAndProduct(user, product)
                .orElse(null);

        if (existingWishlist != null) {
            throw new RuntimeException("Product already exists in wishlist");
        }

        // ⭐ Create new wishlist item
        Wishlist wishlist = new Wishlist();

        wishlist.setUser(user);

        // ⭐ VERY IMPORTANT LINE (STORE EMAIL IN TABLE)
        wishlist.setEmail(user.getEmail());

        wishlist.setProduct(product);

        return wishlistRepository.save(wishlist);
    }

    @Override
    public List<Wishlist> getWishlistByEmail(String email) {
        return wishlistRepository.findByUser_Email(email);
    }

    @Override
    public int getWishlistCount(String email) {
        return wishlistRepository.findByUser_Email(email).size();
    }

    @Override
    public void removeWishlistItem(Long wishlistId) {
        wishlistRepository.deleteById(wishlistId);
    }
}
