package com.simple.book.global.exception;

import com.simple.book.global.advice.ErrorCode;

public class ImageFileNotFoundException extends BusinessException{
	public ImageFileNotFoundException(String message) {
		super(message, ErrorCode.IMAGE_FILE_NOT_FOUND);
	}

}
