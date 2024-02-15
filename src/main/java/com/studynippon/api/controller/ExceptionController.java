package com.studynippon.api.controller;

import static org.springframework.http.HttpStatus.*;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.studynippon.api.dto.response.ErrorResponse;
import com.studynippon.api.dto.response.Validation;

@RestControllerAdvice
public class ExceptionController {

	// @Valid 검증 에러 메서드
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ErrorResponse> methodArgumentNotValidExceptionHandler(MethodArgumentNotValidException e) {
		// 검증 필드 리스트 생성
		List<Validation> validationList = new ArrayList<>();

		// 검증한 내역 리스트에 입력
		for (FieldError fieldError : e.getFieldErrors()) {
			Validation validation = Validation.builder()
				.errorField(fieldError.getField())
				.errorFieldMessage(fieldError.getDefaultMessage())
				.build();
			validationList.add(validation);
		}

		// 에러 생성
		ErrorResponse response = ErrorResponse.builder()
			.statusCode("400")
			.errorMessage("잘못된 요청입니다.")
			.validationList(validationList)
			.build();

		// 검증한 리스트와 함께 에러 리턴
		return ResponseEntity
			.status(BAD_REQUEST)
			.body(response);
	}

}
