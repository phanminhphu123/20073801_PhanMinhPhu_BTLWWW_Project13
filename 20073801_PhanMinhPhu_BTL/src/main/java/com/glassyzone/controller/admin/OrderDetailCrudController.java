package com.glassyzone.controller.admin;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.glassyzone.entity.OrderDetail;
import com.glassyzone.repository.OrderDetailDAO;

@Controller
public class OrderDetailCrudController {
	@Autowired
	OrderDetailDAO orderDetailDAO;

	@GetMapping("/admin/orderDetail")
	public String orderDetailCRUD(Model model) {
		List<OrderDetail> list = orderDetailDAO.findAll();
		model.addAttribute("orderDetail", list);
		return "admin/crud/orderDetail";
	}
}