package com.studynippon.api.exception;

public class PostNotFound extends CustomException {

	private static final String MESSAGE = "게시글을 찾을 수 없습니다.";

	public PostNotFound() {
		super(MESSAGE);
	}

	@Override
	public int getStatusCode() {
		return 404;
	}

}
