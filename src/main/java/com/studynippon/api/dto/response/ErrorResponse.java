package com.studynippon.api.dto.response;

import java.util.List;

import lombok.Builder;
import lombok.Getter;

@Getter
public class ErrorResponse {

	private final String statusCode;
	private final String errorMessage;
	private final List<Validation> validationList;

	@Builder
	public ErrorResponse(String statusCode, String errorMessage, List<Validation> validationList) {
		this.statusCode = statusCode;
		this.errorMessage = errorMessage;
		this.validationList = validationList;
	}

}
