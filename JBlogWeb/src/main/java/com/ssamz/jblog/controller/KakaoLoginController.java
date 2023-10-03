package com.ssamz.jblog.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import com.ssamz.jblog.domain.User;
import com.ssamz.jblog.service.KakaoLoginService;
import com.ssamz.jblog.service.UserService;

@Controller
public class KakaoLoginController {

	@Autowired
	private KakaoLoginService kakaoLoginService;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private AuthenticationManager authenticationManager;	

	@Value("${kakao.default.password}")
	private String kakaoPassword;

	@GetMapping("/oauth/kakao")
	public String kakaoCallback(String code) {
		// 1. 인증 서버로부터 받은 CODE를 이용하여 AccessToken을 얻는다. 
		String accessToken = kakaoLoginService.getAccessToken(code);
		
		// 2. AccessToken을 이용하여 사용자 정보를 얻어온다.
		User kakaoUser = kakaoLoginService.getUserInfo(accessToken);
				
		// 3. 카카오 회원 정보를 이용하여 JBlogWeb 시스템에 회원가입한다.
		// 이미 가입된 회원인지 확인한다.
		User findUser = userService.getUser(kakaoUser.getUsername());
		if(findUser.getUsername() == null) {
			// 기존 회원이 아닌 경우 회원으로 등록한다.
			userService.insertUser(kakaoUser);
		}

		// 4. 카카오로부터 받은 사용자 정보를 기반으로 인증을 처리한다.
		UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(kakaoUser.getUsername(), kakaoPassword);
		Authentication authentication = authenticationManager.authenticate(authenticationToken);
		SecurityContextHolder.getContext().setAuthentication(authentication);
		
		return "redirect:/";
	}
}
