package com.simple.book.global.exception;

import com.simple.book.global.advice.ErrorCode;

public class EntityNotFoundException extends BusinessException{

    public EntityNotFoundException(String message) {
        super(message, ErrorCode.ENTITY_NOT_FOUND);
    }
}
