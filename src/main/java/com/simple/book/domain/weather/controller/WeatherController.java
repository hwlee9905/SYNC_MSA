package com.simple.book.domain.weather.controller;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.simple.book.domain.weather.service.WeatherService;

@Controller
public class WeatherController {
	@Autowired
	private ObjectMapper mapper;
	
	@Autowired
	private WeatherService weatherService;

	@PostMapping("/weather")
	public ResponseEntity<String> weather(@RequestBody HashMap<String, Integer> body) throws Exception{
		HashMap<String, String> weather = weatherService.getWeather(body);
		return new ResponseEntity<>(mapper.writeValueAsString(weather), HttpStatus.OK);
	}
}
