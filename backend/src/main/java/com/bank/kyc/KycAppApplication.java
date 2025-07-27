package com.bank.kyc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(exclude = {
	    org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration.class
	})
public class KycAppApplication {
	
	public static void main(String[] args) {
		SpringApplication.run(KycAppApplication.class, args);
	}

}



