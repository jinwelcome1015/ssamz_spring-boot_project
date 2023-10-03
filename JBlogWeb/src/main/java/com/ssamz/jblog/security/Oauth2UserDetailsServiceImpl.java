package com.ssamz.jblog.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import com.ssamz.jblog.domain.OAuthType;
import com.ssamz.jblog.domain.RoleType;
import com.ssamz.jblog.domain.User;
import com.ssamz.jblog.persistence.UserRepository;

@Service
public class Oauth2UserDetailsServiceImpl extends DefaultOAuth2UserService {

	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	private UserRepository userRepository;
	
	@Value("${google.default.password}")
	private String googlePassword;
	
	// 1. 사용자가 구글 로그인에 성공하면 구글은 인증 클라이언트 쪽으로 CODE를 전달한다.
	// 2. OAuth2 Client는 전달된 CODE를 기반으로 AccessToken이 저장된 OAuth2UserRequest를 리턴받는다. 
	// 3. OAuth2 Client는 OAuth2UserRequest를 인자로 넘기면서 loadUser() 메소드를 호출해준다.
	@Override
	public OAuth2User loadUser(OAuth2UserRequest userRequest) 
	throws OAuth2AuthenticationException {
		// AccessToken이 저장된 userRequest를 이용하여 구글로부터 회원 정보를 받아온다.
		OAuth2User oauth2User = super.loadUser(userRequest);
		
		// 구글이 전달해준 정보를 바탕으로 우리 시스템에 저장할 회원 정보를 만든다.
		String providerId = oauth2User.getAttribute("sub");
		String email = oauth2User.getAttribute("email");
		String username = email + "_" + providerId; 
		String password = passwordEncoder.encode(googlePassword);		
		
		// 회원가입이 되어있는 사용자인지 확인한다. 
		User findUser = userRepository.findByUsername(username).orElseGet(()->{
			return new User();
		});
		
		if(findUser.getUsername() == null) {
			// 신규 가입 처리
			findUser = User.builder()
				.username(username)
				.password(password)
				.email(email)
				.role(RoleType.USER)
				.oauth(OAuthType.GOOGLE)
				.build();
			userRepository.save(findUser);
		} 
		// OAuth2 Client가 자동으로 리턴된 객체를 기반으로 세션을 갱신해준다.
		return new UserDetailsImpl(findUser, oauth2User.getAttributes());
	}
}
