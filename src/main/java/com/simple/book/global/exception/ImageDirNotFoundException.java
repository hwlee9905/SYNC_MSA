package com.simple.book.global.exception;

import com.simple.book.global.advice.ErrorCode;

public class ImageDirNotFoundException extends BusinessException{
	public ImageDirNotFoundException(String message) {
		super(message, ErrorCode.IMAGE_DIR_NOT_FOUND);
	}

}
