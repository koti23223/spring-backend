package com.smartcart.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.smartcart.entity.Product;
import com.smartcart.repository.ProductRepository;

@Service
public class ProductService {

 @Autowired
 private ProductRepository repo;


 public List<Product> searchProducts(String keyword){

  return repo.searchProducts(keyword);

 }

}