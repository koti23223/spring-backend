package com.smartcart;

import com.smartcart.rest.OrderController;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ECommerceWebApplication {

    private final OrderController orderController;

    ECommerceWebApplication(OrderController orderController) {
        this.orderController = orderController;
    }

	public static void main(String[] args) {
		SpringApplication.run(ECommerceWebApplication.class, args);
		System.out.println("Rama koti");
		System.err.println("Error");
	}

}
