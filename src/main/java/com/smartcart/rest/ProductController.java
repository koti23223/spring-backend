package com.smartcart.rest;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.smartcart.entity.Product;
import com.smartcart.repository.CartRepository;
import com.smartcart.repository.ProductRepository;
import com.smartcart.repository.WishlistRepository;
import com.smartcart.service.ProductCacheService;

import jakarta.transaction.Transactional;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    @Autowired
    private ProductRepository repo;

    @Autowired
    private WishlistRepository wishlistRepo;

    @Autowired
    private CartRepository cartRepo;

    @Autowired
    private ProductCacheService cacheService;

    @GetMapping
    public List<Product> getAll(){
        return repo.findAll();
    }

    @GetMapping("/{id}")
    public Product getById(@PathVariable Long id){

        return repo
                .findById(id)
                .orElseThrow(() ->
                    new RuntimeException("Product not found with id " + id)
                );

    }

    @PostMapping("/add")
    public Product add(@RequestBody Product p){

        return repo.save(p);

    }

    @PutMapping("/update/{id}")
    public Product update(

            @PathVariable Long id,
            @RequestBody Product p){

        Product old =
                repo.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Product not found")
                );

        old.setTitle(p.getTitle());
        old.setPrice(p.getPrice());
        old.setCategory(p.getCategory());
        old.setImageUrl(p.getImageUrl());

        return repo.save(old);

    }

    @DeleteMapping("/{id}")
    @Transactional
    public String deleteProduct(@PathVariable Long id){

        wishlistRepo.deleteWishlistByProductId(id);
        cartRepo.deleteCartByProductId(id);

        repo.deleteById(id);

        cacheService.clearCache();

        return "Deleted Successfully";

    }

    @GetMapping("/search")
    public List<Product> searchProducts(

            @RequestParam String keyword){

        return repo.searchProducts(keyword);

    }

}