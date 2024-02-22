package com.simple.book.vo;

public class WeatherVo {
	private String sky;
	
	public WeatherVo(String sky) {
		this.sky=sky;
	}
	
	public String getMessage() {
		return skyState();
	}
	
	private String skyState() {
		String message = "";
		switch (sky) {
		case "1":
			message = "맑음";
			break;
		case "3":
			message = "구름많음";
			break;
		case "4":
			message = "흐림";
			break;
		}
		return message;
	}
}
