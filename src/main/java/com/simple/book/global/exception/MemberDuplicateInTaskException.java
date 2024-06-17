package com.simple.book.global.exception;

import com.simple.book.global.advice.ErrorCode;

public class MemberDuplicateInTaskException extends BusinessException{
    public MemberDuplicateInTaskException(String message) {
        super(message, ErrorCode.MEMBER_DUPLICATE_IN_TASK);
    }
}
