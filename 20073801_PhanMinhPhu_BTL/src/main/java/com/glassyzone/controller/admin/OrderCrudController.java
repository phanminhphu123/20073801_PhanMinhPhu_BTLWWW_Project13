package com.glassyzone.controller.admin;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestTemplate;

import com.glassyzone.entity.Order;
import com.glassyzone.repository.OrderDAO;

@Controller
public class OrderCrudController {
	@Autowired
	OrderDAO orderDAO;

	@RequestMapping("/admin/order")
	public String list(Model model) {
		List<Order> list = orderDAO.findAll();
		model.addAttribute("order", list);
		return "admin/crud/order";
	}

	@RequestMapping("/admin/orderDetail/{id}")
	public String detail() {
		return "admin/crud/order";
	}

	@RequestMapping("/admin/order/checkout")
	public String list() {
		return "admin/crud/order";
	}
}
