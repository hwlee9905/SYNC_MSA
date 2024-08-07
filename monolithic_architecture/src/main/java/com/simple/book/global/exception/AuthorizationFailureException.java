package com.simple.book.global.exception;

import com.simple.book.global.advice.ErrorCode;

public class AuthorizationFailureException extends BusinessException{
    public AuthorizationFailureException(String message, ErrorCode errorCode) {
        super(message, errorCode);
    }

}
