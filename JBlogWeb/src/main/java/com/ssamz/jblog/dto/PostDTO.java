package com.ssamz.jblog.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostDTO {
		
	@NotNull(message = "title 파라미터가 전달되지 않았습니다.")
	@NotBlank(message = "제목은 필수 입력 항목입니다.")
	private String title; 
	
	@NotNull(message = "content 파라미터가 전달되지 않았습니다.")
	@NotBlank(message = "내용은 필수 입력 항목입니다.")
	private String content;
}
