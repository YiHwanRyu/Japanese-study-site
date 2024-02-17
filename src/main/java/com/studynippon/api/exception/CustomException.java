package com.studynippon.api.exception;

import java.util.ArrayList;
import java.util.List;

import com.studynippon.api.dto.response.Validation;

import lombok.Getter;

@Getter
public abstract class CustomException extends RuntimeException {

	public final List<Validation> validationList = new ArrayList<>();

	public CustomException(String message) {
		super(message);
	}

	public CustomException(String message, Throwable cause) {
		super(message, cause);
	}

	public abstract int getStatusCode();

	// 원인 필드 추가
	public void addValidation(Validation validation) {
		validationList.add(validation);
	}

}
