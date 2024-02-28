package com.studynippon.api.dto.response;

import java.util.List;

import lombok.Builder;
import lombok.Getter;

@Getter
public class PageResponse {

	private int totalPages;

	private boolean firstPage;

	private boolean lastPage;

	private List<PostDetail> postDtoList;

	@Builder
	public PageResponse(int totalPages, boolean firstPage, boolean lastPage, List<PostDetail> postDtoList) {
		this.totalPages = totalPages;
		this.firstPage = firstPage;
		this.lastPage = lastPage;
		this.postDtoList = postDtoList;
	}
}
