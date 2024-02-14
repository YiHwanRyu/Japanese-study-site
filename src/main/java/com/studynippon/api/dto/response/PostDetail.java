package com.studynippon.api.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
public class PostDetail {

	private String title;

	private String content;

	@Builder
	public PostDetail(String title, String content) {
		this.title = title;
		this.content = content;
	}
}
