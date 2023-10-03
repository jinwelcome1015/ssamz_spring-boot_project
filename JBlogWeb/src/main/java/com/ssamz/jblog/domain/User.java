package com.ssamz.jblog.domain;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.hibernate.annotations.CreationTimestamp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class User {
	// Primary Key와 매핑되는 식별자 변수
	@Id
	// 1부터 시작하여 자동으로 1씩 증가하도록 증가 전략 설정 
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id; 				// 회원 일련번호
	
	@Column(nullable = false, length = 50, unique = true)
	private String username; 		// 로그인 아이디
	
	@Column(length = 100)
	private String password; 		// 비밀번호
	
	@Column(nullable = false, length = 100)
	private String email;
	
	@Enumerated(EnumType.STRING) 		// 설정할 수 있는 값을 제한한다.
	private RoleType role;
	
	@CreationTimestamp 			// 현재 시간이 기본 값으로 등록되도록 설정 
	private Timestamp createDate;
	
	@Enumerated(EnumType.STRING) 
	private OAuthType oauth; 

}
