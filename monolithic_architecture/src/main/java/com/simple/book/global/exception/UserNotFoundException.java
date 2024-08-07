package com.simple.book.global.exception;

import com.simple.book.global.advice.ErrorCode;

public class UserNotFoundException extends BusinessException{
    public UserNotFoundException(String message) {
        super(message, ErrorCode.USER_NOT_FOUND);
    }

}
