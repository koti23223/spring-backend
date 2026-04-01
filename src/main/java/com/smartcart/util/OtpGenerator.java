package com.smartcart.util;

import java.util.Random;

public class OtpGenerator {
	 public static String generateOtp() {
	        Random random = new Random();
	        return String.valueOf(100000 + random.nextInt(900000));
	    }
}
