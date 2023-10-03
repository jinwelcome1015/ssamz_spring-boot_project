package com.ssamz.jblog.controller;

import javax.validation.Valid;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ssamz.jblog.domain.OAuthType;
import com.ssamz.jblog.domain.User;
import com.ssamz.jblog.dto.ResponseDTO;
import com.ssamz.jblog.dto.UserDTO;
import com.ssamz.jblog.security.UserDetailsImpl;
import com.ssamz.jblog.service.UserService;

@Controller
public class UserController {

	@Autowired
	private UserService userService;
	
	@Autowired
	private ModelMapper modelMapper;
	
	@Value("${kakao.default.password}")
	private String kakaoPassword;

	@Value("${google.default.password}")
	private String googlePassword;
	
	@PutMapping("/user")
	public @ResponseBody ResponseDTO<?> updateUser(@RequestBody User user, @AuthenticationPrincipal UserDetailsImpl principal) {
		// 회원 정보 수정 전, 로그인 성공한 사용자가 카카오 회원인지 구글 회원인지 확인한다. 
		if(principal.getUser().getOauth().equals(OAuthType.KAKAO)) {
			// 카카오 회원인 경우 비밀번호를 고정한다.
			user.setPassword(kakaoPassword);
		} else if(principal.getUser().getOauth().equals(OAuthType.GOOGLE)) {
			// 구글 회원인 경우 비밀번호를 고정한다.
			user.setPassword(googlePassword);
		}
		
		// 회원정보 수정과 동시에 세션 갱신
		principal.setUser(userService.updateUser(user));
	
		return new ResponseDTO<>(HttpStatus.OK.value(), user.getUsername() + 
		" 수정 완료");
	}

	
	@GetMapping("/user/updateUser") 
	public String updateUser() {
		return "user/updateUser";
	}

	
	@GetMapping("/auth/login")
	public String login() {
		return "system/login";
	}
	
	@GetMapping("/auth/insertUser") 
	public String insertUser() {
		return "user/insertUser";
	}
	
	@PostMapping("/auth/insertUser")
	public @ResponseBody ResponseDTO<?> insertUser(@Valid @RequestBody UserDTO userDTO, 
			  BindingResult bindingResult) {
		// UserDTO -> User 객체로 변환
		User user = modelMapper.map(userDTO, User.class);
		User findUser = userService.getUser(user.getUsername());
	
		if(findUser.getUsername() == null) {
			userService.insertUser(user);
			return new ResponseDTO<>(HttpStatus.OK.value(), 
			user.getUsername() + " 가입 성공.");
		} else {
			return new ResponseDTO<>(HttpStatus.BAD_REQUEST.value(), 
			user.getUsername() + "님은 이미 회원입니다.");
		}

	}
}
