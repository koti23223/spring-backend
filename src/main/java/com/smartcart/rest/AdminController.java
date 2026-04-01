package com.smartcart.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.smartcart.entity.Admin;
import com.smartcart.repository.AdminRepository;

@RestController
@RequestMapping("/api/admin")
public class AdminController {
	
	@Autowired
	private AdminRepository repo;
	
	@PostMapping("/register")
	public Admin register(@RequestBody Admin admin) {

		return repo.save(admin);

	}

	@PostMapping("/login")
	public String login(@RequestBody Admin admin) {

		Admin existing = repo.findByUsernameAndPassword(admin.getUsername(), admin.getPassword());

		if (existing != null) {
			return "Login Success";
		}

		return "Invalid Credentials";
	}

}
