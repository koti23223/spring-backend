package com.smartcart.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.smartcart.entity.Product;

public interface ProductRepository extends JpaRepository<Product,Long> {

	@Query("SELECT p FROM Product p WHERE " +
		       "LOWER(p.category) = LOWER(:keyword) OR " +
		       "LOWER(p.title) LIKE LOWER(CONCAT('%', :keyword, '%')) AND " +
		       "LOWER(p.title) NOT LIKE '%women%'")
		List<Product> searchProducts(@Param("keyword") String keyword);

}
