package com.simple.book.controller;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.simple.book.service.WeatherService;

@Controller
public class WeatherController {
	@Autowired
	private WeatherService weatherService;

	@PostMapping("/weather")
	@ResponseBody
	public String weather(@RequestBody HashMap<String, Integer> body) throws Exception{
		HashMap<String, String> weather = weatherService.getWeather(body);
		ObjectMapper mapper = new ObjectMapper();
		return mapper.writeValueAsString(weather);
	}
}
