package com.studynippon.api.validation.enums;

public enum SortParam {

	SORT_BY_LIKES("like"),
	SORT_BY_CLICKS("click"),
	SORT_BY_DATE("id");

	private String value;

	SortParam(String value) {
		this.value = value;
	}

	public String getSortParamValue() {
		return value;
	}
}
