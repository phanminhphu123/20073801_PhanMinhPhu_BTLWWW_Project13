package com.glassyzone.controller;

import java.util.Date;
import java.util.Random;
import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.Email;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.SimpleEmail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.glassyzone.entity.Order;
import com.glassyzone.entity.OrderDetail;
import com.glassyzone.repository.OrderDAO;
import com.glassyzone.repository.OrderDetailDAO;
import com.glassyzone.service.SessionService;

@Controller
public class OrderController {
	@Autowired
	private OrderDAO orderDAO;
	Order order = new Order();
	@Autowired
	private OrderDetailDAO orderDetailDAO;
	@Autowired
	SessionService sessionService;

	@RequestMapping("/order")
	public String index() {
		return "cart/cart";
	}

	@GetMapping("/order/checkout")
	public String checkout1() {
		return "cart/checkout";
	}

	@GetMapping("/order/thank")
	public String t() {
		return "cart/thank";
	}

	@PostMapping("/order/checkout")
	public String checkout2(Model model, @ModelAttribute OrderDetail orderDetail) {

		String verificationCode = generateVerificationCode();
		sessionService.set("verificationCode", verificationCode);
		sessionService.set("orderDetail", orderDetail);
		try {
			sessionService.set("verificationCode", verificationCode);
			Email email = new SimpleEmail();
			email.setHostName("smtp.gmail.com");
			email.setSmtpPort(587);
			email.setAuthenticator(new DefaultAuthenticator("phanminhphu4@gmail.com", "xkrr cwnm vcgb lnus"));
			email.setSSLOnConnect(true);
			email.setFrom("hodanhnhan1166@gmail.com");
			email.setSubject("Mã order dùng 1 lần");
			email.setMsg("Đây là mã dùng 1 lần của bạn, không chia sẻ nó với bất kỳ ai.\n" + verificationCode
					+ " .\n\nThank you,\nGlassyzone!");
			email.addTo(orderDetail.getEmail());
			email.send();

		} catch (EmailException e) {
			System.out.println("Error sending email: " + e.getMessage());
		}
		return "cart/validate";
	}

	@GetMapping("/order/validate")
	public String validate1() {
		return "cart/validate";
	}

	@PostMapping("/order/validate")
	public String validate2(Model model, @RequestParam("OTPcode") String code) {
		String verificationCode = (String) sessionService.get("verificationCode");
		// kiểm tra OTP
		System.out.println(code);
		System.out.println(verificationCode);

		OrderDetail orderDetail = (OrderDetail) sessionService.get("orderDetail");
		if (verificationCode.equals(code)) {
			sessionService.remove("verificationCode");
			// Lưu thông tin khách hàng vào cơ sở dữ liệu
			Order order = new Order();
			order.setOrderDate(new Date());
			orderDAO.save(order);

			orderDetail.setOrder(order);
			orderDetail.setCreatedAt(new Date());
			orderDetailDAO.save(orderDetail);

			return "cart/thank";
		} else {
			model.addAttribute("message", "Mã xác thực không đúng");
			return "cart/validate";
		}
	}

	public static String generateVerificationCode() {
		Random random = new Random();
		int min = 100000;
		int max = 999999;
		int code = random.nextInt((max - min) + 1) + min;
		return String.valueOf(code);
	}

}