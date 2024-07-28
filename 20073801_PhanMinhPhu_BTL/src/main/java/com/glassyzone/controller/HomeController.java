package com.glassyzone.controller;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.glassyzone.entity.Product;
import com.glassyzone.repository.ProductDAO;


@Controller
@RequestMapping("/home")
public class HomeController {
	@Autowired
	ProductDAO productDAO;
	@Autowired
	HttpSession session;
	
	@RequestMapping
	public String index(Model model) {
		List<Product> products = productDAO.findByTop3();
		model.addAttribute("product", products);
		return "index";
	}
}
