
package com.smartcart.service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.smartcart.dto.LoginRequest;
import com.smartcart.dto.RegisterRequest;
import com.smartcart.dto.ResetPasswordRequest;
import com.smartcart.entity.ChangePassword;
import com.smartcart.entity.User;
import com.smartcart.exception.EmailAlreadyExistsException;
import com.smartcart.exception.InvalidCredentialsException;
import com.smartcart.exception.PasswordMismatchException;
import com.smartcart.repository.ChangePasswordRepository;
import com.smartcart.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;

	@Autowired
	private final ChangePasswordRepository changeRepo;
	@Autowired
	private final EmailService emailService;

	@Override
	public String register(RegisterRequest request) {
		if (!request.getPassword().equals(request.getConfirmPassword())) {
			throw new PasswordMismatchException("Passwords do not match");
		}

		if (userRepository.findByEmail(request.getEmail()).isPresent()) {
			throw new EmailAlreadyExistsException("Email already registered");
		}

		User user = User.builder().fullName(request.getFullName()).email(request.getEmail())
				.password(passwordEncoder.encode(request.getPassword())).build();

		userRepository.save(user);

		return "User registered successfully";
	}

	@Override
	public String login(LoginRequest request) {

		User user = userRepository.findByEmail(request.getEmail())
				.orElseThrow(() -> new InvalidCredentialsException("Invalid email or password"));

		if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
			throw new InvalidCredentialsException("Invalid email or password");
		}

		return "Login successful";
	}

	@Override
	public String sendOtp(String email) {

	    String otp = String.valueOf(new Random().nextInt(900000) + 100000);

	    ChangePassword cp;

	    Optional<ChangePassword> existing = changeRepo.findByEmail(email);

	    if (existing.isPresent()) {
	        cp = existing.get();
	    } else {
	        cp = new ChangePassword();
	        cp.setEmail(email);
	        cp.setResetCount(0); // set only once for new record
	    }

	    cp.setOtp(otp);
	    cp.setOtpExpiry(LocalDateTime.now().plusMinutes(5));

	    changeRepo.save(cp);

	    emailService.sendOtp(email, otp);

	    return "OTP Sent Successfully";
	}
	@Override
	public String resetPassword(ResetPasswordRequest request) {

	    ChangePassword cp = changeRepo.findByEmail(request.getEmail())
	            .orElseThrow(() -> new RuntimeException("OTP not requested"));

	    if (!cp.getOtp().equals(request.getOtp())) {
	        return "Invalid OTP";
	    }

	    if (cp.getOtpExpiry().isBefore(LocalDateTime.now())) {
	        return "OTP expired";
	    }

	    if (cp.getResetCount() >= 3) {
	        return "Password reset limit reached. Please request new OTP.";
	    }

	    User user = userRepository.findByEmail(request.getEmail())
	            .orElseThrow(() -> new RuntimeException("User not found"));

	    user.setPassword(passwordEncoder.encode(request.getNewPassword()));
	    userRepository.save(user);

	    cp.setResetCount(cp.getResetCount() + 1);

	    // optional: invalidate OTP after successful reset
	    cp.setOtp(null);
	    cp.setOtpExpiry(null);

	    changeRepo.save(cp);

	    int remaining = 3 - cp.getResetCount();

	    return "Password changed successfully. Remaining attempts: " + remaining;
	}
}
