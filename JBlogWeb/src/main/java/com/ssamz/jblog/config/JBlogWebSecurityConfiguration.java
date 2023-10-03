package com.ssamz.jblog.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.ssamz.jblog.security.Oauth2UserDetailsServiceImpl;
import com.ssamz.jblog.security.UserDetailsServiceImpl;

@Configuration
@EnableWebSecurity
public class JBlogWebSecurityConfiguration extends WebSecurityConfigurerAdapter {
	
	// 사용자가 입력한 username으로 사용자를 인증하는 객체. 
	@Autowired
	private UserDetailsServiceImpl userDetailsService;
	
	@Autowired
	private Oauth2UserDetailsServiceImpl oauth2DetailsService;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Bean
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}
	
//	@Bean
//	public PasswordEncoder passwordEncoder() {
//		return new BCryptPasswordEncoder();
//	}
	
	// 사용자가 입력한 username으로 User 객체를 검색하고 password를 비교한다.
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder);
	}
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests().antMatchers("/webjars/**", "/js/**", "/image/**", "/", "/auth/**", "/oauth/**").permitAll();
		http.authorizeRequests().anyRequest().authenticated();
		http.csrf().disable();
		http.formLogin().loginPage("/auth/login");
		http.formLogin().loginProcessingUrl("/auth/securitylogin");
		http.logout().logoutUrl("/auth/logout").logoutSuccessUrl("/");
		
		// OAuth2 로그인 설정을 시작한다.
		http.oauth2Login()
		// OAuth2로 사용자 정보를 가져온다.
		.userInfoEndpoint()
		// userInfoEndpoint()로 가져온 사용자 정보를 이용해서 auth2DetailsService 객체로 사후처리한다.
		.userService(oauth2DetailsService);
	}
}

