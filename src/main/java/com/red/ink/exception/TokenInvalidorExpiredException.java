package com.red.ink.exception;

public class TokenInvalidorExpiredException extends  RuntimeException {

	private static final long serialVersionUID = 1L;

	public TokenInvalidorExpiredException(String message) {
		super(message);
	}

}
