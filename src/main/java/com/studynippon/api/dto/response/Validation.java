package com.studynippon.api.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
public class Validation {

	private final String errorField;
	private final String errorFieldMessage;

	@Builder
	public Validation(String errorField, String errorFieldMessage) {
		this.errorField = errorField;
		this.errorFieldMessage = errorFieldMessage;
	}
}