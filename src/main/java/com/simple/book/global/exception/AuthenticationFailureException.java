package com.simple.book.global.exception;

import com.simple.book.global.advice.ErrorCode;

public class AuthenticationFailureException extends BusinessException{
    public AuthenticationFailureException(String message, ErrorCode errorCode) {
        super(message, errorCode);
    }
}
