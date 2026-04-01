
package com.smartcart.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.smartcart.entity.Product;
import com.smartcart.repository.ProductRepository;

@Service
public class ProductCacheService {

    @Autowired
    private ProductRepository repo;

    private List<Product> cachedProducts;

    private long lastLoadedTime = 0;

    private final long CACHE_DURATION = 60000;

    public List<Product> getProducts() {

        long now = System.currentTimeMillis();

        if (cachedProducts == null ||
                (now - lastLoadedTime) > CACHE_DURATION) {

            System.out.println("🔥 Loading Products From DATABASE");

            cachedProducts = repo.findAll();

            lastLoadedTime = now;

        } else {

            System.out.println("⚡ Loading Products From CACHE");

        }

        return cachedProducts;
    }

    public void clearCache() {

        cachedProducts = null;
        lastLoadedTime = 0;

        System.out.println("🧹 Product Cache Cleared");
    }
}