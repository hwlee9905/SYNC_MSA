package com.simple.book.global.exception;

import com.simple.book.global.advice.ErrorCode;

public class IdenticalValuesCannotChangedException extends BusinessException{
	public IdenticalValuesCannotChangedException(String value) {
		super(value, ErrorCode.IDENTICAL_VALUE);
	}
}
