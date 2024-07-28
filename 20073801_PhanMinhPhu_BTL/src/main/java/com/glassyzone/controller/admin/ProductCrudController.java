package com.glassyzone.controller.admin;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

import javax.validation.constraints.NotBlank;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.glassyzone.entity.Product;
import com.glassyzone.repository.ProductDAO;

@Controller
public class ProductCrudController {
	@Autowired
	ProductDAO productDAO;
	@Autowired
	RestTemplate restTemplate;

	@RequestMapping("/admin/products")
	public String getAllProduct(Model model, @ModelAttribute("product") Product product) {
		loadAll(model);
		return "admin/crud/product";
	}

	@PostMapping("/admin/products/create")
	public String createProduct(Model model, @Validated @ModelAttribute("product") Product product, Errors errors,
			@RequestParam("productImage") MultipartFile productImage) throws IllegalStateException, IOException {
		if (product.getProductName().isBlank() || product.getProductPrice() == null
				|| product.getCategoryName() == null) {
			model.addAttribute("message", "Vui lòng điền thông tin hợp lệ!");
		} else {
			if (!productImage.isEmpty()) {
				String fileName = "product_" + UUID.randomUUID() + ".jpg";
				Path imagePath = Paths.get("src/main/resources/static/image/" + fileName);
				Files.write(imagePath, productImage.getBytes());
				product.setProductImage(fileName);
			}

		}
		Product p = productDAO.findByProductName(product.getProductName());
		if (p == null) {
			productDAO.save(product);
			// Đẩy vào cơ sở dữ liệu qua API
			restTemplate.postForObject("http://localhost:8080/product", product, Product.class);
		} else {
			model.addAttribute("message", "Tên sản phẩm đã tồn tại!");
		}
		loadAll(model);
		return "admin/crud/product";
	}

	// edit - update
	@RequestMapping("/admin/products/update/{id}")
	public String update(Model model, @PathVariable Integer id, @ModelAttribute("product") Product product,
	                     BindingResult errors, @RequestParam("productImage") MultipartFile productImage) throws IOException {

	    ResponseEntity<Product> response = restTemplate.exchange("http://localhost:8080/product/" + id, HttpMethod.GET,
	            null, Product.class);
	    Product existingProduct = response.getBody();

	    if (existingProduct != null) {
	        existingProduct.setProductName(product.getProductName());
	        existingProduct.setProductPrice(product.getProductPrice());
	        existingProduct.setProductDescription(product.getProductDescription());

	        if (!productImage.isEmpty()) {
	            String fileName = "product_" + UUID.randomUUID() + ".jpg";
	            Path imagePath = Paths.get("src/main/resources/static/image/" + fileName);
	            Files.write(imagePath, productImage.getBytes());
	            existingProduct.setProductImage(fileName);
	        }

	        restTemplate.put("http://localhost:8080/product/" + id, existingProduct);

	        model.addAttribute("message", "Cập nhật sản phẩm thành công!");
	    } else {
	        restTemplate.postForObject("http://localhost:8080/product", product, Product.class);

	        model.addAttribute("message", "Thêm mới sản phẩm thành công!");
	    }

	    loadAll(model);
	    return "admin/crud/product";
	}


	// edit
	@RequestMapping("/admin/products/edit/{id}")
	public String editProduct(Model model, @PathVariable("id") Integer id, @ModelAttribute("product") Product product) {
		Product p = productDAO.findById(id).get();
		product.setProductId(p.getProductId());
		product.setProductName(p.getProductName());
		product.setProductImage(p.getProductImage());
		product.setProductPrice(p.getProductPrice());
		product.setProductDescription(p.getProductDescription());
		product.setCategoryName(p.getCategoryName());
		loadAll(model);
		return "admin/crud/product";
	}

	// delete
	@RequestMapping("/admin/products/remove/{id}")
	public String deleteProduct(RedirectAttributes redirectAttributes, @PathVariable("id") Integer id) {
		if (id == null || id <= 0) {
			redirectAttributes.addFlashAttribute("message", "Vui lòng chọn sản phẩm cần xoá!");
		} else {
			try {
				restTemplate.delete("http://localhost:8080/product/" + id);
				redirectAttributes.addFlashAttribute("message", "Xoá sản phẩm thành công!");
			} catch (HttpClientErrorException ex) {
				if (ex.getStatusCode() == HttpStatus.NOT_FOUND) {
					redirectAttributes.addFlashAttribute("message", "Không tìm thấy sản phẩm để xoá!");
				} else {
					redirectAttributes.addFlashAttribute("message", "Có lỗi xảy ra khi xoá sản phẩm!");
				}
			}
		}
		return "redirect:/admin/products";
	}

	// reset
	@RequestMapping("/admin/products/reset")
	public String reset(Model model) {
		return "redirect:/admin/products";
	}

	void loadAll(Model model) {
		// Gọi API để lấy danh sách sản phẩm
		ResponseEntity<List<Product>> response = restTemplate.exchange("http://localhost:8080/product", HttpMethod.GET,
				null, new ParameterizedTypeReference<List<Product>>() {
				});
		List<Product> products = response.getBody();

		model.addAttribute("products", products);
	}

}