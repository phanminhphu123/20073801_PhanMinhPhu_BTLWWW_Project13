package com.glassyzone;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "com.glassyzone") 
public class J6D5Application {
	public static void main(String[] args) {
		SpringApplication.run(J6D5Application.class, args);
	}
}
//chạy dự án tại file này