package com.studynippon.api.dto.request;

import org.springframework.data.domain.Sort;

import com.studynippon.api.validation.annotation.EnumValid;
import com.studynippon.api.validation.enums.PageSize;
import com.studynippon.api.validation.enums.SortParam;

import jakarta.validation.constraints.Min;
import lombok.Builder;
import lombok.Getter;

@Getter
public class PostSearch {

	@Min(message = "1페이지 이상이어야 합니다.", value = 1)
	private int pageNumber;

	@EnumValid(enumClass = PageSize.class, message = "조회 가능한 게시글 수가 아닙니다.")
	private String pageSize;

	@EnumValid(enumClass = SortParam.class, message = "등록순, 조회수, 좋아요 순만 정렬 가능합니다.")
	private String sortParam;

	@EnumValid(enumClass = Sort.Direction.class, message = "오름차순, 내림차순만 가능합니다.")
	private String pageSort;

	@Builder
	public PostSearch(int pageNumber, String pageSize, String sortParam, String pageSort) {
		this.pageNumber = pageNumber;
		this.pageSize = pageSize;
		this.sortParam = sortParam;
		this.pageSort = pageSort;
	}
}
