package com.glassyzone.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;

import com.glassyzone.entity.Product;

@Repository
@EnableJpaRepositories
public interface ProductDAO extends JpaRepository<Product, Integer> {
	@Query(nativeQuery = true, value = "SELECT * FROM product WHERE product_name = ?1")
	Product findByProductName(String productName);

	@Query(nativeQuery = true, value = "SELECT * FROM product ORDER BY product_price DESC LIMIT 3")
	List<Product> findByTop3();

	List<Product> findByProductNameContainingIgnoreCase(String productName);
	
	@Query(nativeQuery = true, value = "SELECT * FROM product ORDER BY product_price ASC")
	List<Product> findAllByOrderByPriceAsc();
	
	@Query(nativeQuery = true, value = "SELECT * FROM product ORDER BY product_price DESC")
	List<Product> findAllByOrderByPriceDesc();
}
