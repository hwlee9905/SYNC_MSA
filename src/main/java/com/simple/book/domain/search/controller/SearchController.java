package com.simple.book.domain.search.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.simple.book.domain.search.service.SearchService;

@RestController
public class SearchController {

	@Autowired
	private SearchService searchService;
	
	@GetMapping("/search/test")
	public ResponseEntity<?> testSearch(@RequestParam(name = "text") String text){
		return ResponseEntity.ok().body(searchService.test(text));
	}
}
