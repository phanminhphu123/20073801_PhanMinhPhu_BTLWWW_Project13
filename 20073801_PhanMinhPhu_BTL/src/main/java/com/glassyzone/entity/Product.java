package com.glassyzone.entity;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;

import com.fasterxml.jackson.annotation.JsonIgnore;


@Entity
@Table(name = "Product")
public class Product {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	Integer productId;

	@NotBlank
	String productName;

	String productImage;

	String productDescription;

	@NotNull
	@PositiveOrZero
	Double productPrice;

	@JsonIgnore
	@ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
	@JoinColumn(name = "categoryId")
	Category category;

	public Integer getCategoryId() {
		return category != null ? category.getCategoryId() : null;
	}

	public void setCategoryId(Integer categoryId) {
		if (category == null) {
			category = new Category();
		}
		category.setCategoryId(categoryId);
	}

	public String getCategoryName() {
		return category != null ? category.getCategoryName() : null;
	}

	public void setCategoryName(String categoryName) {
		if (category == null) {
			category = new Category();
		}
		category.setCategoryName(categoryName);
	}

	public Integer getProductId() {
		return productId;
	}

	public void setProductId(Integer productId) {
		this.productId = productId;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getProductImage() {
		return productImage;
	}

	public void setProductImage(String productImage) {
		this.productImage = productImage;
	}

	public String getProductDescription() {
		return productDescription;
	}

	public void setProductDescription(String productDescription) {
		this.productDescription = productDescription;
	}

	public Double getProductPrice() {
		return productPrice;
	}

	public void setProductPrice(Double productPrice) {
		this.productPrice = productPrice;
	}

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	public Product(Integer productId, @NotBlank String productName, String productImage, String productDescription,
			@NotNull @PositiveOrZero Double productPrice, Category category) {
		super();
		this.productId = productId;
		this.productName = productName;
		this.productImage = productImage;
		this.productDescription = productDescription;
		this.productPrice = productPrice;
		this.category = category;
	}

	public Product() {
		super();
	}
	
}
