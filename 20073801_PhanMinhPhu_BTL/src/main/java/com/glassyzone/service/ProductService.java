package com.glassyzone.service;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Controller;

import com.glassyzone.entity.Product;

@Controller
public interface ProductService {
	

	List<Product> findAll();

	

	
}
