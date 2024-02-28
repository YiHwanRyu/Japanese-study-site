package com.studynippon.api.validation.enums;

public enum PageSize {

	PAGE_SIZE_5(5),
	PAGE_SIZE_10(10),
	PAGE_SIZE_15(15),
	PAGE_SIZE_20(20);

	private int value;

	PageSize(int value) {
		this.value = value;
	}

	public int getSizeValue() {
		return value;
	}
}
