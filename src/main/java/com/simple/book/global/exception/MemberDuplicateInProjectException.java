package com.simple.book.global.exception;

import com.simple.book.global.advice.ErrorCode;

public class MemberDuplicateInProjectException extends BusinessException{
    public MemberDuplicateInProjectException(String message) {
        super(message, ErrorCode.MEMBER_DUPLICATE_IN_PROJECT);
    }
}
