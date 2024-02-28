package com.studynippon.api.validation;

import com.studynippon.api.validation.annotation.EnumValid;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class EnumConstraint implements ConstraintValidator<EnumValid, String> {

	private EnumValid annotation;

	@Override
	public void initialize(EnumValid constraintAnnotation) {
		this.annotation = constraintAnnotation;
	}

	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {

		// null 체크
		if (value == null) {
			return false;
		}

		var enumValues = this.annotation.enumClass().getEnumConstants();

		// enum 가져와서 String 으로 변환 후 입력값(String)과 비교
		for (var enumValue : enumValues) {

			if (value.equals(enumValue.toString())) {
				return true;
			}
		}
		return false;
	}

}
