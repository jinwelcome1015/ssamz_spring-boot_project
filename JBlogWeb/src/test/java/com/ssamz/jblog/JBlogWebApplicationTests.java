package com.ssamz.jblog;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootTest
class JBlogWebApplicationTests {

	@Test
	void contextLoads() {
	}

	@Test
	public void passwordEncode() {
		
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		String rawPassword = "abc123@@";
		String encodedPassword = encoder.encode(rawPassword);
		System.out.println("암호화되기 전 비번 : " + rawPassword);
		System.out.println("암호화된 이후 비번 : " + encodedPassword);
		System.out.println("비번 비교 : " + encoder.matches(rawPassword, encodedPassword));
	}
}
