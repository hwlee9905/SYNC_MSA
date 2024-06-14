package com.simple.book.domain.search.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import com.simple.book.domain.search.service.SearchService;

@RestController
public class SearchController {

	@Autowired
	private SearchService searchService;
}
