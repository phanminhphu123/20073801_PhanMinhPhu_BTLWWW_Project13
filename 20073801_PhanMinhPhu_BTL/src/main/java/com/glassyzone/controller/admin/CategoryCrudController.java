package com.glassyzone.controller.admin;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.glassyzone.entity.Category;
import com.glassyzone.repository.CategoryDAO;

@Controller
public class CategoryCrudController {
	@Autowired
	CategoryDAO categoryDAO;

	@GetMapping("/admin/category")
	public String userCRUD(Model model) {
		List<Category> list = categoryDAO.findAll();
		model.addAttribute("category", list);
		return "admin/crud/category";
	}

}
