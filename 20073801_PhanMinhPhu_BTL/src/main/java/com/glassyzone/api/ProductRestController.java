package com.glassyzone.api;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.glassyzone.entity.Product;
import com.glassyzone.repository.ProductDAO;

@RestController
@RequestMapping("/product")
@CrossOrigin("*")
public class ProductRestController {

	@Autowired
	ProductDAO productDAO;

	@GetMapping
	public ResponseEntity<List<Product>> getAllProducts() {
		return ResponseEntity.ok(productDAO.findAll());
	}

	@GetMapping("/sortAsc")
	public ResponseEntity<List<Product>> sortAsc() {
		return ResponseEntity.ok(productDAO.findAllByOrderByPriceAsc());
	}

	@GetMapping("/sortDesc")
	public ResponseEntity<List<Product>> sortDesc() {
		return ResponseEntity.ok(productDAO.findAllByOrderByPriceDesc());
	}

	@GetMapping("/{id}")
	public ResponseEntity<Product> getProductById(@PathVariable Integer id) {
		Product product = productDAO.findById(id).orElse(null);
		return product != null ? ResponseEntity.ok(product) : ResponseEntity.notFound().build();
	}

	@GetMapping("/search")
	public ResponseEntity<List<Product>> searchProductByName(@RequestParam String name) {
		List<Product> matchingProducts = productDAO.findByProductNameContainingIgnoreCase(name);
		if (!matchingProducts.isEmpty()) {
			return ResponseEntity.ok(matchingProducts);
		} else {
			return ResponseEntity.noContent().build();
		}
	}

	@PostMapping
	public ResponseEntity<Product> addProduct(@RequestBody Product product) {
		return ResponseEntity.ok(productDAO.save(product));
	}

	@PutMapping("/{id}")
	public ResponseEntity<Product> updateProduct(@PathVariable Integer id, @RequestBody Product product) {
		return ResponseEntity.ok(productDAO.save(product));
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteProduct(@PathVariable Integer id) {
		if (productDAO.existsById(id)) {
			productDAO.deleteById(id);
			return ResponseEntity.noContent().build();
		} else {
			return ResponseEntity.notFound().build();
		}
	}
}