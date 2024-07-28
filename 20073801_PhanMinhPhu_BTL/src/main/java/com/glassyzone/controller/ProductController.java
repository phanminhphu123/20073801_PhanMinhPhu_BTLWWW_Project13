package com.glassyzone.controller;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

import com.glassyzone.entity.Product;
import com.glassyzone.repository.ProductDAO;

@Controller
public class ProductController {
	@Autowired
	ProductDAO productDAO;

	@Autowired
	HttpSession session;

	@Autowired
	RestTemplate restTemplate;

	@GetMapping("/products")
	public String showProducts(Model model) {
		// Gọi API để lấy danh sách sản phẩm
		ResponseEntity<List<Product>> response = restTemplate.exchange("http://localhost:8080/product", HttpMethod.GET,
				null, new ParameterizedTypeReference<List<Product>>() {
				});
		List<Product> products = response.getBody();
		model.addAttribute("products", products);
		return "product/product"; 
	}

	@GetMapping("/products/{id}")
	public String detailProduct(Model model, @PathVariable("id") Integer id) {
		ResponseEntity<Product> response = restTemplate.exchange("http://localhost:8080/product" + "/" + id,
				HttpMethod.GET, null, Product.class);
		Product product = response.getBody();
		model.addAttribute("detailProduct", product);
		return "product/detailProduct";
	}

	@GetMapping("/products/search")
	public String searchProduct(Model model, @RequestParam("name") String name) {
	    ResponseEntity<List<Product>> response = restTemplate.exchange(
	        "http://localhost:8080/product/search?name=" + name, HttpMethod.GET, null,
	        new ParameterizedTypeReference<List<Product>>() {}
	    );
	    HttpStatus statusCode = response.getStatusCode();
	    String message = "Không tìm thấy sản phẩm nào!";
	    
	    if (statusCode == HttpStatus.OK) {
	        List<Product> products = response.getBody();
	        if (products != null && !products.isEmpty()) {
	            System.out.println(products);
	            model.addAttribute("products", products);
	        } 
	    } else if (statusCode == HttpStatus.NOT_FOUND) {
	        model.addAttribute("message", message);
	    }
	    return "product/product";
	}


	@GetMapping("/products/sort/asc")
	public String sortAsc(Model model) {
		ResponseEntity<List<Product>> response = restTemplate.exchange("http://localhost:8080/product/sortAsc",
				HttpMethod.GET, null, new ParameterizedTypeReference<List<Product>>() {
				});
		List<Product> products = response.getBody();
		model.addAttribute("products", products);
		return "product/product";
	}

	@GetMapping("/products/sort/desc")
	public String sortDesc(Model model) {
		ResponseEntity<List<Product>> response = restTemplate.exchange("http://localhost:8080/product/sortDesc",
				HttpMethod.GET, null, new ParameterizedTypeReference<List<Product>>() {
				});
		List<Product> products = response.getBody();
		model.addAttribute("products", products);
		return "product/product";
	}
}
