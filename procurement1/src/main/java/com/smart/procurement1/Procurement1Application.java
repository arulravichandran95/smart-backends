package com.smart.procurement1;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@org.springframework.context.annotation.ComponentScan(basePackages = { "com.smart.procurement1",
		"com.smart.procurement1.controller" })
public class Procurement1Application {

	public static void main(String[] args) {
		SpringApplication.run(Procurement1Application.class, args);
	}

}
