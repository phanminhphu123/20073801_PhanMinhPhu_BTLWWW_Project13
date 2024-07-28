package com.glassyzone.controller.admin;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestTemplate;

import com.glassyzone.entity.User;
import com.glassyzone.repository.UserDAO;

@Controller
public class UserCrudController {
	@Autowired
	UserDAO userDAO;

	@RequestMapping("/admin/user")
	public String get(Model model) {
		List<User> list = userDAO.findAll();
		model.addAttribute("user", list);
		return "admin/crud/user";
	}


	@PostMapping("/admin/user/create")
	public String post(Model model, @ModelAttribute("user") User user) {

		if (!userDAO.existsById(user.getId())) {
			model.addAttribute("message", "Category ID already exist!");
		} else {
			userDAO.save(user);
			List<User> u = userDAO.findAll();
			model.addAttribute("user", u);
		}
		return "admin/crud/user";
	}

	
	@DeleteMapping("/admin/user/delete/{id}")
	public String delete(@PathVariable("id") Integer id) {
		userDAO.deleteById(id);
		return "admin/crud/user";
	}
	
}
