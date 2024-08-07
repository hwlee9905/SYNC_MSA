package com.simple.book.global.util;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.stereotype.Component;

@Component
public class DateFmt {
	public String getDate(String fmt) {
		SimpleDateFormat sdfDate = new SimpleDateFormat(fmt);
		return sdfDate.format(new Date());
	}
}
