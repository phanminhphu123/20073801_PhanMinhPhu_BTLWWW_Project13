package com.glassyzone.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.glassyzone.entity.User;
import com.glassyzone.repository.UserDAO;
import com.glassyzone.service.CookieUtils;
import com.glassyzone.service.SessionService;

@Controller
public class LoginController {
	@Autowired
	HttpServletResponse response;
	@Autowired
	HttpServletRequest request;
	@Autowired
	HttpSession session;
	@Autowired
	UserDAO userDAO;
	@Autowired
	CookieUtils cookieUtils;
	@Autowired
	SessionService sessionService;

	@RequestMapping("/login")
	public String index(Model model) {
		String username = cookieUtils.getCookieValue(request, "username");
		String password = cookieUtils.getCookieValue(request, "password");
		model.addAttribute("username", username);
		model.addAttribute("password", password);
		return "security/login";
	}

	@PostMapping("/login")
	public String checkLogin(Model model, @RequestParam(name = "remember-me", required = false) Boolean remember,
			@RequestParam("username") String username, @RequestParam("password") String password) throws IOException {
		User user = userDAO.findByUsername(username);
		try {
			if (user != null) {
				if (user.getPassword().equals(password)) {
					sessionService.set("user", user);
					if (user.getRole().equalsIgnoreCase("admin")) {
						if (remember != null) {
							cookieUtils.setCookieValue(response, "username", username, 15 * 24);
							cookieUtils.setCookieValue(response, "password", password, 15 * 24);
						}
						return "/admin/product";
					} else {
						if (remember != null) {
							cookieUtils.setCookieValue(response, "username", username, 15 * 24);
							cookieUtils.setCookieValue(response, "password", password, 15 * 24);
						}
						return "/home";
					}
				} else {
					model.addAttribute("message", "Sai tên đăng nhập hoặc mật khẩu!");
				}
			} else {
				model.addAttribute("message", "Sai tên đăng nhập hoặc mật khẩu!");
			}
		} catch (Exception e) {
			System.out.print(e);
			model.addAttribute("message", "Lỗi không xác định, xin thử lại!");
		}

		return "security/login";
	}

	@RequestMapping("/logout")
	public String logout() {
		session.removeAttribute("user");
		return "redirect:/login";
	}

	@GetMapping("/signup")
	public String indexSignup(Model model) {
		model.addAttribute("user", new User());
		return "security/signup";
	}

	@PostMapping("/signup")
	public String signUp(@ModelAttribute("user") @Validated User user, BindingResult result, Model model,
			@RequestParam("confirmPass") String confirm) throws InterruptedException {
		if (result.hasErrors()) {
			model.addAttribute("message", "Vui lòng điền thông tin hợp lệ!");
		} else {
			if (userDAO.findByUsername(user.getUsername()) != null) {
				model.addAttribute("message", "Tên đăng nhập đã tồn tại!");
			} else if (!user.getPassword().equals(confirm)) {
				model.addAttribute("message", "Xác nhận mật khẩu không đúng!");
			} else {
				user.setRole("user");
				userDAO.save(user);
				return "redirect:/home";
			}
		}
		return "security/signup";
	}

	@RequestMapping("/unauthorized")
	public String unauthorize(Model model) {
		model.addAttribute("message", "Bạn không có quyền truy xuất");
		return "security/login";
	}
}
