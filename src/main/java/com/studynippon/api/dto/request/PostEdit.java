package com.studynippon.api.dto.request;

import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;
import lombok.Getter;

@Getter
public class PostEdit {

	@NotEmpty(message = "수정 시 제목은 필수 입력입니다.")
	private String title;

	@NotEmpty(message = "수정 시 내용은 필수 입력입니다.")
	private String content;

	@Builder
	public PostEdit(String title, String content) {
		this.title = title;
		this.content = content;
	}
}
