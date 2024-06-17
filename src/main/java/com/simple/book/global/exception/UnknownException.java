package com.simple.book.global.exception;

import com.simple.book.global.advice.ErrorCode;

public class UnknownException extends BusinessException{
	public UnknownException(String message) {
        super(message, ErrorCode.UNKNOWN_ERROR);
    }
}
