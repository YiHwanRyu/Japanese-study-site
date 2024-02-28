package com.studynippon.api.validation.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.studynippon.api.validation.EnumConstraint;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = EnumConstraint.class)
public @interface EnumValid {
	String message() default "허용되지 않은 값입니다.";

	Class<?>[] groups() default {};

	Class<? extends Enum<?>> enumClass();

	Class<? extends Payload>[] payload() default {};

}
