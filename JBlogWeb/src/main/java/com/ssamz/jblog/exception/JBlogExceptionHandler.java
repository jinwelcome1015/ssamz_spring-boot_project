package com.ssamz.jblog.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;

import com.ssamz.jblog.dto.ResponseDTO;

@ControllerAdvice
@RestController
public class JBlogExceptionHandler {

	@ExceptionHandler(value = Exception.class)
	public ResponseDTO<String> globalExceptionHandler(Exception e) {
		e.printStackTrace();
		return new ResponseDTO<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), 
		e.getMessage());

	}

}
