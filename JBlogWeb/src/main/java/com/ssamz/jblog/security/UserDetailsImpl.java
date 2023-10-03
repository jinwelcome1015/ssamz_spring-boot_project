package com.ssamz.jblog.security;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import com.ssamz.jblog.domain.User;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDetailsImpl implements UserDetails, OAuth2User {
	private static final long serialVersionUID = 1L;
	private User user;
	
	// 구글에서 조회한 사용자 정보들을 담을 컬렉션
	private Map<String, Object> attributes;
	
	public UserDetailsImpl(User user) {
		this.user = user;
	}
	
	// OAuth 로그인시 사용할 생성자
	public UserDetailsImpl(User user, Map<String, Object> attributes) {
		this.user = user;
		this.attributes = attributes;
	}
	
	// 구글에서 조회한 사용자 정보가 저장된 컬렉션을 리턴한다.
	@Override
	public Map<String, Object> getAttributes() {
		return attributes;
	}

	// 이름은 사용하지 않는 정보이므로 null을 리턴한다.
	@Override
	public String getName() {
		return null;
	}

	@Override
	public String getPassword() {
		// {noop}은 비밀번호에 대해서 암호화를 처리하지 않겠다는 설정이다.
		return user.getPassword();
	}

	@Override
	public String getUsername() {
		return user.getUsername();
	}

	// 계정이 만료되지 않았는지 리턴한다. 
	@Override
	public boolean isAccountNonExpired() {
		return true; // 만료 안됐다. 
	}

	// 계정이 잠겨있는지 리턴한다. 
	@Override
	public boolean isAccountNonLocked() {
		return true; // 안 잠겨있다. 
	}

	// 비밀번호가 만료되지 않았는지 리턴한다. 
	@Override
	public boolean isCredentialsNonExpired() {
		return true; // 만료되지 않았다. 
	}

	// 계정이 활성화 되었는지를 리턴한다. 
	@Override
	public boolean isEnabled() {
		return true; // 활성화 되어있다.
	}
	
	// 계정이 가지고 있는 권한 목록(for문을 이용하여…)을 리턴한다.
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		// 권한 목록
		Collection<GrantedAuthority> roleList = new ArrayList<>();
		
		// 권한 목록 설정
		roleList.add(()-> {
			return "ROLE_" + user.getRole();
		});
		
		return roleList;
	}

}
